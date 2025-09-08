import java.util.Scanner;

/**
 * Teste Interativo do Sistema de Escalonamento
 *
 * Demonstra todas as funcionalidades do escalonamento de processos:
 * - Carregamento de multiplos processos
 * - Comando execAll (execucao escalonada)
 * - Execucao continua em background
 * - Adicao de processos durante execucao
 * - Salvamento/restauracao de contexto
 * - Preempcao por tempo
 */
public class TesteEscalonamento {

        private static Scanner scanner = new Scanner(System.in);
        private static HW hw;
        private static SO so;

        public static void main(String[] args) {
                System.out.println("========================================");
                System.out.println("  SISTEMA DE ESCALONAMENTO DE PROCESSOS");
                System.out.println("      Demonstracao Interativa");
                System.out.println("========================================");
                System.out.println();

                // Inicialização
                inicializarSistema();

                // Menu principal
                menuPrincipal();

                System.out.println("\nDemonstracao do escalonamento finalizada!");
                scanner.close();
        }

        /**
         * Inicializa o sistema de escalonamento
         */
        private static void inicializarSistema() {
                System.out.println("INICIALIZACAO DO SISTEMA DE ESCALONAMENTO");
                System.out.println("==========================================");
                System.out.print("Pressione ENTER para inicializar...");
                scanner.nextLine();

                System.out.println("\nCriando hardware (1024 palavras, paginas de 16)...");
                hw = new HW(1024, 16);

                System.out.println("Inicializando Sistema Operacional com escalonamento...");
                so = new SO(hw);

                System.out.println("\nSistema de escalonamento inicializado!");
                System.out.println("Caracteristicas do escalonamento:");
                System.out.println("- Politica: Round-Robin");
                System.out.println("- Fatia de tempo (Delta): 5 ciclos");
                System.out.println("- Preempcao automatica");
                System.out.println("- Salvamento/restauracao de contexto");

                pausar();
        }

        /**
         * Menu principal da demonstração
         */
        private static void menuPrincipal() {
                boolean continuar = true;

                while (continuar) {
                        System.out.println("\n========================================");
                        System.out.println("    MENU - ESCALONAMENTO");
                        System.out.println("========================================");
                        System.out.println();
                        System.out.println("1. Carregar multiplos processos");
                        System.out.println("2. Demonstrar comando execAll");
                        System.out.println("3. Demonstrar execucao continua");
                        System.out.println("4. Adicionar processos durante execucao");
                        System.out.println("5. Demonstrar salvamento de contexto");
                        System.out.println("6. Demonstrar preempcao por tempo");
                        System.out.println("7. Monitorar status do escalonador");
                        System.out.println("8. Simulacao completa de escalonamento");
                        System.out.println("0. Voltar");
                        System.out.println();
                        System.out.print("Escolha uma opcao: ");

                        String opcao = scanner.nextLine().trim();

                        switch (opcao) {
                                case "1":
                                        demonstrarCarregamentoMultiplo();
                                        break;
                                case "2":
                                        demonstrarExecAll();
                                        break;
                                case "3":
                                        demonstrarExecucaoContinua();
                                        break;
                                case "4":
                                        demonstrarAdicaoDuranteExecucao();
                                        break;
                                case "5":
                                        demonstrarSalvamentoContexto();
                                        break;
                                case "6":
                                        demonstrarPreempcao();
                                        break;
                                case "7":
                                        monitorarEscalonador();
                                        break;
                                case "8":
                                        simulacaoCompleta();
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
         * Demonstra o carregamento de múltiplos processos
         */
        private static void demonstrarCarregamentoMultiplo() {
                System.out.println("\n========================================");
                System.out.println("    CARREGAMENTO MULTIPLO");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Esta demonstracao mostra como carregar multiplos processos");
                System.out.println("para o escalonamento simultaneo:");
                System.out.println();
                System.out.println("- Cada processo e carregado no ProcessManager");
                System.out.println("- Processos ficam prontos para escalonamento");
                System.out.println("- Sistema gerencia automaticamente a memoria");
                System.out.println();

                pausar();

                System.out.println("Carregando processos para escalonamento...");
                System.out.println();

                carregarProcessoInterativo("fibonacci", "Calcula Fibonacci - processo CPU intensivo");
                carregarProcessoInterativo("contador", "Contador 1-5 - processo medio");
                carregarProcessoInterativo("matematica", "Operacoes matematicas - processo rapido");
                carregarProcessoInterativo("hello", "Programa simples - processo muito rapido");

                System.out.println("\nStatus apos carregamento:");
                so.showSystemStatus();

                pausar();
        }

        /**
         * Carrega um processo de forma interativa
         */
        private static void carregarProcessoInterativo(String programa, String descricao) {
                System.out.println("Carregando: " + descricao);
                System.out.print("Pressione ENTER para executar: loadProgram " + programa + " ... ");
                scanner.nextLine();

                boolean sucesso = so.loadProgram(programa);
                if (sucesso) {
                        System.out.println("Processo " + programa + " carregado no escalonador");
                } else {
                        System.out.println("Falha no carregamento");
                }
                System.out.println();
        }

        /**
         * Demonstra o comando execAll
         */
        private static void demonstrarExecAll() {
                System.out.println("\n========================================");
                System.out.println("        COMANDO EXECALL");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O comando execAll executa todos os processos de forma escalonada:");
                System.out.println("- Inicia escalonamento round-robin");
                System.out.println("- Cada processo executa por uma fatia de tempo");
                System.out.println("- Troca de contexto automatica");
                System.out.println("- Continua ate todos os processos terminarem");
                System.out.println();

                // Verificar se há processos carregados
                System.out.println("Status atual do sistema:");
                so.showSystemStatus();
                System.out.println();

                System.out.print("Deseja carregar processos primeiro? (s/N): ");
                String resposta = scanner.nextLine().trim().toLowerCase();

                if (resposta.equals("s") || resposta.equals("sim")) {
                        System.out.println("\nCarregando processos...");
                        so.loadProgram("fibonacci");
                        so.loadProgram("contador");
                        so.loadProgram("matematica");
                }

                System.out.println("\nExecutando comando: execAll");
                System.out.println("============================");
                System.out.print("Pressione ENTER para iniciar execucao escalonada...");
                scanner.nextLine();

                so.execAll();

                System.out.println("\nExecucao escalonada concluida!");
                pausar();
        }

        /**
         * Demonstra execução contínua
         */
        private static void demonstrarExecucaoContinua() {
                System.out.println("\n========================================");
                System.out.println("        EXECUCAO CONTINUA");
                System.out.println("========================================");
                System.out.println();

                System.out.println("A execucao continua permite:");
                System.out.println("- Escalonamento em thread separada");
                System.out.println("- Execucao em background");
                System.out.println("- Adicao de processos durante execucao");
                System.out.println("- Controle interativo do usuario");
                System.out.println();

                pausar();

                // Carregar alguns processos
                System.out.println("Preparando processos para execucao continua...");
                so.loadProgram("contador");
                so.loadProgram("fibonacci");

                System.out.println("\nIniciando execucao continua...");
                System.out.print("Pressione ENTER para iniciar...");
                scanner.nextLine();

                so.startContinuousExecution();

                System.out.println("Execucao continua iniciada!");
                System.out.println("Aguardando 3 segundos para observar execucao...");

                try {
                        Thread.sleep(3000);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }

                System.out.println("\nStatus durante execucao continua:");
                so.showSystemStatus();

                System.out.println("\nParando execucao continua...");
                System.out.print("Pressione ENTER para parar...");
                scanner.nextLine();

                so.stopContinuousExecution();
                System.out.println("Execucao continua parada!");

                pausar();
        }

        /**
         * Demonstra adição de processos durante execução
         */
        private static void demonstrarAdicaoDuranteExecucao() {
                System.out.println("\n========================================");
                System.out.println("    ADICAO DURANTE EXECUCAO");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Esta demonstracao mostra como adicionar processos");
                System.out.println("enquanto o escalonamento esta ativo:");
                System.out.println("- Sistema continua executando");
                System.out.println("- Novos processos entram na fila");
                System.out.println("- Escalonamento se adapta automaticamente");
                System.out.println();

                pausar();

                // Iniciar com alguns processos
                System.out.println("Iniciando com processos base...");
                so.loadProgram("contador");
                so.startContinuousExecution();

                System.out.println("Execucao continua ativa!");
                System.out.println("\nAdicionando processos durante execucao...");

                try {
                        Thread.sleep(1000);
                        System.out.println("Adicionando fibonacci...");
                        so.loadProgram("fibonacci");

                        Thread.sleep(1000);
                        System.out.println("Adicionando matematica...");
                        so.loadProgram("matematica");

                        Thread.sleep(2000);
                        System.out.println("\nStatus com processos adicionados:");
                        so.showSystemStatus();

                        Thread.sleep(1000);

                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }

                System.out.println("\nParando execucao...");
                so.stopContinuousExecution();

                pausar();
        }

        /**
         * Demonstra salvamento de contexto
         */
        private static void demonstrarSalvamentoContexto() {
                System.out.println("\n========================================");
                System.out.println("      SALVAMENTO DE CONTEXTO");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O salvamento de contexto e fundamental para o escalonamento:");
                System.out.println("- PCB armazena estado completo do processo");
                System.out.println("- Registradores sao salvos/restaurados");
                System.out.println("- Program Counter (PC) e preservado");
                System.out.println("- Processo continua de onde parou");
                System.out.println();

                pausar();

                // Criar processo e mostrar contexto
                System.out.println("Criando processo para demonstrar contexto...");
                int id = so.criarProcesso("fibonacci");

                System.out.println("\nPCB inicial do processo:");
                so.dumpProcesso(id);

                System.out.println("\nExecutando algumas instrucoes...");
                System.out.print("Pressione ENTER para executar...");
                scanner.nextLine();

                so.executarProcesso(id);

                System.out.println("\nPCB apos execucao (contexto salvo):");
                so.dumpProcesso(id);

                System.out.println("\nObserve como o PC e registradores foram atualizados!");

                pausar();
        }

        /**
         * Demonstra preempção por tempo
         */
        private static void demonstrarPreempcao() {
                System.out.println("\n========================================");
                System.out.println("      PREEMPCAO POR TEMPO");
                System.out.println("========================================");
                System.out.println();

                System.out.println("A preempcao por tempo garante fairness:");
                System.out.println("- Cada processo executa por Delta=5 ciclos");
                System.out.println("- Apos o tempo, processo e preemptado");
                System.out.println("- Contexto e salvo automaticamente");
                System.out.println("- Proximo processo da fila assume CPU");
                System.out.println();

                pausar();

                System.out.println("Carregando processos para demonstrar preempcao...");
                so.loadProgram("fibonacci"); // Processo longo
                so.loadProgram("contador"); // Processo médio
                so.loadProgram("hello"); // Processo curto

                System.out.println("\nIniciando execucao com preempcao...");
                System.out.print("Pressione ENTER para ver preempcao em acao...");
                scanner.nextLine();

                // Executar com observação de preempção
                so.execAll();

                System.out.println("\nObserve como os processos foram alternados!");
                System.out.println("Cada um executou por sua fatia de tempo antes da troca.");

                pausar();
        }

        /**
         * Monitora o status do escalonador
         */
        private static void monitorarEscalonador() {
                System.out.println("\n========================================");
                System.out.println("    MONITORAMENTO DO ESCALONADOR");
                System.out.println("========================================");
                System.out.println();

                System.out.println("O monitoramento permite observar:");
                System.out.println("- Processo atual em execucao");
                System.out.println("- Processos na fila de prontos");
                System.out.println("- Tempo atual da fatia");
                System.out.println("- Status da memoria");
                System.out.println();

                pausar();

                // Carregar processos
                System.out.println("Preparando sistema para monitoramento...");
                so.loadProgram("fibonacci");
                so.loadProgram("contador");
                so.loadProgram("matematica");

                System.out.println("\nStatus inicial:");
                so.showSystemStatus();

                System.out.println("\nIniciando execucao continua para monitoramento...");
                so.startContinuousExecution();

                // Monitorar por alguns ciclos
                for (int i = 1; i <= 3; i++) {
                        try {
                                Thread.sleep(2000);
                                System.out.println("\nStatus - Ciclo " + i + ":");
                                so.showSystemStatus();
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                        }
                }

                System.out.println("\nParando monitoramento...");
                so.stopContinuousExecution();

                pausar();
        }

        /**
         * Simulação completa de escalonamento
         */
        private static void simulacaoCompleta() {
                System.out.println("\n========================================");
                System.out.println("    SIMULACAO COMPLETA DE ESCALONAMENTO");
                System.out.println("========================================");
                System.out.println();

                System.out.println("Esta simulacao demonstra todos os aspectos do escalonamento:");
                System.out.println("- Carregamento de multiplos processos");
                System.out.println("- Execucao escalonada (execAll)");
                System.out.println("- Execucao continua");
                System.out.println("- Adicao dinamica de processos");
                System.out.println("- Monitoramento em tempo real");
                System.out.println();

                System.out.print("Deseja executar a simulacao completa? (s/N): ");
                String resposta = scanner.nextLine().trim().toLowerCase();

                if (!resposta.equals("s") && !resposta.equals("sim")) {
                        return;
                }

                System.out.println("\nINICIANDO SIMULACAO COMPLETA");
                System.out.println("============================");

                // Fase 1: Carregamento
                System.out.println("\nFASE 1: Carregamento de Processos");
                so.loadProgram("fibonacci");
                so.loadProgram("contador");
                System.out.println("Processos iniciais carregados");

                pausar();

                // Fase 2: execAll
                System.out.println("\nFASE 2: Execucao Escalonada (execAll)");
                so.execAll();
                System.out.println("Execucao escalonada concluida");

                pausar();

                // Fase 3: Execução contínua
                System.out.println("\nFASE 3: Execucao Continua");
                so.loadProgram("matematica");
                so.startContinuousExecution();

                try {
                        Thread.sleep(2000);
                        System.out.println("Adicionando processo durante execucao...");
                        so.loadProgram("hello");
                        Thread.sleep(2000);
                        System.out.println("Status final:");
                        so.showSystemStatus();
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }

                so.stopContinuousExecution();
                System.out.println("Execucao continua finalizada");

                System.out.println("\nSIMULACAO COMPLETA FINALIZADA!");
                System.out.println("Todos os aspectos do escalonamento foram demonstrados.");

                pausar();
        }

        /**
         * Utilitário para pausar
         */
        private static void pausar() {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine();
        }
}