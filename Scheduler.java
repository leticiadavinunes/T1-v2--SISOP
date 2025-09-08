import java.util.LinkedList;
import java.util.Queue;

/**
 * Escalonador de Processos
 *
 * Implementa uma política de escalonamento round-robin com preempção por tempo.
 * Gerencia a fila de processos prontos e controla as trocas de contexto,
 * garantindo que cada processo execute por no máximo uma fatia de tempo (Delta)
 * antes de ser preemptado.
 */
public class Scheduler {
    // === ESTRUTURAS DE DADOS DO ESCALONADOR ===
    private Queue<PCB> readyQueue; // Fila de processos prontos (FIFO)
    private PCB currentProcess; // Processo atualmente em execução
    private HW hw; // Referência ao hardware (CPU + Memória)
    private MemoryManager memoryManager; // Gerente de memória para alocação/desalocação

    // === CONTROLE DE PROCESSOS ===
    private int processIdCounter; // Contador para gerar IDs únicos de processos

    // === CONTROLE DE TEMPO (PREEMPÇÃO) ===
    private int timeSlice; // Delta - tempo máximo de execução (em ciclos)
    private int currentTime; // Contador atual de ciclos do processo em execução

    /**
     * Construtor do Escalonador
     * 
     * @param hw            Hardware do sistema (CPU + Memória)
     * @param memoryManager Gerente de memória para alocação de processos
     * @param timeSlice     Fatia de tempo (Delta) em ciclos de CPU
     */
    public Scheduler(HW hw, MemoryManager memoryManager, int timeSlice) {
        this.hw = hw;
        this.memoryManager = memoryManager;
        this.timeSlice = timeSlice;
        this.readyQueue = new LinkedList<>(); // Fila FIFO para round-robin
        this.currentProcess = null; // Nenhum processo inicialmente
        this.processIdCounter = 1; // IDs começam em 1
        this.currentTime = 0; // Tempo zerado
    }

    /**
     * Adiciona um novo processo à fila de prontos
     *
     * Realiza todo o processo de criação de um novo processo:
     * 1. Calcula quantas páginas são necessárias
     * 2. Aloca memória através do gerente de memória
     * 3. Carrega o programa na memória usando paginação
     * 4. Cria o PCB do processo
     * 5. Adiciona o processo à fila de prontos
     *
     * @param programName Nome do programa a ser executado
     * @param program     Array de instruções do programa
     * @return true se o processo foi criado com sucesso, false caso contrário
     */
    public boolean addProcess(String programName, Word[] program) {
        // Calcula quantas páginas são necessárias para o programa
        int numPaginas = (int) Math.ceil((double) program.length / hw.mem.tamPg);
        int[] tabelaPaginas = new int[numPaginas];

        // Tenta alocar memória para o processo
        if (!memoryManager.aloca(program.length, tabelaPaginas)) {
            System.out.println("Erro: Não foi possível alocar memória para o processo " + programName);
            return false;
        }

        // Carrega o programa na memória usando o sistema de paginação
        hw.mem.carregarProgramaPaginado(program, tabelaPaginas);

        // Cria o PCB (Process Control Block) para o novo processo
        PCB pcb = new PCB(processIdCounter++, programName, tabelaPaginas, program.length);
        pcb.state = ProcessState.READY;

        // Adiciona o processo à fila de prontos para execução
        readyQueue.offer(pcb);

        System.out.println("Processo " + programName + " (ID: " + pcb.processId + ") adicionado à fila de prontos");
        return true;
    }

    /**
     * Realiza o escalonamento de processos
     *
     * Este é o coração do escalonador. Implementa a troca de contexto:
     * 1. Se há um processo atual, salva seu contexto
     * 2. Trata o processo atual conforme seu estado (preempção ou término)
     * 3. Seleciona o próximo processo da fila (round-robin)
     * 4. Restaura o contexto do novo processo na CPU
     * 5. Reinicia o contador de tempo da fatia
     */
    public void schedule() {
        // === TRATAMENTO DO PROCESSO ATUAL ===
        if (currentProcess != null) {
            // Salva o contexto completo do processo atual no seu PCB
            currentProcess.saveContext(hw.cpu);

            if (currentProcess.state == ProcessState.RUNNING) {
                // Processo foi preemptado por tempo - volta para fila de prontos
                currentProcess.state = ProcessState.READY;
                readyQueue.offer(currentProcess); // Adiciona no final da fila (round-robin)
                System.out.println("Processo " + currentProcess.processId + " preemptado e retornado à fila");
            } else if (currentProcess.state == ProcessState.TERMINATED) {
                // Processo terminou - libera seus recursos
                memoryManager.desaloca(currentProcess.tabelaPaginas);
                System.out.println("Processo " + currentProcess.processId + " finalizado e memória liberada");
            }
        }

        // === SELEÇÃO DO PRÓXIMO PROCESSO ===
        // Remove o primeiro processo da fila (FIFO = round-robin)
        currentProcess = readyQueue.poll();

        if (currentProcess != null) {
            // Novo processo assume a CPU
            currentProcess.state = ProcessState.RUNNING;
            currentProcess.restoreContext(hw.cpu); // Restaura contexto na CPU
            currentTime = 0; // Reinicia contador de tempo da fatia
            System.out.println(
                    "Processo " + currentProcess.processId + " (" + currentProcess.programName + ") em execução");
        } else {
            System.out.println("Nenhum processo na fila de prontos");
        }
    }

    /**
     * Verifica se o processo atual deve ser preemptado por tempo
     *
     * Incrementa o contador de tempo e verifica se atingiu a fatia máxima.
     * Este método é chamado a cada instrução executada pelo processo atual.
     *
     * @return true se o processo deve ser preemptado, false caso contrário
     */
    public boolean shouldPreempt() {
        currentTime++; // Incrementa contador de ciclos
        return currentTime >= timeSlice; // Verifica se atingiu o limite (Delta)
    }

    /**
     * Marca o processo atual como terminado
     *
     * Chamado quando o processo executa uma instrução STOP ou termina por erro.
     * O processo será removido do sistema na próxima chamada de schedule().
     */
    public void terminateCurrentProcess() {
        if (currentProcess != null) {
            currentProcess.state = ProcessState.TERMINATED;
            System.out.println("Processo " + currentProcess.processId + " marcado como terminado");
        }
    }

    /**
     * Verifica se há processos no sistema para executar
     *
     * @return true se há processo atual ou processos na fila de prontos
     */
    public boolean hasProcesses() {
        return currentProcess != null || !readyQueue.isEmpty();
    }

    /**
     * Retorna o processo atualmente em execução
     *
     * @return PCB do processo atual ou null se nenhum processo está executando
     */
    public PCB getCurrentProcess() {
        return currentProcess;
    }

    /**
     * Exibe informações detalhadas sobre o estado atual do escalonador
     *
     * Mostra:
     * - Processo atualmente em execução
     * - Número de processos na fila de prontos
     * - Tempo atual da fatia de execução
     * - Lista completa dos processos na fila
     */
    public void printStatus() {
        System.out.println("=== Status do Escalonador ===");
        System.out.println("Processo atual: " + (currentProcess != null ? currentProcess.toString() : "Nenhum"));
        System.out.println("Processos na fila: " + readyQueue.size());
        System.out.println("Tempo atual da fatia: " + currentTime + "/" + timeSlice);

        if (!readyQueue.isEmpty()) {
            System.out.println("Fila de prontos:");
            for (PCB pcb : readyQueue) {
                System.out.println("  " + pcb.toString());
            }
        }
        System.out.println("=============================");
    }
}