/**
 * Process Control Block (PCB) - Bloco de Controle de Processo
 *
 * Estrutura de dados que armazena todas as informações necessárias sobre um
 * processo,
 * incluindo seu contexto de execução (registradores, PC, etc.) para permitir
 * salvamento e restauração durante trocas de contexto no escalonamento.
 */
public class PCB {
    // === INFORMAÇÕES DO PROCESSO ===
    public int processId; // Identificador único do processo
    public String programName; // Nome do programa que originou este processo
    public ProcessState state; // Estado atual do processo (READY, RUNNING, etc.)
    public int[] tabelaPaginas; // Tabela de páginas para tradução de endereços
    public int tamanhoPrograma; // Tamanho do programa em palavras

    // === CONTEXTO DA CPU SALVO ===
    // Estes campos armazenam o estado da CPU quando o processo é retirado dela
    public int pc; // Program Counter (próxima instrução a executar)
    public int[] registers; // Cópia de todos os registradores da CPU
    public Interrupts savedInterrupt; // Estado de interrupção salvo

    /**
     * Construtor do PCB
     * 
     * @param id      Identificador único do processo
     * @param name    Nome do programa
     * @param tabela  Tabela de páginas já alocada pelo gerente de memória
     * @param tamanho Tamanho do programa em palavras
     */
    public PCB(int id, String name, int[] tabela, int tamanho) {
        this.processId = id;
        this.programName = name;
        this.tabelaPaginas = tabela;
        this.tamanhoPrograma = tamanho;
        this.state = ProcessState.READY; // Processo inicia pronto para execução
        this.registers = new int[10]; // 10 registradores como definido na CPU
        this.pc = 0; // Inicia no endereço 0 do programa
        this.savedInterrupt = Interrupts.noInterrupt; // Sem interrupção inicial
    }

    /**
     * Salva o contexto atual da CPU neste PCB
     * Chamado quando o processo é retirado da CPU (preempção ou bloqueio)
     * 
     * @param cpu Referência para a CPU atual
     */
    public void saveContext(CPU cpu) {
        this.pc = cpu.getPC(); // Salva o Program Counter
        this.savedInterrupt = cpu.getInterrupt(); // Salva estado de interrupção

        // Salva todos os registradores da CPU
        for (int i = 0; i < registers.length; i++) {
            this.registers[i] = cpu.getReg(i);
        }
    }

    /**
     * Restaura o contexto deste PCB na CPU
     * Chamado quando o processo volta a executar na CPU
     * 
     * @param cpu Referência para a CPU atual
     */
    public void restoreContext(CPU cpu) {
        cpu.setPC(this.pc); // Restaura o Program Counter
        cpu.setInterrupt(this.savedInterrupt); // Restaura estado de interrupção

        // Restaura todos os registradores na CPU
        for (int i = 0; i < registers.length; i++) {
            cpu.setReg(i, this.registers[i]);
        }
    }

    /**
     * Representação textual do PCB para debug e monitoramento
     */
    @Override
    public String toString() {
        return "PCB[ID=" + processId + ", Name=" + programName +
                ", State=" + state + ", PC=" + pc + "]";
    }
}