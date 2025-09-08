import java.util.Scanner;

/**
 * Menu Principal para Demonstrações do Sistema
 * 
 * Menu simples com 3 opções principais:
 * - Teste do Gerente de Processos (GP)
 * - Teste do Escalonamento
 * - Teste do Gerente de Memória
 */
public class MenuPrincipal {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SISTEMA OPERACIONAL - T1");
        System.out.println("    Demonstrações Interativas");
        System.out.println("========================================");
        System.out.println();

        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    executarTesteGP();
                    break;
                case "2":
                    executarTesteEscalonamento();
                    break;
                case "3":
                    executarTesteMemoria();
                    break;
                case "0":
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcao invalida! Tente novamente.");
                    pausar();
                    break;
            }
        }

        System.out.println("\nObrigado por usar o sistema!");
        scanner.close();
    }

    /**
     * Mostra o menu principal
     */
    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("           MENU PRINCIPAL");
        System.out.println("========================================");
        System.out.println();
        System.out.println("1. Teste do Gerente de Processos (GP)");
        System.out.println("   - Comandos: new, rm, ps, dump, exec");
        System.out.println("   - Funcoes: criaProcesso, desalocaProcesso");
        System.out.println("   - Estruturas: PCB, fila ready, running");
        System.out.println();
        System.out.println("2. Teste do Escalonamento");
        System.out.println("   - Round-robin com preempcao");
        System.out.println("   - Execucao continua e escalonada");
        System.out.println("   - Salvamento de contexto");
        System.out.println();
        System.out.println("3. Teste do Gerente de Memoria");
        System.out.println("   - Alocacao e desalocacao de memoria");
        System.out.println("   - Paginacao e traducao de enderecos");
        System.out.println("   - Gerenciamento de frames");
        System.out.println();
        System.out.println("0. Sair");
        System.out.println();
        System.out.print("Escolha uma opcao: ");
    }

    /**
     * Executa o teste do GP
     */
    private static void executarTesteGP() {
        System.out.println("\n========================================");
        System.out.println("    TESTE DO GERENTE DE PROCESSOS");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Voce escolheu testar o Gerente de Processos!");
        System.out.println();
        System.out.println("Este teste inclui:");
        System.out.println("- Funcao criaProcesso() completa");
        System.out.println("- Funcao desalocaProcesso()");
        System.out.println("- Todos os comandos: new, rm, ps, dump, exec, trace");
        System.out.println("- Demonstracao das estruturas PCB, fila ready, running");
        System.out.println("- Interface completa de comandos");
        System.out.println();
        System.out.print("Pressione ENTER para iniciar...");
        scanner.nextLine();

        TesteGP.main(new String[] {});

        System.out.println("\nVoltando ao menu principal...");
        pausar();
    }

    /**
     * Executa o teste de escalonamento
     */
    private static void executarTesteEscalonamento() {
        System.out.println("\n========================================");
        System.out.println("      TESTE DO ESCALONAMENTO");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Voce escolheu testar o Escalonamento!");
        System.out.println();
        System.out.println("Este teste inclui:");
        System.out.println("- Carregamento de multiplos processos");
        System.out.println("- Comando execAll (execucao escalonada)");
        System.out.println("- Execucao continua em background");
        System.out.println("- Preempcao por tempo (Delta = 5 ciclos)");
        System.out.println("- Salvamento e restauracao de contexto");
        System.out.println("- Monitoramento do escalonador");
        System.out.println();
        System.out.print("Pressione ENTER para iniciar...");
        scanner.nextLine();

        TesteEscalonamento.main(new String[] {});

        System.out.println("\nVoltando ao menu principal...");
        pausar();
    }

    /**
     * Executa o teste do gerente de memória
     */
    private static void executarTesteMemoria() {
        System.out.println("\n========================================");
        System.out.println("    TESTE DO GERENTE DE MEMORIA");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Voce escolheu testar o Gerente de Memoria!");
        System.out.println();
        System.out.println("Este teste inclui:");
        System.out.println("- Alocacao e desalocacao de memoria");
        System.out.println("- Sistema de paginacao");
        System.out.println("- Traducao de enderecos logicos para fisicos");
        System.out.println("- Gerenciamento de frames");
        System.out.println("- Carregamento paginado de programas");
        System.out.println();
        System.out.print("Pressione ENTER para iniciar...");
        scanner.nextLine();

        TesteGerenteMemoria.main(new String[] {});

        System.out.println("\nVoltando ao menu principal...");
        pausar();
    }

    /**
     * Utilitário para pausar
     */
    private static void pausar() {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}