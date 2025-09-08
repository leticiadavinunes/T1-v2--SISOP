/**
 * Classe de Teste do Sistema de Escalonamento
 *
 * Programa de teste abrangente que valida todas as funcionalidades
 * implementadas no sistema de escalonamento de processos:
 *
 * 1. Carregamento de múltiplos processos
 * 2. Comando execAll (execução escalonada)
 * 3. Execução contínua em background
 * 4. Adição de processos durante execução
 * 5. Salvamento/restauração de contexto
 * 6. Preempção por tempo
 * 7. Término de processos
 * 8. Gerenciamento de memória
 */
public class TesteEscalonamento {

        public static void main(String[] args) {
                // === INICIALIZAÇÃO DO SISTEMA ===
                HW hw = new HW(1024, 16); // 1024 palavras de memória, páginas de 16 palavras
                SO so = new SO(hw); // Sistema operacional com escalonamento

                // Cria programas de teste no repositório
                criarProgramasTeste(so.programs);

                System.out.println("=== TESTE DO SISTEMA DE ESCALONAMENTO ===\n");

                // === TESTE 1: CARREGAMENTO DE PROCESSOS ===
                System.out.println("1. Carregando processos...");
                so.loadProgram("programa1"); // Programa com operações matemáticas
                so.loadProgram("programa2"); // Programa com loop
                so.loadProgram("programa3"); // Programa com operações de memória

                System.out.println("\n2. Status inicial do sistema:");
                so.showSystemStatus();

                // === TESTE 2: COMANDO EXECALL ===
                System.out.println("\n3. Executando todos os processos (execAll):");
                so.execAll(); // Executa todos os processos de forma escalonada

                System.out.println("\n4. Status após execAll:");
                so.showSystemStatus();

                // === TESTE 3: EXECUÇÃO CONTÍNUA ===
                System.out.println("\n5. Testando execução contínua...");

                // Carrega mais processos para testar execução contínua
                so.loadProgram("programa4"); // Programa Fibonacci
                so.loadProgram("programa5"); // Programa simples

                // Inicia execução contínua em thread separada
                so.startContinuousExecution();

                // === TESTE 4: COMANDOS DURANTE EXECUÇÃO CONTÍNUA ===
                try {
                        Thread.sleep(2000); // Deixa executar por 2 segundos

                        System.out.println("\n6. Status durante execução contínua:");
                        so.showSystemStatus();

                        // Testa adição de processo durante execução contínua
                        System.out.println("\n7. Adicionando processo durante execução contínua:");
                        so.loadProgram("programa6"); // Programa com syscall

                        Thread.sleep(3000); // Mais 3 segundos de execução

                        // Para a execução contínua
                        System.out.println("\n8. Parando execução contínua:");
                        so.stopContinuousExecution();

                        Thread.sleep(500); // Aguarda thread parar

                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                // === RESULTADO FINAL ===
                System.out.println("\n9. Status final do sistema:");
                so.showSystemStatus();

                System.out.println("\n=== TESTE CONCLUÍDO ===");
        }

        /**
         * Cria programas de teste para validar o sistema de escalonamento
         *
         * Cada programa testa aspectos diferentes do sistema:
         * - Programa 1: Operações matemáticas básicas
         * - Programa 2: Loop com controle de fluxo (testa preempção)
         * - Programa 3: Operações de memória (load/store)
         * - Programa 4: Programa mais longo (Fibonacci - testa múltiplas preempções)
         * - Programa 5: Programa muito simples (execução rápida)
         * - Programa 6: Programa com syscall (testa chamadas de sistema)
         */
        private static void criarProgramasTeste(Programs programs) {

                // === PROGRAMA 1: OPERAÇÕES MATEMÁTICAS ===
                // Testa operações aritméticas básicas e armazenamento
                Word[] prog1 = {
                                new Word(Opcode.LDI, 0, -1, 10), // R0 = 10
                                new Word(Opcode.LDI, 1, -1, 5), // R1 = 5
                                new Word(Opcode.ADD, 2, 0, 1), // R2 = R0 + R1 = 15
                                new Word(Opcode.MULT, 3, 2, 1), // R3 = R2 * R1 = 75
                                new Word(Opcode.STD, 3, -1, 20), // MEM[20] = R3 = 75
                                new Word(Opcode.STOP, -1, -1, -1) // Termina processo
                };

                // === PROGRAMA 2: LOOP COM CONTROLE ===
                // Testa controle de fluxo e preempção durante loops
                Word[] prog2 = {
                                new Word(Opcode.LDI, 0, -1, 0), // R0 = 0 (contador)
                                new Word(Opcode.LDI, 1, -1, 5), // R1 = 5 (limite do loop)
                                new Word(Opcode.ADDI, 0, -1, 1), // R0++ (incrementa contador)
                                new Word(Opcode.SUB, 2, 1, 0), // R2 = R1 - R0 (testa condição)
                                new Word(Opcode.JMPIG, 2, -1, 2), // Se R2 > 0, volta para linha 2
                                new Word(Opcode.STD, 0, -1, 25), // MEM[25] = contador final
                                new Word(Opcode.STOP, -1, -1, -1) // Termina processo
                };

                // === PROGRAMA 3: OPERAÇÕES DE MEMÓRIA ===
                // Testa load/store e manipulação de dados na memória
                Word[] prog3 = {
                                new Word(Opcode.LDI, 0, -1, 100), // R0 = 100
                                new Word(Opcode.STD, 0, -1, 30), // MEM[30] = 100
                                new Word(Opcode.LDD, 1, -1, 30), // R1 = MEM[30] = 100
                                new Word(Opcode.ADDI, 1, -1, 50), // R1 = R1 + 50 = 150
                                new Word(Opcode.STD, 1, -1, 31), // MEM[31] = 150
                                new Word(Opcode.STOP, -1, -1, -1) // Termina processo
                };

                // === PROGRAMA 4: FIBONACCI (PROGRAMA LONGO) ===
                // Testa múltiplas preempções e salvamento/restauração de contexto
                Word[] prog4 = {
                                new Word(Opcode.LDI, 0, -1, 1), // R0 = 1 (fib anterior)
                                new Word(Opcode.LDI, 1, -1, 1), // R1 = 1 (fib atual)
                                new Word(Opcode.ADD, 2, 0, 1), // R2 = R0 + R1 (próximo fib)
                                new Word(Opcode.MOVE, 0, -1, 1), // R0 = R1 (atualiza anterior)
                                new Word(Opcode.MOVE, 1, -1, 2), // R1 = R2 (atualiza atual)
                                new Word(Opcode.STD, 2, -1, 40), // MEM[40] = fibonacci atual
                                new Word(Opcode.SUBI, 2, -1, 10), // R2 = R2 - 10 (testa parada)
                                new Word(Opcode.JMPIL, 2, -1, 2), // Se R2 < 0, continua loop
                                new Word(Opcode.STOP, -1, -1, -1) // Termina quando fib >= 10
                };

                // === PROGRAMA 5: PROGRAMA SIMPLES ===
                // Execução muito rápida para testar escalonamento com processos curtos
                Word[] prog5 = {
                                new Word(Opcode.LDI, 0, -1, 42), // R0 = 42 (resposta universal)
                                new Word(Opcode.STD, 0, -1, 50), // MEM[50] = 42
                                new Word(Opcode.STOP, -1, -1, -1) // Termina imediatamente
                };

                // === PROGRAMA 6: SYSCALL ===
                // Testa chamadas de sistema e saída de dados
                Word[] prog6 = {
                                new Word(Opcode.LDI, 8, -1, 2), // R8 = 2 (código syscall OUT)
                                new Word(Opcode.LDI, 9, -1, 60), // R9 = 60 (endereço do dado)
                                new Word(Opcode.LDI, 0, -1, 999), // R0 = 999 (valor a imprimir)
                                new Word(Opcode.STD, 0, -1, 60), // MEM[60] = 999
                                new Word(Opcode.SYSCALL, -1, -1, -1), // Chama syscall para imprimir
                                new Word(Opcode.STOP, -1, -1, -1) // Termina processo
                };

                // === CRIAÇÃO DO REPOSITÓRIO DE PROGRAMAS ===
                Program[] progs = {
                                new Program("programa1", prog1), // Matemática
                                new Program("programa2", prog2), // Loop
                                new Program("programa3", prog3), // Memória
                                new Program("programa4", prog4), // Fibonacci
                                new Program("programa5", prog5), // Simples
                                new Program("programa6", prog6) // Syscall
                };

                programs.progs = progs;
        }
}