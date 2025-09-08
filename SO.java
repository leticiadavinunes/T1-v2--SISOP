import java.util.Scanner;

/**
 * Sistema Operacional com Gerente de Processos
 * 
 * Classe principal do SO que integra todos os componentes do sistema,
 * incluindo o Gerente de Processos (GP) que implementa os requisitos
 * especificados para criação, execução e gerenciamento de processos.
 */
public class SO {
    // === COMPONENTES DO SISTEMA ===
    public InterruptHandling ih; // Tratamento de interrupções
    public SysCallHandling sc; // Tratamento de syscalls
    public Utilities utils; // Utilitários do sistema
    public ProcessManager processManager; // Gerenciador de processos (antigo)
    public MemoryManager memoryManager; // Gerente de memória
    public Programs programs; // Repositório de programas
    public GP gp; // Gerente de Processos (GP) - novo
    private HW hw; // Hardware do sistema

    /**
     * Construtor do Sistema Operacional
     * 
     * @param hw Hardware do sistema
     */
    public SO(HW hw) {
        this.hw = hw;

        // === INICIALIZAÇÃO DOS COMPONENTES BÁSICOS ===
        ih = new InterruptHandling(hw);
        sc = new SysCallHandling(hw);
        hw.cpu.setAddressOfHandlers(ih, sc);
        utils = new Utilities(hw);

        // === INICIALIZAÇÃO DO GERENCIAMENTO DE MEMÓRIA ===
        memoryManager = new MemoryManager(hw.mem);

        // === INICIALIZAÇÃO DO REPOSITÓRIO DE PROGRAMAS ===
        programs = new Programs();
        criarProgramasDemo(); // Cria programas de demonstração

        // === INICIALIZAÇÃO DO GERENTE DE PROCESSOS (GP) ===
        gp = new GP(hw, memoryManager, programs, utils);

        // === INICIALIZAÇÃO DO PROCESS MANAGER (MANTIDO PARA COMPATIBILIDADE) ===
        processManager = new ProcessManager(hw, memoryManager, programs);

        // === CONECTA OS HANDLERS ===
        ih.setProcessManager(processManager);
        sc.setProcessManager(processManager);

        System.out.println("Sistema Operacional inicializado com Gerente de Processos (GP)");
        mostrarInformacoesSistema();
    }

    /**
     * Cria programas de demonstração no repositório
     */
    private void criarProgramasDemo() {

        // === PROGRAMA 1: HELLO WORLD ===
        Word[] progHello = {
                new Word(Opcode.LDI, 8, -1, 2), // R8 = 2 (syscall OUT)
                new Word(Opcode.LDI, 9, -1, 10), // R9 = 10 (endereço)
                new Word(Opcode.LDI, 0, -1, 72), // R0 = 72 ('H')
                new Word(Opcode.STD, 0, -1, 10), // MEM[10] = 'H'
                new Word(Opcode.SYSCALL, -1, -1, -1), // Imprime 'H'
                new Word(Opcode.STOP, -1, -1, -1) // Para
        };

        // === PROGRAMA 2: CONTADOR ===
        Word[] progContador = {
                new Word(Opcode.LDI, 0, -1, 1), // R0 = 1
                new Word(Opcode.LDI, 1, -1, 5), // R1 = 5 (limite)
                new Word(Opcode.LDI, 8, -1, 2), // R8 = 2 (syscall OUT)
                new Word(Opcode.LDI, 9, -1, 20), // R9 = 20 (endereço)
                new Word(Opcode.STD, 0, -1, 20), // MEM[20] = contador
                new Word(Opcode.SYSCALL, -1, -1, -1), // Imprime contador
                new Word(Opcode.ADDI, 0, -1, 1), // R0++
                new Word(Opcode.SUB, 2, 1, 0), // R2 = R1 - R0
                new Word(Opcode.JMPIG, 2, -1, 4), // Se R2 > 0, volta
                new Word(Opcode.STOP, -1, -1, -1) // Para
        };

        // === PROGRAMA 3: FIBONACCI ===
        Word[] progFib = {
                new Word(Opcode.LDI, 0, -1, 0), // R0 = 0 (fib n-2)
                new Word(Opcode.LDI, 1, -1, 1), // R1 = 1 (fib n-1)
                new Word(Opcode.LDI, 3, -1, 5), // R3 = 5 (contador)
                new Word(Opcode.ADD, 2, 0, 1), // R2 = R0 + R1 (próximo fib)
                new Word(Opcode.MOVE, 0, -1, 1), // R0 = R1
                new Word(Opcode.MOVE, 1, -1, 2), // R1 = R2
                new Word(Opcode.STD, 2, -1, 30), // MEM[30] = fib atual
                new Word(Opcode.SUBI, 3, -1, 1), // R3--
                new Word(Opcode.JMPIG, 3, -1, 3), // Se R3 > 0, continua
                new Word(Opcode.STOP, -1, -1, -1) // Para
        };

        // === PROGRAMA 4: MATEMÁTICA ===
        Word[] progMath = {
                new Word(Opcode.LDI, 0, -1, 15), // R0 = 15
                new Word(Opcode.LDI, 1, -1, 7), // R1 = 7
                new Word(Opcode.ADD, 2, 0, 1), // R2 = 15 + 7 = 22
                new Word(Opcode.SUB, 3, 0, 1), // R3 = 15 - 7 = 8
                new Word(Opcode.MULT, 4, 1, 1), // R4 = 7 * 7 = 49
                new Word(Opcode.STD, 2, -1, 40), // MEM[40] = soma
                new Word(Opcode.STD, 3, -1, 41), // MEM[41] = subtração
                new Word(Opcode.STD, 4, -1, 42), // MEM[42] = multiplicação
                new Word(Opcode.STOP, -1, -1, -1) // Para
        };

        // === PROGRAMA 5: LOOP SIMPLES ===
        Word[] progLoop = {
                new Word(Opcode.LDI, 0, -1, 0), // R0 = 0
                new Word(Opcode.LDI, 1, -1, 3), // R1 = 3 (limite)
                new Word(Opcode.ADDI, 0, -1, 1), // R0++ (linha 2)
                new Word(Opcode.STD, 0, -1, 50), // MEM[50] = contador
                new Word(Opcode.SUB, 2, 1, 0), // R2 = R1 - R0
                new Word(Opcode.JMPIG, 2, -1, 2), // Se R2 > 0, volta
                new Word(Opcode.STOP, -1, -1, -1) // Para
        };

        // === CRIAÇÃO DO REPOSITÓRIO ===
        Program[] progs = {
                new Program("hello", progHello),
                new Program("contador", progContador),
                new Program("fibonacci", progFib),
                new Program("matematica", progMath),
                new Program("loop", progLoop)
        };

        programs.progs = progs;
    }

    /**
     * Mostra informações básicas do sistema
     */
    private void mostrarInformacoesSistema() {
        System.out.println("=== INFORMAÇÕES DO SISTEMA ===");
        System.out.println("Memória total: " + hw.mem.pos.length + " palavras");
        System.out.println("Tamanho da página: " + hw.mem.tamPg + " palavras");
        System.out.println("Número de frames: " + hw.mem.numFrames);
        System.out.println("Programas disponíveis: " + programs.progs.length);
        for (Program p : programs.progs) {
            System.out.println("  - " + p.name + " (" + p.image.length + " instruções)");
        }
        System.out.println("==============================\n");
    }

    // === MÉTODOS DO PROCESSMANAGER (MANTIDOS PARA COMPATIBILIDADE) ===

    /**
     * Comando para carregar um programa (ProcessManager)
     */
    public boolean loadProgram(String programName) {
        return processManager.loadProgram(programName);
    }

    /**
     * Comando execAll - executa todos os processos de forma escalonada
     * (ProcessManager)
     */
    public void execAll() {
        processManager.execAll();
    }

    /**
     * Inicia execução contínua (ProcessManager)
     */
    public void startContinuousExecution() {
        processManager.startContinuousExecution();
    }

    /**
     * Para execução contínua (ProcessManager)
     */
    public void stopContinuousExecution() {
        processManager.stopContinuousExecution();
    }

    // === MÉTODOS DO GERENTE DE PROCESSOS (GP) - NOVOS ===

    /**
     * Cria um processo usando o GP
     * 
     * @param nomePrograma Nome do programa
     * @return ID do processo criado ou -1 se falhou
     */
    public int criarProcesso(String nomePrograma) {
        return gp.criaProcesso(nomePrograma);
    }

    /**
     * Remove um processo usando o GP
     * 
     * @param id ID do processo
     * @return true se removido com sucesso
     */
    public boolean removerProcesso(int id) {
        return gp.desalocaProcesso(id);
    }

    /**
     * Lista todos os processos usando o GP
     */
    public void listarProcessos() {
        gp.listarProcessos();
    }

    /**
     * Executa um processo específico usando o GP
     * 
     * @param id ID do processo
     * @return true se execução foi iniciada
     */
    public boolean executarProcesso(int id) {
        return gp.executarProcesso(id);
    }

    /**
     * Faz dump de um processo usando o GP
     * 
     * @param id ID do processo
     */
    public void dumpProcesso(int id) {
        gp.dumpProcesso(id);
    }

    /**
     * Faz dump da memória usando o GP
     * 
     * @param inicio Posição inicial
     * @param fim    Posição final
     */
    public void dumpMemoria(int inicio, int fim) {
        gp.dumpMemoria(inicio, fim);
    }

    /**
     * Liga o modo trace usando o GP
     */
    public void ligarTrace() {
        gp.ligarTrace();
    }

    /**
     * Desliga o modo trace usando o GP
     */
    public void desligarTrace() {
        gp.desligarTrace();
    }

    // === INTERFACE DE COMANDOS DO SISTEMA ===

    /**
     * Interface de linha de comando do Sistema Operacional
     * 
     * Sistema iterativo que aceita comandos conforme especificado:
     * - new <nomeDePrograma>: cria um processo
     * - rm <id>: remove um processo
     * - ps: lista processos
     * - dump <id>: dump do PCB e memória do processo
     * - dumpM <inicio> <fim>: dump da memória
     * - exec <id>: executa um processo
     * - traceOn/traceOff: liga/desliga modo trace
     * - exit: sai do sistema
     */
    public void interfaceComandos() {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        System.out.println("=== SISTEMA OPERACIONAL - INTERFACE DE COMANDOS ===");
        System.out.println("Digite 'help' para ver os comandos disponíveis\n");

        while (executando) {
            System.out.print("SO> ");
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
                            System.out.println("Programas disponíveis: hello, contador, fibonacci, matematica, loop");
                        } else {
                            int id = criarProcesso(partes[1]);
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
                                if (removerProcesso(id)) {
                                    System.out.println("Processo " + id + " removido com sucesso");
                                }
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
                                if (executarProcesso(id)) {
                                    System.out.println("Execução do processo " + id + " concluída");
                                }
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

                    case "status":
                        showSystemStatus();
                        break;

                    case "exit":
                        executando = false;
                        System.out.println("Saindo do sistema...");
                        break;

                    // === COMANDOS DO PROCESSMANAGER (COMPATIBILIDADE) ===
                    case "load":
                        if (partes.length < 2) {
                            System.out.println("Uso: load <nomePrograma>");
                        } else {
                            if (loadProgram(partes[1])) {
                                System.out.println("Programa " + partes[1] + " carregado no ProcessManager");
                            }
                        }
                        break;

                    case "execall":
                        System.out.println("Executando todos os processos (ProcessManager)...");
                        execAll();
                        break;

                    case "startcontinuous":
                        System.out.println("Iniciando execução contínua (ProcessManager)...");
                        startContinuousExecution();
                        break;

                    case "stopcontinuous":
                        System.out.println("Parando execução contínua (ProcessManager)...");
                        stopContinuousExecution();
                        break;

                    default:
                        System.out.println("Comando não reconhecido: " + comando);
                        System.out.println("Digite 'help' para ver os comandos disponíveis");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Erro ao executar comando: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    /**
     * Mostra a ajuda com todos os comandos disponíveis
     */
    private void mostrarAjuda() {
        System.out.println("=== COMANDOS DO GERENTE DE PROCESSOS (GP) ===");
        System.out.println("new <nomeDePrograma>  - Cria um processo na memória");
        System.out.println("rm <id>               - Remove o processo com ID especificado");
        System.out.println("ps                    - Lista todos os processos existentes");
        System.out.println("dump <id>             - Mostra PCB e memória do processo");
        System.out.println("dumpM <inicio> <fim>  - Mostra memória entre posições");
        System.out.println("exec <id>             - Executa o processo especificado");
        System.out.println("traceOn               - Liga modo de execução com trace");
        System.out.println("traceOff              - Desliga modo trace");
        System.out.println();
        System.out.println("=== COMANDOS DO PROCESSMANAGER (COMPATIBILIDADE) ===");
        System.out.println("load <nomePrograma>   - Carrega programa no ProcessManager");
        System.out.println("execAll               - Executa todos os processos escalonados");
        System.out.println("startContinuous       - Inicia execução contínua");
        System.out.println("stopContinuous        - Para execução contínua");
        System.out.println();
        System.out.println("=== COMANDOS GERAIS ===");
        System.out.println("status                - Mostra status do sistema");
        System.out.println("help                  - Mostra esta ajuda");
        System.out.println("exit                  - Sai do sistema");
        System.out.println();
        System.out.println("=== PROGRAMAS DISPONÍVEIS ===");
        for (Program p : programs.progs) {
            System.out.println("  - " + p.name + " (" + p.image.length + " instruções)");
        }
        System.out.println("=============================================");
    }

    /**
     * Mostra status do sistema
     */
    public void showSystemStatus() {
        System.out.println("=== STATUS DO SISTEMA OPERACIONAL ===");

        // Status do GP
        System.out.println("Gerente de Processos (GP):");
        System.out.println("  Processos ativos: " + gp.getNumeroProcessos());
        PCB processoRodando = gp.getProcessoRodando();
        System.out.println("  Processo em execução: " +
                (processoRodando != null ? processoRodando.processId : "Nenhum"));

        // Status da memória
        System.out.println("\nStatus da Memória:");
        int framesLivres = 0;
        for (boolean livre : hw.mem.frameLivre) {
            if (livre)
                framesLivres++;
        }
        System.out.println("  Frames livres: " + framesLivres + "/" + hw.mem.numFrames);
        System.out.println("  Tamanho da página: " + hw.mem.tamPg + " palavras");
        System.out.println("  Memória total: " + hw.mem.pos.length + " palavras");

        // Status do ProcessManager
        System.out.println("\nProcessManager (compatibilidade):");
        processManager.showStatus();

        System.out.println("=====================================");
    }

    /**
     * Interface de linha de comando simples (método antigo mantido para
     * compatibilidade)
     */
    public void commandInterface() {
        System.out.println("=== Interface do Sistema Operacional (Modo Compatibilidade) ===");
        System.out.println("Comandos disponíveis:");
        System.out.println("- loadProgram <nome>: Carrega um programa");
        System.out.println("- execAll: Executa todos os processos escalonados");
        System.out.println("- startContinuous: Inicia execução contínua");
        System.out.println("- stopContinuous: Para execução contínua");
        System.out.println("- status: Mostra status do sistema");
        System.out.println("- help: Mostra esta ajuda");
        System.out.println();
        System.out.println("RECOMENDAÇÃO: Use interfaceComandos() para acesso completo ao GP");
        System.out.println("================================================================");
    }
}
