import java.util.Scanner;

/**
 * Teste Interativo Completo do Sistema Operacional com Gerente de Processos
 * 
 * Versão completamente interativa para demonstração em apresentação.
 * Permite ao usuário escolher quais funcionalidades demonstrar e
 * controlar o fluxo da apresentação.
 */
public class TesteSistemaCompleto {

    private static Scanner scanner = new Scanner(System.in);
    private static SO so;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          SISTEMA OPERACIONAL - GERENTE DE PROCESSOS         ║");
        System.out.println("║                    DEMONSTRAÇÃO INTERATIVA                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Inicialização do sistema
        inicializarSistema();

        // Menu principal interativo
        menuPrincipal();

        System.out.println("\n🎯 Demonstração finalizada!");
        System.out.println("Obrigado por usar o Sistema Operacional com Gerente de Processos!");
        scanner.close();
    }

    /**
     * Inicializa o sistema com confirmação do usuário
     */
    private static void inicializarSistema() {
        System.out.println("🚀 INICIALIZAÇÃO DO SISTEMA");
        System.out.println("═══════════════════════════");
        System.out.print("Pressione ENTER para inicializar o sistema...");
        scanner.nextLine();

        System.out.println("\n⚙️  Inicializando hardware...");
        HW hw = new HW(1024, 16); // 1024 palavras, páginas de 16

        System.out.println("⚙️  Inicializando Sistema Operacional...");
        so = new SO(hw);

        System.out.println("\n✅ Sistema inicializado com sucesso!");
        pausar();
    }

    /**
     * Menu principal da demonstração
     */
    private static void menuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    MENU DE DEMONSTRAÇÃO                     ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. 📋 Demonstrar Criação de Processos                      ║");
            System.out.println("║  2. 📊 Demonstrar Listagem de Processos (ps)                ║");
            System.out.println("║  3. 🔍 Demonstrar Dump de Processo                          ║");
            System.out.println("║  4. 💾 Demonstrar Dump de Memória                           ║");
            System.out.println("║  5. ▶️  Demonstrar Execução de Processos                     ║");
            System.out.println("║  6. 🗑️  Demonstrar Remoção de Processos                     ║");
            System.out.println("║  7. 🔧 Demonstrar Modo Trace                                ║");
            System.out.println("║  8. 🌐 Interface Completa de Comandos                       ║");
            System.out.println("║  9. 📈 Status do Sistema                                    ║");
            System.out.println("║  0. 🚪 Sair                                                 ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.print("\n👉 Escolha uma opção (0-9): ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    demonstrarCriacaoProcessos();
                    break;
                case "2":
                    demonstrarListagemProcessos();
                    break;
                case "3":
                    demonstrarDumpProcesso();
                    break;
                case "4":
                    demonstrarDumpMemoria();
                    break;
                case "5":
                    demonstrarExecucaoProcessos();
                    break;
                case "6":
                    demonstrarRemocaoProcessos();
                    break;
                case "7":
                    demonstrarModoTrace();
                    break;
                case "8":
                    interfaceCompleta();
                    break;
                case "9":
                    demonstrarStatusSistema();
                    break;
                case "0":
                    continuar = false;
                    break;
                default:
                    System.out.println("❌ Opção inválida! Pressione ENTER para continuar...");
                    scanner.nextLine();
                    break;
            }
        }
    }

    /**
     * Demonstra a criação de processos
     */
    private static void demonstrarCriacaoProcessos() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 DEMONSTRAÇÃO: CRIAÇÃO DE PROCESSOS          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("📋 Esta demonstração mostra como o GP cria processos:");
        System.out.println("   1. Verifica tamanho do programa");
        System.out.println("   2. Aloca memória via Gerente de Memória");
        System.out.println("   3. Cria PCB (Process Control Block)");
        System.out.println("   4. Carrega programa na memória");
        System.out.println("   5. Coloca processo na fila de prontos");
        System.out.println();

        System.out.println("🎯 Programas disponíveis:");
        System.out.println("   • hello      - Programa simples de saudação");
        System.out.println("   • contador   - Conta de 1 a 5");
        System.out.println("   • fibonacci  - Calcula sequência de Fibonacci");
        System.out.println("   • matematica - Operações aritméticas");
        System.out.println("   • loop       - Loop simples");
        System.out.println();

        pausar();

        // Criar processos interativamente
        System.out.println("🚀 Criando processos...");
        System.out.println();

        criarProcessoInterativo("hello", "Programa de saudação");
        criarProcessoInterativo("contador", "Programa contador");
        criarProcessoInterativo("fibonacci", "Programa Fibonacci");

        System.out.println("\n✅ Demonstração de criação concluída!");
        pausar();
    }

    /**
     * Cria um processo de forma interativa
     */
    private static void criarProcessoInterativo(String programa, String descricao) {
        System.out.println("➤ Criando processo: " + descricao);
        System.out.print("  Pressione ENTER para executar: new " + programa + " ... ");
        scanner.nextLine();

        int id = so.criarProcesso(programa);
        if (id > 0) {
            System.out.println("  ✅ Processo criado com ID: " + id);
        } else {
            System.out.println("  ❌ Falha na criação do processo");
        }
        System.out.println();
    }

    /**
     * Demonstra a listagem de processos
     */
    private static void demonstrarListagemProcessos() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                DEMONSTRAÇÃO: LISTAGEM DE PROCESSOS          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("📊 O comando 'ps' lista todos os processos no sistema:");
        System.out.println("   • ID do processo");
        System.out.println("   • Nome do programa");
        System.out.println("   • Estado atual (READY, RUNNING, etc.)");
        System.out.println("   • Program Counter (PC)");
        System.out.println("   • Número de páginas alocadas");
        System.out.println();

        pausar();

        System.out.println("🔍 Executando comando: ps");
        System.out.println("═══════════════════════════");
        so.listarProcessos();

        pausar();
    }

    /**
     * Demonstra o dump de processo
     */
    private static void demonstrarDumpProcesso() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 DEMONSTRAÇÃO: DUMP DE PROCESSO              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("🔍 O comando 'dump <id>' mostra:");
        System.out.println("   • Informações do PCB (Process Control Block)");
        System.out.println("   • Estado dos registradores");
        System.out.println("   • Tabela de páginas");
        System.out.println("   • Conteúdo da memória do processo");
        System.out.println();

        // Listar processos primeiro
        System.out.println("📋 Processos disponíveis:");
        so.listarProcessos();
        System.out.println();

        System.out.print("👉 Digite o ID do processo para dump (ou ENTER para ID 1): ");
        String input = scanner.nextLine().trim();
        int id = input.isEmpty() ? 1 : Integer.parseInt(input);

        System.out.println("\n🔍 Executando comando: dump " + id);
        System.out.println("═══════════════════════════════════");
        so.dumpProcesso(id);

        pausar();
    }

    /**
     * Demonstra o dump de memória
     */
    private static void demonstrarDumpMemoria() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 DEMONSTRAÇÃO: DUMP DE MEMÓRIA               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("💾 O comando 'dumpM <inicio> <fim>' mostra:");
        System.out.println("   • Conteúdo da memória física");
        System.out.println("   • Instruções e dados armazenados");
        System.out.println("   • Formato: [endereço]: [opcode, ra, rb, p]");
        System.out.println();

        System.out.print("👉 Início (padrão 0): ");
        String inicioStr = scanner.nextLine().trim();
        int inicio = inicioStr.isEmpty() ? 0 : Integer.parseInt(inicioStr);

        System.out.print("👉 Fim (padrão 30): ");
        String fimStr = scanner.nextLine().trim();
        int fim = fimStr.isEmpty() ? 30 : Integer.parseInt(fimStr);

        System.out.println("\n💾 Executando comando: dumpM " + inicio + " " + fim);
        System.out.println("═══════════════════════════════════════════");
        so.dumpMemoria(inicio, fim);

        pausar();
    }

    /**
     * Demonstra a execução de processos
     */
    private static void demonstrarExecucaoProcessos() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║               DEMONSTRAÇÃO: EXECUÇÃO DE PROCESSOS           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("▶️ O comando 'exec <id>' executa um processo:");
        System.out.println("   • Carrega contexto do processo na CPU");
        System.out.println("   • Executa instruções sequencialmente");
        System.out.println("   • Traduz endereços lógicos para físicos");
        System.out.println("   • Para quando encontra instrução STOP");
        System.out.println();

        // Listar processos
        System.out.println("📋 Processos disponíveis:");
        so.listarProcessos();
        System.out.println();

        System.out.print("👉 Digite o ID do processo para executar (ou ENTER para ID 1): ");
        String input = scanner.nextLine().trim();
        int id = input.isEmpty() ? 1 : Integer.parseInt(input);

        System.out.println("\n▶️ Executando processo " + id + "...");
        System.out.println("═══════════════════════════════════");
        so.executarProcesso(id);

        pausar();
    }

    /**
     * Demonstra a remoção de processos
     */
    private static void demonstrarRemocaoProcessos() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║               DEMONSTRAÇÃO: REMOÇÃO DE PROCESSOS            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("🗑️ O comando 'rm <id>' remove um processo:");
        System.out.println("   • Desaloca toda memória do processo");
        System.out.println("   • Remove de todas as filas");
        System.out.println("   • Libera o PCB");
        System.out.println("   • Libera recursos do sistema");
        System.out.println();

        // Listar processos antes
        System.out.println("📋 Processos ANTES da remoção:");
        so.listarProcessos();
        System.out.println();

        System.out.print("👉 Digite o ID do processo para remover (ou ENTER para ID 2): ");
        String input = scanner.nextLine().trim();
        int id = input.isEmpty() ? 2 : Integer.parseInt(input);

        System.out.println("\n🗑️ Removendo processo " + id + "...");
        System.out.println("═══════════════════════════════");
        boolean removido = so.removerProcesso(id);

        if (removido) {
            System.out.println("✅ Processo removido com sucesso!");
        } else {
            System.out.println("❌ Processo não encontrado!");
        }

        System.out.println("\n📋 Processos APÓS a remoção:");
        so.listarProcessos();

        pausar();
    }

    /**
     * Demonstra o modo trace
     */
    private static void demonstrarModoTrace() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                  DEMONSTRAÇÃO: MODO TRACE                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("🔧 O modo trace permite debug detalhado:");
        System.out.println("   • traceOn  - Liga modo debug");
        System.out.println("   • traceOff - Desliga modo debug");
        System.out.println("   • Mostra cada instrução executada");
        System.out.println("   • Exibe PC e endereço físico");
        System.out.println();

        pausar();

        System.out.println("🔧 Ligando modo trace...");
        so.ligarTrace();

        System.out.println("\n▶️ Executando processo com trace ativo:");
        System.out.println("═══════════════════════════════════════");

        // Criar um processo simples se necessário
        int id = so.criarProcesso("hello");
        so.executarProcesso(id);

        System.out.println("\n🔧 Desligando modo trace...");
        so.desligarTrace();

        pausar();
    }

    /**
     * Abre a interface completa de comandos
     */
    private static void interfaceCompleta() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 INTERFACE COMPLETA DE COMANDOS              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("🌐 Entrando na interface completa do sistema...");
        System.out.println("   Digite 'help' para ver todos os comandos");
        System.out.println("   Digite 'exit' para voltar ao menu principal");
        System.out.println();

        pausar();

        // Chama a interface completa
        so.interfaceComandos();

        System.out.println("\n🔙 Voltando ao menu principal...");
        pausar();
    }

    /**
     * Demonstra o status do sistema
     */
    private static void demonstrarStatusSistema() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                 DEMONSTRAÇÃO: STATUS DO SISTEMA             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("📈 O status do sistema mostra:");
        System.out.println("   • Processos ativos no GP");
        System.out.println("   • Processo em execução");
        System.out.println("   • Status da memória");
        System.out.println("   • Frames livres/ocupados");
        System.out.println();

        pausar();

        System.out.println("📈 Status atual do sistema:");
        System.out.println("═══════════════════════════");
        so.showSystemStatus();

        pausar();
    }

    /**
     * Utilitário para pausar e aguardar input do usuário
     */
    private static void pausar() {
        System.out.print("\n⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Utilitário para limpar a tela (simulado)
     */
    private static void limparTela() {
        // Imprime várias linhas em branco para simular limpeza de tela
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }
}