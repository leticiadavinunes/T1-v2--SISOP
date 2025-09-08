import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Gerente de Processos (GP)
 * 
 * Módulo do SO responsável por:
 * - Criar processos dado um programa como parâmetro
 * - Desalocar processos
 * - Gerenciar lista de processos no sistema
 * - Fornecer interface de comandos interativa
 */
public class GP {
    // === COMPONENTES DO SISTEMA ===
    private HW hw; // Hardware (CPU + Memória)
    private MemoryManager memoryManager; // Gerente de memória
    private Programs programs; // Repositório de programas
    private Utilities utilities; // Utilitários para dump

    // === ESTRUTURAS DE DADOS ===
    private Map<Integer, PCB> processos; // Mapa de todos os processos (ID -> PCB)
    private LinkedList<PCB> filaReady; // Fila de processos prontos
    private PCB processoRodando; // Processo atualmente em execução

    // === CONTROLE DE PROCESSOS ===
    private int proximoId; // Próximo ID de processo a ser atribuído
    private boolean traceMode; // Modo de trace (debug) ativo

    /**
     * Construtor do Gerente de Processos
     * 
     * @param hw            Hardware do sistema
     * @param memoryManager Gerente de memória
     * @param programs      Repositório de programas
     * @param utilities     Utilitários do sistema
     */
    public GP(HW hw, MemoryManager memoryManager, Programs programs, Utilities utilities) {
        this.hw = hw;
        this.memoryManager = memoryManager;
        this.programs = programs;
        this.utilities = utilities;

        // Inicializa estruturas de dados
        this.processos = new HashMap<>();
        this.filaReady = new LinkedList<>();
        this.processoRodando = null;
        this.proximoId = 1;
        this.traceMode = false;

        System.out.println("Gerente de Processos inicializado");
    }

    /**
     * Cria um processo dado um programa como parâmetro
     * 
     * Algoritmo:
     * 1. Verifica tamanho do programa
     * 2. Pede alocação de memória ao Gerente de Memória
     * 3. Se não tem memória, retorna false
     * 4. Cria PCB
     * 5. Seta partição usada no PCB
     * 6. Carrega o programa na memória
     * 7. Seta demais parâmetros do PCB (id, pc=0, etc)
     * 8. Coloca PCB na fila de prontos
     * 9. Retorna true
     * 
     * @param programa Array de instruções do programa
     * @return true se processo foi criado com sucesso, false caso contrário
     */
    public boolean criaProcesso(Word[] programa) {
        if (programa == null || programa.length == 0) {
            System.out.println("Erro: Programa inválido");
            return false;
        }

        // 1. Verifica tamanho do programa
        int tamanhoPrograma = programa.length;
        System.out.println("Criando processo - Tamanho do programa: " + tamanhoPrograma + " palavras");

        // 2. Calcula número de páginas necessárias
        int numPaginas = (int) Math.ceil((double) tamanhoPrograma / hw.mem.tamPg);
        int[] tabelaPaginas = new int[numPaginas];

        // 3. Pede alocação de memória ao Gerente de Memória
        if (!memoryManager.aloca(tamanhoPrograma, tabelaPaginas)) {
            System.out.println("Erro: Não foi possível alocar memória para o processo");
            return false; // Se não tem memória, retorna false
        }

        // 4. Cria PCB com ID único
        int processId = proximoId++;
        String nomePrograma = "processo_" + processId;
        PCB pcb = new PCB(processId, nomePrograma, tabelaPaginas, tamanhoPrograma);

        // 5. Seta partição usada no PCB (já feito no construtor do PCB)
        System.out.println("PCB criado - ID: " + processId + ", Páginas alocadas: " + numPaginas);

        // 6. Carrega o programa na memória usando paginação
        hw.mem.carregarProgramaPaginado(programa, tabelaPaginas);
        System.out.println("Programa carregado na memória");

        // 7. Seta demais parâmetros do PCB (id, pc=0, etc) - já feito no construtor
        pcb.pc = 0; // Program Counter inicia em 0
        pcb.state = ProcessState.READY; // Estado inicial: pronto

        // 8. Coloca PCB na fila de prontos
        filaReady.offer(pcb);
        processos.put(processId, pcb); // Adiciona ao mapa de processos

        System.out.println("Processo " + processId + " criado e adicionado à fila de prontos");

        // 9. Retorna true indicando sucesso
        return true;
    }

    /**
     * Cria um processo a partir do nome de um programa no repositório
     * 
     * @param nomePrograma Nome do programa no repositório
     * @return ID do processo criado ou -1 se falhou
     */
    public int criaProcesso(String nomePrograma) {
        // Busca o programa no repositório
        Word[] programa = programs.retrieveProgram(nomePrograma);
        if (programa == null) {
            System.out.println("Erro: Programa '" + nomePrograma + "' não encontrado");
            return -1;
        }

        // Guarda o ID antes de criar o processo
        int idAnterior = proximoId;

        // Cria o processo
        if (criaProcesso(programa)) {
            // Atualiza o nome do programa no PCB
            PCB pcb = processos.get(idAnterior);
            if (pcb != null) {
                pcb.programName = nomePrograma;
            }
            return idAnterior;
        }

        return -1; // Falhou na criação
    }

    /**
     * Desaloca um processo do sistema
     * 
     * Algoritmo:
     * 1. Desaloca toda memória do processo com id
     * 2. Retira de qualquer fila que esteja
     * 3. Desaloca PCB
     * 
     * @param id ID do processo a ser desalocado
     * @return true se processo foi desalocado com sucesso
     */
    public boolean desalocaProcesso(int id) {
        // Verifica se o processo existe
        PCB pcb = processos.get(id);
        if (pcb == null) {
            System.out.println("Erro: Processo " + id + " não encontrado");
            return false;
        }

        System.out.println("Desalocando processo " + id + " (" + pcb.programName + ")");

        // 1. Desaloca toda memória do processo
        if (pcb.tabelaPaginas != null) {
            memoryManager.desaloca(pcb.tabelaPaginas);
            System.out.println("Memória do processo " + id + " liberada");
        }

        // 2. Retira de qualquer fila que esteja
        if (processoRodando != null && processoRodando.processId == id) {
            processoRodando = null; // Remove da execução
            System.out.println("Processo " + id + " removido da execução");
        }

        // Remove da fila de prontos se estiver lá
        filaReady.removeIf(p -> p.processId == id);

        // 3. Desaloca PCB - remove do mapa de processos
        processos.remove(id);

        System.out.println("Processo " + id + " completamente desalocado");
        return true;
    }

    /**
     * Lista todos os processos existentes no sistema
     */
    public void listarProcessos() {
        System.out.println("=== LISTA DE PROCESSOS ===");

        if (processos.isEmpty()) {
            System.out.println("Nenhum processo no sistema");
            return;
        }

        System.out.printf("%-4s %-15s %-10s %-6s %-10s%n",
                "ID", "NOME", "ESTADO", "PC", "PÁGINAS");
        System.out.println("------------------------------------------------");

        for (PCB pcb : processos.values()) {
            String estado = pcb.state.toString();
            if (processoRodando != null && processoRodando.processId == pcb.processId) {
                estado = "RUNNING";
            }

            System.out.printf("%-4d %-15s %-10s %-6d %-10d%n",
                    pcb.processId,
                    pcb.programName,
                    estado,
                    pcb.pc,
                    pcb.tabelaPaginas.length);
        }

        System.out.println("Total de processos: " + processos.size());
    }

    /**
     * Faz dump do PCB e conteúdo da memória de um processo
     * 
     * @param id ID do processo
     */
    public void dumpProcesso(int id) {
        PCB pcb = processos.get(id);
        if (pcb == null) {
            System.out.println("Erro: Processo " + id + " não encontrado");
            return;
        }

        System.out.println("=== DUMP DO PROCESSO " + id + " ===");

        // Dump do PCB
        System.out.println("PCB:");
        System.out.println("  ID: " + pcb.processId);
        System.out.println("  Nome: " + pcb.programName);
        System.out.println("  Estado: " + pcb.state);
        System.out.println("  PC: " + pcb.pc);
        System.out.println("  Tamanho: " + pcb.tamanhoPrograma + " palavras");

        // Dump dos registradores
        System.out.println("  Registradores:");
        for (int i = 0; i < pcb.registers.length; i++) {
            System.out.println("    R" + i + ": " + pcb.registers[i]);
        }

        // Dump da tabela de páginas
        System.out.println("  Tabela de Páginas:");
        for (int i = 0; i < pcb.tabelaPaginas.length; i++) {
            System.out.println("    Página " + i + " -> Frame " + pcb.tabelaPaginas[i]);
        }

        // Dump do conteúdo da memória
        System.out.println("Conteúdo da Memória:");
        for (int pagina = 0; pagina < pcb.tabelaPaginas.length; pagina++) {
            int frame = pcb.tabelaPaginas[pagina];
            int inicioFrame = hw.mem.getFrameStart(frame);
            int fimFrame = Math.min(inicioFrame + hw.mem.tamPg,
                    inicioFrame + (pcb.tamanhoPrograma - pagina * hw.mem.tamPg));

            System.out.println("  Página " + pagina + " (Frame " + frame + "):");
            for (int i = inicioFrame; i < fimFrame; i++) {
                int enderecoLogico = (pagina * hw.mem.tamPg) + (i - inicioFrame);
                if (enderecoLogico < pcb.tamanhoPrograma) {
                    System.out.print("    [" + enderecoLogico + "]: ");
                    utilities.dump(hw.mem.pos[i]);
                }
            }
        }
    }

    /**
     * Faz dump da memória entre posições início e fim
     * 
     * @param inicio Posição inicial
     * @param fim    Posição final
     */
    public void dumpMemoria(int inicio, int fim) {
        System.out.println("=== DUMP DA MEMÓRIA [" + inicio + " - " + fim + "] ===");

        if (inicio < 0 || fim >= hw.mem.pos.length || inicio > fim) {
            System.out.println("Erro: Intervalo inválido");
            return;
        }

        utilities.dump(inicio, fim + 1);
    }

    /**
     * Executa um processo específico
     * 
     * @param id ID do processo a ser executado
     * @return true se execução foi iniciada com sucesso
     */
    public boolean executarProcesso(int id) {
        PCB pcb = processos.get(id);
        if (pcb == null) {
            System.out.println("Erro: Processo " + id + " não encontrado");
            return false;
        }

        if (pcb.state == ProcessState.TERMINATED) {
            System.out.println("Erro: Processo " + id + " já foi terminado");
            return false;
        }

        // Se há um processo rodando, para ele primeiro
        if (processoRodando != null) {
            processoRodando.saveContext(hw.cpu);
            processoRodando.state = ProcessState.READY;
            filaReady.offer(processoRodando);
        }

        // Coloca o processo solicitado em execução
        processoRodando = pcb;
        pcb.state = ProcessState.RUNNING;
        pcb.restoreContext(hw.cpu);

        System.out.println("Executando processo " + id + " (" + pcb.programName + ")");

        // Executa o processo
        try {
            executarCiclosCPU();
        } catch (Exception e) {
            System.out.println("Erro na execução: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Executa ciclos da CPU até o processo terminar ou ser interrompido
     */
    private void executarCiclosCPU() {
        if (processoRodando == null)
            return;

        hw.cpu.setContext(processoRodando.pc);

        while (processoRodando != null && processoRodando.state == ProcessState.RUNNING) {
            try {
                // Salva PC atual
                int pcAtual = hw.cpu.getPC();

                // Traduz endereço lógico para físico
                int enderecoFisico = memoryManager.traduzEndereco(pcAtual, processoRodando.tabelaPaginas);

                // Verifica se endereço é válido
                if (enderecoFisico >= hw.mem.pos.length) {
                    System.out.println("Erro: Endereço físico inválido - " + enderecoFisico);
                    break;
                }

                // Busca instrução
                Word instrucao = hw.mem.pos[enderecoFisico];

                // Modo trace - imprime instrução se ativo
                if (traceMode) {
                    System.out.print("PC=" + pcAtual + " [" + enderecoFisico + "]: ");
                    utilities.dump(instrucao);
                }

                // Verifica se é STOP
                if (instrucao.opc == Opcode.STOP) {
                    System.out.println("Processo " + processoRodando.processId + " executou STOP - terminando");
                    processoRodando.state = ProcessState.TERMINATED;
                    desalocaProcesso(processoRodando.processId);
                    processoRodando = null;
                    break;
                }

                // Simula execução da instrução
                hw.cpu.setPC(pcAtual + 1);

                // Verifica se ultrapassou o programa
                if (pcAtual >= processoRodando.tamanhoPrograma) {
                    System.out.println("Processo " + processoRodando.processId + " chegou ao fim");
                    processoRodando.state = ProcessState.TERMINATED;
                    desalocaProcesso(processoRodando.processId);
                    processoRodando = null;
                    break;
                }

                // Salva contexto atualizado
                processoRodando.saveContext(hw.cpu);

                // Pequena pausa para visualização
                Thread.sleep(100);

            } catch (Exception e) {
                System.out.println("Erro na execução: " + e.getMessage());
                if (processoRodando != null) {
                    desalocaProcesso(processoRodando.processId);
                    processoRodando = null;
                }
                break;
            }
        }
    }

    /**
     * Liga o modo trace (debug)
     */
    public void ligarTrace() {
        traceMode = true;
        System.out.println("Modo trace LIGADO - instruções serão impressas durante execução");
    }

    /**
     * Desliga o modo trace (debug)
     */
    public void desligarTrace() {
        traceMode = false;
        System.out.println("Modo trace DESLIGADO");
    }

    /**
     * Interface de linha de comando do sistema
     * Sistema iterativo que aceita comandos do usuário
     */
    public void interfaceComandos() {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        System.out.println("=== GERENTE DE PROCESSOS - INTERFACE DE COMANDOS ===");
        System.out.println("Digite 'help' para ver os comandos disponíveis");

        while (executando) {
            System.out.print("GP> ");
            String linha = scanner.nextLine().trim();

            if (linha.isEmpty())
                continue;

            String[] partes = linha.split("\\s+");
            String comando = partes[0].toLowerCase();

            try {
                switch (comando) {
                    case "new":
                        if (partes.length < 2) {
                            System.out.println("Uso: new <nomeDePrograma>");
                        } else {
                            int id = criaProcesso(partes[1]);
                            if (id > 0) {
                                System.out.println("Processo criado com ID: " + id);
                            }
                        }
                        break;

                    case "rm":
                        if (partes.length < 2) {
                            System.out.println("Uso: rm <id>");
                        } else {
                            try {
                                int id = Integer.parseInt(partes[1]);
                                desalocaProcesso(id);
                            } catch (NumberFormatException e) {
                                System.out.println("Erro: ID deve ser um número");
                            }
                        }
                        break;

                    case "ps":
                        listarProcessos();
                        break;

                    case "dump":
                        if (partes.length < 2) {
                            System.out.println("Uso: dump <id>");
                        } else {
                            try {
                                int id = Integer.parseInt(partes[1]);
                                dumpProcesso(id);
                            } catch (NumberFormatException e) {
                                System.out.println("Erro: ID deve ser um número");
                            }
                        }
                        break;

                    case "dumpm":
                        if (partes.length < 3) {
                            System.out.println("Uso: dumpM <inicio> <fim>");
                        } else {
                            try {
                                int inicio = Integer.parseInt(partes[1]);
                                int fim = Integer.parseInt(partes[2]);
                                dumpMemoria(inicio, fim);
                            } catch (NumberFormatException e) {
                                System.out.println("Erro: Início e fim devem ser números");
                            }
                        }
                        break;

                    case "exec":
                        if (partes.length < 2) {
                            System.out.println("Uso: exec <id>");
                        } else {
                            try {
                                int id = Integer.parseInt(partes[1]);
                                executarProcesso(id);
                            } catch (NumberFormatException e) {
                                System.out.println("Erro: ID deve ser um número");
                            }
                        }
                        break;

                    case "traceon":
                        ligarTrace();
                        break;

                    case "traceoff":
                        desligarTrace();
                        break;

                    case "help":
                        mostrarAjuda();
                        break;

                    case "exit":
                        executando = false;
                        System.out.println("Saindo do sistema...");
                        break;

                    default:
                        System.out.println("Comando não reconhecido: " + comando);
                        System.out.println("Digite 'help' para ver os comandos disponíveis");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Erro ao executar comando: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Mostra a ajuda com todos os comandos disponíveis
     */
    private void mostrarAjuda() {
        System.out.println("=== COMANDOS DISPONÍVEIS ===");
        System.out.println("new <nomeDePrograma>  - Cria um processo na memória");
        System.out.println("rm <id>               - Remove o processo com ID especificado");
        System.out.println("ps                    - Lista todos os processos existentes");
        System.out.println("dump <id>             - Mostra PCB e memória do processo");
        System.out.println("dumpM <inicio> <fim>  - Mostra memória entre posições");
        System.out.println("exec <id>             - Executa o processo especificado");
        System.out.println("traceOn               - Liga modo de execução com trace");
        System.out.println("traceOff              - Desliga modo trace");
        System.out.println("help                  - Mostra esta ajuda");
        System.out.println("exit                  - Sai do sistema");
        System.out.println("============================");
    }

    /**
     * Retorna o número de processos no sistema
     */
    public int getNumeroProcessos() {
        return processos.size();
    }

    /**
     * Retorna o processo atualmente em execução
     */
    public PCB getProcessoRodando() {
        return processoRodando;
    }
}