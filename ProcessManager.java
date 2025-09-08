import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de Processos
 *
 * Classe de alto nível que coordena o gerenciamento de processos no sistema.
 * Integra o escalonador, gerente de memória e repositório de programas,
 * oferecendo uma interface unificada para:
 * - Carregamento de programas
 * - Execução escalonada (comando execAll)
 * - Execução contínua em background
 * - Monitoramento do sistema
 */
public class ProcessManager {
    // === COMPONENTES DO SISTEMA ===
    private Scheduler scheduler; // Escalonador de processos
    private Programs programs; // Repositório de programas disponíveis
    private HW hw; // Hardware (CPU + Memória)
    private MemoryManager memoryManager; // Gerente de memória

    // === CONTROLE DE EXECUÇÃO ===
    private Map<String, Word[]> loadedPrograms; // Cache de programas carregados
    private boolean continuousExecution; // Flag para execução contínua
    private Thread schedulerThread; // Thread para execução em background

    /**
     * Construtor do Gerenciador de Processos
     *
     * @param hw            Hardware do sistema
     * @param memoryManager Gerente de memória
     * @param programs      Repositório de programas
     */
    public ProcessManager(HW hw, MemoryManager memoryManager, Programs programs) {
        this.hw = hw;
        this.memoryManager = memoryManager;
        this.programs = programs;
        this.scheduler = new Scheduler(hw, memoryManager, 5); // Delta = 5 ciclos
        this.loadedPrograms = new HashMap<>();
        this.continuousExecution = false;
    }

    /**
     * Carrega um programa na memória e cria um processo
     *
     * Busca o programa no repositório e delega ao escalonador a criação
     * do processo (alocação de memória, criação do PCB, etc.)
     *
     * @param programName Nome do programa a ser carregado
     * @return true se o programa foi carregado com sucesso
     */
    public boolean loadProgram(String programName) {
        // Busca o programa no repositório
        Word[] program = programs.retrieveProgram(programName);
        if (program == null) {
            System.out.println("Erro: Programa " + programName + " não encontrado");
            return false;
        }

        // Delega ao escalonador a criação do processo
        boolean success = scheduler.addProcess(programName, program);
        if (success) {
            loadedPrograms.put(programName, program); // Cache para referência
        }
        return success;
    }

    /**
     * Executa todos os processos de forma escalonada (comando execAll)
     *
     * Implementa o requisito do comando execAll:
     * 1. Verifica se há processos carregados
     * 2. Inicia o escalonamento
     * 3. Executa processos em round-robin até todos terminarem
     * 4. Controla preempção por tempo e término de processos
     */
    public void execAll() {
        if (!scheduler.hasProcesses()) {
            System.out.println("Nenhum processo carregado para execução");
            return;
        }

        System.out.println("=== Iniciando execução escalonada de todos os processos ===");

        // Inicia o escalonamento com o primeiro processo
        scheduler.schedule();

        // Loop principal de execução escalonada
        while (scheduler.hasProcesses()) {
            executeCurrentProcess(); // Executa uma instrução do processo atual

            // Verifica condições para troca de contexto
            if (scheduler.shouldPreempt() || isCurrentProcessTerminated()) {
                scheduler.schedule(); // Escala próximo processo
            }

            // Pequena pausa para visualização do escalonamento
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("=== Execução escalonada finalizada ===");
    }

    /**
     * Inicia execução contínua em thread separada
     *
     * Implementa o requisito de funcionamento contínuo:
     * - Cria uma thread separada para o escalonamento
     * - Executa processos continuamente enquanto houver processos no sistema
     * - Permite que o usuário submeta comandos simultaneamente
     * - Thread daemon para não impedir o encerramento do programa
     */
    public void startContinuousExecution() {
        if (continuousExecution) {
            System.out.println("Execução contínua já está ativa");
            return;
        }

        continuousExecution = true;
        schedulerThread = new Thread(() -> {
            System.out.println("=== Iniciando execução contínua ===");

            // Loop principal da execução contínua
            while (continuousExecution) {
                if (scheduler.hasProcesses()) {
                    // Se não há processo atual, escala o primeiro da fila
                    if (scheduler.getCurrentProcess() == null) {
                        scheduler.schedule();
                    }

                    // Executa o processo atual se existe
                    if (scheduler.getCurrentProcess() != null) {
                        executeCurrentProcess();

                        // Verifica condições para troca de contexto
                        if (scheduler.shouldPreempt() || isCurrentProcessTerminated()) {
                            scheduler.schedule();
                        }
                    }
                }

                // Pequena pausa para não sobrecarregar o sistema
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("=== Execução contínua finalizada ===");
        });

        // Thread daemon para não impedir encerramento do programa
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }

    /**
     * Para a execução contínua
     *
     * Sinaliza para a thread de escalonamento parar e interrompe a thread
     */
    public void stopContinuousExecution() {
        continuousExecution = false;
        if (schedulerThread != null) {
            schedulerThread.interrupt();
        }
    }

    /**
     * Executa uma instrução do processo atual
     *
     * Método privado que simula a execução de uma instrução do processo atual.
     * Em uma implementação completa, chamaria o ciclo fetch-decode-execute da CPU.
     *
     * Responsabilidades:
     * - Traduz endereço lógico para físico
     * - Verifica se a instrução é STOP (término do processo)
     * - Simula execução incrementando PC
     * - Detecta fim do programa por overflow de PC
     * - Trata erros de execução
     */
    private void executeCurrentProcess() {
        PCB currentProcess = scheduler.getCurrentProcess();
        if (currentProcess == null) {
            return;
        }

        try {
            // Obtém o PC atual do processo
            int pc = hw.cpu.getPC();

            // Traduz endereço lógico para físico usando tabela de páginas
            int enderecoFisico = memoryManager.traduzEndereco(pc, currentProcess.tabelaPaginas);

            // Verifica se o endereço físico é válido
            if (enderecoFisico < hw.mem.pos.length) {
                Word instruction = hw.mem.pos[enderecoFisico];

                // Verifica se é instrução STOP (fim do processo)
                if (instruction.opc == Opcode.STOP) {
                    scheduler.terminateCurrentProcess();
                    System.out.println("Processo " + currentProcess.processId + " executou STOP");
                    return;
                }
            }

            // Simula execução da instrução incrementando PC
            // Em implementação completa, aqui seria o ciclo da CPU
            hw.cpu.setPC(pc + 1);

            // Verifica se ultrapassou o tamanho do programa
            if (pc >= currentProcess.tamanhoPrograma) {
                scheduler.terminateCurrentProcess();
                System.out.println("Processo " + currentProcess.processId + " chegou ao fim");
            }

        } catch (Exception e) {
            // Trata erros de execução (endereço inválido, etc.)
            System.out.println("Erro na execução do processo " + currentProcess.processId + ": " + e.getMessage());
            scheduler.terminateCurrentProcess();
        }
    }

    /**
     * Verifica se o processo atual terminou
     *
     * @return true se o processo atual está no estado TERMINATED
     */
    private boolean isCurrentProcessTerminated() {
        PCB currentProcess = scheduler.getCurrentProcess();
        return currentProcess != null && currentProcess.state == ProcessState.TERMINATED;
    }

    /**
     * Mostra status completo do gerenciador de processos
     *
     * Exibe informações sobre:
     * - Estado do escalonador
     * - Status da execução contínua
     * - Programas carregados no sistema
     */
    public void showStatus() {
        scheduler.printStatus();
        System.out.println("Execução contínua: " + (continuousExecution ? "ATIVA" : "INATIVA"));
        System.out.println("Programas carregados: " + loadedPrograms.keySet());
    }

    /**
     * Retorna referência ao escalonador
     *
     * @return Instância do escalonador para acesso direto
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
}