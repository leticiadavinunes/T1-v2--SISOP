import java.util.Scanner;

/**
 * Teste Interativo do Gerente de Processos (GP)
 * 
 * Demonstra as funcionalidades do Gerente de Processos:
 * - Criacao de processos (criaProcesso)
 * - Desalocacao de processos (desalocaProcesso)
 * - Comandos interativos (new, rm, ps, dump, dumpM, exec, trace)
 * - Estruturas PCB, fila ready, processo running
 */
public class TesteGP {

        private static Scanner scanner = new Scanner(System.in);
        private static HW hw;
        private static MemoryManager memoryManager;
        private static Programs programs;
        private static Utilities utilities;
        private static GP gp;

        public static void main(String[] args) {
                System.out.println("========================================");
                System.out.println("    GERENTE DE PROCESSOS (GP)");
                System.out.println("    Demonstracao Interativa");
                System.out.println("========================================");
                System.out.println();

                // Inicialização
                inicializarSistema();

                // Menu principal
                menuPrincipal();

                System.out.println("\nDemonstracao do Gerente de Processos finalizada!");
                scanner.close();
        }

        /**
         * Inicializa o sistema do GP
         */
        private static void inicializarSistema() {
                System.out.println("INICIALIZACAO DO GERENTE DE PROCESSOS");
                System.out.println("=====================================");
                System.out.print("Pressione ENTER para inicializar o GP...");
                scanner.nextLine();

                System.out.println("\nCriando hardware (1024 palavras, paginas de 16)...");
                hw = new HW(1024, 16);

                System.out.println("Inicializando Gerente de Memoria...");
                memoryManager = new MemoryManager(hw.mem);

                System.out.println("Criando repositorio de programas...");
                programs = new Programs();
                utilities = new Utilities(hw);

                System.out.println("Criando programas de teste...");
                criarProgramasTeste();

                System.out.println("Inicializando Gerente de Processos (GP)...");
                gp = new GP(hw, memoryManager, programs, utilities);

                System.out.println("\nGerente de Processos inicializado!");
                System.out.println("Caracteristicas do GP:");
                System.out.println("- Funcao criaProcesso() implementada");
                System.out.println("- Funcao desalocaProcesso() implementada");
                System.out.println("- Estruturas: PCB, fila ready, processo running");
                System.out.println("- Comandos: new, rm, ps, dump, dumpM, exec, trace");

                pausar();
        }

        /**
         * Cria programas de teste para o GP
         */
        private static void criarProgramasTeste() {

                // PROGRAMA 1: MATEMATICA BASICA
                Word[] progMatematica = {
                                new Word(Opcode.LDI, 0, -1, 10), // R0 = 10
                                new Word(Opcode.LDI, 1, -1, 5), // R1 = 5
                                new Word(Opcode.ADD, 2, 0, 1), // R2 = R0 + R1 = 15
                                new Word(Opcode.MULT, 3, 2, 1), // R3 = R2 * R1 = 75
                                new Word(Opcode.STD, 3, -1, 100), // MEM[100] = 75
                                new Word(Opcode.STOP, -1, -1, -1) // Para o processo
                };

                // PROGRAMA 2: LOOP SIMPLES
                Word[] progLoop = {
                                new Word(Opcode.LDI, 0, -1, 0), // R0 = 0 (contador)
                                new Word(Opcode.LDI, 1, -1, 3), // R1 = 3 (limite)
                                new Word(Opcode.ADDI, 0, -1, 1), // R0++ (linha 2)
                                new Word(Opcode.SUB, 2, 1, 0), // R2 = R1 - R0
                                new Word(Opcode.JMPIG, 2, -1, 2), // Se R2 > 0, volta para linha 2
                                new Word(Opcode.STD, 0, -1, 101), // MEM[101] = contador final
                                new Word(Opcode.STOP, -1, -1, -1) // Para o processo
                };

                // PROGRAMA 3: OPERACOES DE MEMORIA
                Word[] progMemoria = {
                                new Word(Opcode.LDI, 0, -1, 42), // R0 = 42
                                new Word(Opcode.STD, 0, -1, 102), // MEM[102] = 42
                                new Word(Opcode.LDD, 1, -1, 102), // R1 = MEM[102]
                                new Word(Opcode.ADDI, 1, -1, 8), // R1 = R1 + 8 = 50
                                new Word(Opcode.STD, 1, -1, 103), // MEM[103] = 50
                                new Word(Opcode.STOP, -1, -1, -1) // Para o processo
                };

                // PROGRAMA 4: FIBONACCI
                Word[] progFibonacci = {
                                new Word(Opcode.LDI, 0, -1, 1), // R0 = 1 (fib anterior)
                                new Word(Opcode.LDI, 1, -1, 1), // R1 = 1 (fib atual)
                                new Word(Opcode.ADD, 2, 0, 1), // R2 = R0 + R1 (proximo fib)
                                new Word(Opcode.MOVE, 0, -1, 1), // R0 = R1 (atualiza anterior)
                                new Word(Opcode.MOVE, 1, -1, 2), // R1 = R2 (atualiza atual)
                                new Word(Opcode.STD, 2, -1, 104), // MEM[104] = fibonacci atual
                                new Word(Opcode.SUBI, 2, -1, 20), // R2 = R2 - 20 (testa parada)
                                new Word(Opcode.JMPIL, 2, -1, 2), // Se R2 < 0, continua loop
                                new Word(Opcode.STOP, -1, -1, -1) // Termina quando fib >= 20
                };

                // PROGRAMA 5: PROGRAMA SIMPLES
                Word[] progSimples = {
                                new Word(Opcode.LDI, 0, -1, 99), // R0 = 99
                                new Word(Opcode.STD, 0, -1, 105), // MEM[105] = 99
                                new Word(Opcode.STOP, -1, -1, -1) // Para imediatamente
                };

                // CRIACAO DO REPOSITORIO DE PROGRAMAS
                Program[] progs = {
                                new Program("programa_matematica", progMatematica),
                                new Program("programa_loop", progLoop),
                                new Program("programa_memoria", progMemoria),
                                new Program("programa_fibonacci", progFibonacci),
                                new Program("programa_simples", progSimples)
                };

                programs.progs = progs;
        }

        /**
         * Menu principal da demonstração
         */
        private static void menuPrincipal() {
                boolean continuar = true;

                while (continuar) {
                        System.out.println("\n========================================");
                        System.out.println("    MENU - GERENTE DE PROCESSOS");
                        System.out.println("========================================");
                        System.out.println();
                        System.out.println("1. Demonstrar funcao criaProcesso()");
                        System.out.println("2. Demonstrar funcao desalocaProcesso()");
                        System.out.println("3. Testar comando 'new'");
                        System.out.println("4. Testar comando 'ps'");
                        System.out.println("5. Testar comando 'dump'");
                        System.out.println("6. Testar comando 'dumpM'");
                        System.out.println("7. Testar comando 'exec'");
                        System.out.println("8. Testar comandos 'traceOn/traceOff'");
                        System.out.println("9. Ver estruturas (PCB, fila, running)");
                        System.out.println("A. Interface completa de comandos");
                        System.out.println("B. Demonstracao completa automatica");
                        System.out.println("0. Voltar");
                        System.out.println();
                        System.out.print("Escolha uma opcao: ");

                        String opcao = scanner.nextLine().trim().toUpperCase();

                        switch (opcao) {
                                case "1":
                                        demonstrarCriaProcesso();
                                        break;
                                case "2":
                                        demonstrarDesalocaProcesso();
                                        break;
                                case "3":
                                        demonstrarComandoNew();
                                        break;
                                case "4":
                                        demonstrarComandoPs();
                                        break;
                                case "5":
                                        demonstrarComandoDump();
                                        break;
                                case "6":
                                        demonstrarComandoDumpM();
                                        break;
                                case "7":
                                        demonstrarComandoExec();
                                        break;
                                case "8":
                                        demonstrarComandosTrace();
                                        break;
                                case "9":
                                        demonstrarEstruturas();
                                        break;
                                case "A":
                                        interfaceCompleta();
                                        break;
                                case "B":
                                        demonstracaoCompleta();
                                        break;
                                case "0":
                                        continuar = false;
                                        break;
                                default:
                                        System.out.println("Opcao invalida!");
                                        pausar();
                                        break;
                        }
                }
        }

        /**
         * Demonstra a função criaProcesso()
         */
        private static void demonstrarCriaProcesso() {
                System.out.println("\n========================================");
                System.out.println("    FUNCAO criaProcesso()");
                System.out.println("========================================");
                System.out.println();

                System.out.println("A funcao criaProcesso() implementa o algoritmo especificado:");
                System.out.println("1. Verifica tamanho do programa");
                System.out.println("2. Pede alocacao de memoria ao Gerente de Memoria");
                System.out.println("3. Se nao tem memoria, retorna false");
                System.out.println("4. Cria PCB (Process Control Block)");
                System.out.println("5. Seta particao usada no PCB");
                System.out.println("6. Carrega o programa na memoria");
                System.out.println("7. Seta demais parametros do PCB (id, pc=0, etc)");
                System.out.println("8. Coloca PCB na fila de prontos");
                System.out.println("9. Retorna true");
                System.out.println();

                pausar();

                System.out.println("Programas disponiveis para criacao:");
                for (Program p : programs.progs) {
                        System.out.println("- " + p.name + " (" + p.image.length + " instrucoes)");
                }
                System.out.println();

                System.out.print("Escolha um programa (ou ENTER para programa_matematica): ");
                String programa = scanner.nextLine().trim();
                if (programa.isEmpty()) {
                        programa = "programa_matematica";
                }

                System.out.println("\nExecutando: criaProcesso(\"" + programa + "\")");
                System.out.println("=======================================");

                int id = gp.criaProcesso(programa);

                if (id > 0) {
                        System.out.println("Processo criado com sucesso! ID: " + id);
                        System.out.println("\nStatus apos criacao:");
                        System.out.println("- Numero de processos: " + gp.getNumeroProcessos());
                        System.out.println("- Processo na fila de prontos");
                        System.out.println("- Memoria alocada e programa carregado");
                } else {
                        System.out.println("Falha na criacao - Memoria insuficiente");
                }

                pausar();
        }

        /**
         * Demonstra a função desalocaProcesso()
         */
        private static void demonstrarDesalocaProcesso() {
                System.out.println("\n========================================");
                System.out.println("    FUNCAO desalocaProcesso()");
                System.out.println("========================================");
                System.out.println();

                System.out.println("A funcao desalocaProcesso() implementa:");
                System.out.println("1. Desaloca toda memoria do processo com id");
                System.out.println("2. Retira de qualquer fila que esteja");
                System.out.println("3. Desaloca PCB");
                System.out.println();

                // Primeiro criar alguns processos se necessário
                if (gp.getNumeroProcessos() == 0) {
                        System.out.println("Criando processos para demonstracao...");
                        gp.criaProcesso("programa_matematica");
                        gp.criaProcesso("programa_simples");
                        gp.criaProcesso("programa_loop");
                }

                System.out.println("Processos ANTES da desalocacao:");
                gp.listarProcessos();
                System.out.println();

                System.out.print("Digite o ID do processo para desalocar (ou ENTER para ID 2): ");
                String input = scanner.nextLine().trim();
                int id = input.isEmpty() ? 2 : Integer.parseInt(input);

                pausar();

                System.out.println("Executando: desalocaProcesso(" + id + ")");
                System.out.println("=======================================");

                boolean sucesso = gp.desalocaProcesso(id);

                if (sucesso) {
                        System.out.println("Processo desalocado com sucesso!");
                } else {
                        System.out.println("Processo nao encontrado!");
                }

                System.out.println("\nProcessos APOS a desalocacao:");
                gp.listarProcessos();

                pausar();
        }

        /**
         * Demonstra o comando 'new'
         */
        private static void demonstrarComandoNew() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO 'new'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando 'new <nomeDePrograma>' cria um processo:");
                System.out.println("- Busca o programa no repositorio");
                System.out.println("- Chama criaProcesso() internamente");
                System.out.println("- Retorna ID unico do processo");
                System.out.println("- Adiciona a fila de prontos");
                System.out.println();

                System.out.println("Programas disponiveis:");
                for (Program p : programs.progs) {
                        System.out.println("- " + p.name);
                }
                System.out.println();

                System.out.print("Digite o nome do programa (ou ENTER para programa_fibonacci): ");
                String programa = scanner.nextLine().trim();
                if (programa.isEmpty()) {
                        programa = "programa_fibonacci";
                }

                System.out.println("\nExecutando comando: new " + programa);
                System.out.println("=======================================");

                int id = gp.criaProcesso(programa);

                if (id > 0) {
                        System.out.println("Processo criado com ID: " + id);
                        System.out.println("\nLista de processos atualizada:");
                        gp.listarProcessos();
                } else {
                        System.out.println("Falha na criacao do processo");
                }

                pausar();
        }

        /**
         * Demonstra o comando 'ps'
         */
        private static void demonstrarComandoPs() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO 'ps'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando 'ps' lista todos os processos existentes:");
                System.out.println("- ID do processo");
                System.out.println("- Nome do programa");
                System.out.println("- Estado atual (READY, RUNNING, etc.)");
                System.out.println("- Program Counter (PC)");
                System.out.println("- Numero de paginas alocadas");
                System.out.println();

                // Criar alguns processos se necessário
                if (gp.getNumeroProcessos() < 3) {
                        System.out.println("Criando processos para demonstracao...");
                        gp.criaProcesso("programa_matematica");
                        gp.criaProcesso("programa_fibonacci");
                        gp.criaProcesso("programa_simples");
                        System.out.println();
                }

                pausar();

                System.out.println("Executando comando: ps");
                System.out.println("=======================");
                gp.listarProcessos();

                pausar();
        }

        /**
         * Demonstra o comando 'dump'
         */
        private static void demonstrarComandoDump() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO 'dump'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando 'dump <id>' lista:");
                System.out.println("- Conteudo do PCB (Process Control Block)");
                System.out.println("- Estado dos registradores");
                System.out.println("- Tabela de paginas do processo");
                System.out.println("- Conteudo da memoria do processo");
                System.out.println();

                // Garantir que há processos
                if (gp.getNumeroProcessos() == 0) {
                        System.out.println("Criando processo para demonstracao...");
                        gp.criaProcesso("programa_matematica");
                }

                System.out.println("Processos disponiveis:");
                gp.listarProcessos();
                System.out.println();

                System.out.print("Digite o ID do processo para dump (ou ENTER para ID 1): ");
                String input = scanner.nextLine().trim();
                int id = input.isEmpty() ? 1 : Integer.parseInt(input);

                pausar();

                System.out.println("Executando comando: dump " + id);
                System.out.println("=======================================");
                gp.dumpProcesso(id);

                pausar();
        }

        /**
         * Demonstra o comando 'dumpM'
         */
        private static void demonstrarComandoDumpM() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO 'dumpM'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando 'dumpM <inicio> <fim>' lista:");
                System.out.println("- Memoria entre posicoes inicio e fim");
                System.out.println("- Independente do processo");
                System.out.println("- Mostra conteudo fisico da memoria");
                System.out.println("- Formato: [endereco]: [opcode, ra, rb, p]");
                System.out.println();

                System.out.print("Posicao inicial (padrao 0): ");
                String inicioStr = scanner.nextLine().trim();
                int inicio = inicioStr.isEmpty() ? 0 : Integer.parseInt(inicioStr);

                System.out.print("Posicao final (padrao 50): ");
                String fimStr = scanner.nextLine().trim();
                int fim = fimStr.isEmpty() ? 50 : Integer.parseInt(fimStr);

                pausar();

                System.out.println("Executando comando: dumpM " + inicio + " " + fim);
                System.out.println("=======================================");
                gp.dumpMemoria(inicio, fim);

                pausar();
        }

        /**
         * Demonstra o comando 'exec'
         */
        private static void demonstrarComandoExec() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO 'exec'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando 'exec <id>' executa o processo:");
                System.out.println("- Verifica se processo existe");
                System.out.println("- Carrega contexto na CPU");
                System.out.println("- Executa instrucoes sequencialmente");
                System.out.println("- Para quando encontra STOP");
                System.out.println();

                // Garantir que há processos
                if (gp.getNumeroProcessos() == 0) {
                        System.out.println("Criando processo para demonstracao...");
                        gp.criaProcesso("programa_simples");
                }

                System.out.println("Processos disponiveis:");
                gp.listarProcessos();
                System.out.println();

                System.out.print("Digite o ID do processo para executar (ou ENTER para ID 1): ");
                String input = scanner.nextLine().trim();
                int id = input.isEmpty() ? 1 : Integer.parseInt(input);

                pausar();

                System.out.println("Executando comando: exec " + id);
                System.out.println("=======================================");
                boolean sucesso = gp.executarProcesso(id);

                if (sucesso) {
                        System.out.println("Execucao concluida!");
                } else {
                        System.out.println("Erro na execucao!");
                }

                pausar();
        }

        /**
         * Demonstra os comandos traceOn/traceOff
         */
        private static void demonstrarComandosTrace() {
                System.out.println("\n========================================");
                System.out.println("    COMANDOS 'traceOn/traceOff'");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Os comandos de trace permitem debug:");
                System.out.println("- traceOn  - Liga modo de execucao com debug");
                System.out.println("- traceOff - Desliga modo debug");
                System.out.println("- CPU imprime cada instrucao executada");
                System.out.println("- Mostra PC e endereco fisico");
                System.out.println();

                // Criar processo se necessário
                if (gp.getNumeroProcessos() == 0) {
                        System.out.println("Criando processo para demonstracao...");
                        gp.criaProcesso("programa_matematica");
                }

                pausar();

                System.out.println("Executando comando: traceOn");
                System.out.println("============================");
                gp.ligarTrace();

                System.out.println("\nExecutando processo com trace ativo:");
                System.out.println("=====================================");
                gp.executarProcesso(1);

                System.out.println("\nExecutando comando: traceOff");
                System.out.println("=============================");
                gp.desligarTrace();

                System.out.println("Modo trace desligado!");

                pausar();
        }

        /**
         * Demonstra as estruturas do GP
         */
        private static void demonstrarEstruturas() {
                System.out.println("\n========================================");
                System.out.println("      ESTRUTURAS DO GP");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O GP utiliza as estruturas especificadas:");
                System.out.println("- PCB (Process Control Block)");
                System.out.println("- Fila Ready (processos prontos)");
                System.out.println("- Processo Running (processo em execucao)");
                System.out.println();

                // Criar processos para demonstrar
                System.out.println("Criando processos para demonstrar estruturas...");
                gp.criaProcesso("programa_matematica");
                gp.criaProcesso("programa_fibonacci");
                gp.criaProcesso("programa_simples");

                System.out.println("\nESTRUTURA: PCB (Process Control Block)");
                System.out.println("=======================================");
                System.out.println("Cada processo tem seu proprio PCB com:");
                System.out.println("- ID unico do processo");
                System.out.println("- Nome do programa");
                System.out.println("- Estado (NEW, READY, RUNNING, TERMINATED)");
                System.out.println("- Tabela de paginas");
                System.out.println("- Contexto da CPU (PC, registradores)");

                pausar();

                System.out.println("\nExemplo de PCB:");
                gp.dumpProcesso(1);

                pausar();

                System.out.println("\nESTRUTURA: Fila Ready");
                System.out.println("=====================");
                System.out.println("Lista de processos prontos para execucao:");
                gp.listarProcessos();

                pausar();

                System.out.println("\nESTRUTURA: Processo Running");
                System.out.println("===========================");
                System.out.println("Apenas um processo pode estar RUNNING por vez:");

                if (gp.getProcessoRodando() != null) {
                        System.out.println("- Processo em execucao: " + gp.getProcessoRodando().processId);
                } else {
                        System.out.println("- Nenhum processo em execucao no momento");
                }

                pausar();
        }

        /**
         * Interface completa de comandos
         */
        private static void interfaceCompleta() {
                System.out.println("\n========================================");
                System.out.println("    INTERFACE COMPLETA DE COMANDOS");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Entrando na interface completa do GP...");
                System.out.println("Digite 'help' para ver todos os comandos");
                System.out.println("Digite 'exit' para voltar ao menu principal");
                System.out.println();

                pausar();

                // Chama a interface completa do GP
                gp.interfaceComandos();

                System.out.println("\nVoltando ao menu principal...");
                pausar();
        }

        /**
         * Demonstração completa do GP
         */
        private static void demonstracaoCompleta() {
                System.out.println("\n========================================");
                System.out.println("    DEMONSTRACAO COMPLETA DO GP");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Esta demonstracao executa todos os aspectos do GP:");
                System.out.println("- Criacao de processos");
                System.out.println("- Listagem e dump");
                System.out.println("- Execucao com trace");
                System.out.println("- Remocao de processos");
                System.out.println();

                System.out.print("Deseja executar a demonstracao completa? (s/N): ");
                String resposta = scanner.nextLine().trim().toLowerCase();

                if (!resposta.equals("s") && !resposta.equals("sim")) {
                        return;
                }

                System.out.println("\nINICIANDO DEMONSTRACAO COMPLETA");
                System.out.println("===============================");

                // Fase 1: Criação
                System.out.println("\nFASE 1: Criacao de Processos");
                gp.criaProcesso("programa_matematica");
                gp.criaProcesso("programa_fibonacci");
                gp.criaProcesso("programa_simples");
                System.out.println("Processos criados");

                pausar();

                // Fase 2: Listagem
                System.out.println("\nFASE 2: Listagem de Processos");
                gp.listarProcessos();

                pausar();

                // Fase 3: Dump
                System.out.println("\nFASE 3: Dump de Processo");
                gp.dumpProcesso(1);

                pausar();

                // Fase 4: Execução com trace
                System.out.println("\nFASE 4: Execucao com Trace");
                gp.ligarTrace();
                gp.executarProcesso(1);
                gp.desligarTrace();

                pausar();

                // Fase 5: Remoção
                System.out.println("\nFASE 5: Remocao de Processo");
                gp.desalocaProcesso(2);
                gp.listarProcessos();

                System.out.println("\nDEMONSTRACAO COMPLETA FINALIZADA!");
                System.out.println("Todos os aspectos do GP foram demonstrados.");

                pausar();
        }

        /**
         * Utilitário para pausar e aguardar input do usuário
         */
        private static void pausar() {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine();
        }
}