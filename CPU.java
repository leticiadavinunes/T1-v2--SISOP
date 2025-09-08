public class CPU {
    private int maxInt;
    private int minInt;
    private int pc;
    private Word ir;
    private int[] reg;
    private Interrupts irpt;
    private Word[] m;
    private InterruptHandling ih;
    private SysCallHandling sysCall;
    private boolean cpuStop;
    private boolean debug;
    private Utilities u;

    public CPU(Memory _mem, boolean _debug) {
        maxInt = 32767;
        minInt = -32767;
        m = _mem.pos;
        reg = new int[10];
        debug = _debug;
    }

    public void setAddressOfHandlers(InterruptHandling _ih, SysCallHandling _sysCall) {
        ih = _ih;
        sysCall = _sysCall;
    }

    public void setUtilities(Utilities _u) {
        u = _u;
    }

    // Métodos restantes omitidos para brevidade

    public int getPC() {
        return pc;
    }

    public int getReg(int idx) {
        return reg[idx];
    }

    public void setReg(int idx, int value) {
        reg[idx] = value;
    }

    public void setPC(int _pc) {
        pc = _pc;
    }

    public void setContext(int _pc) {
        pc = _pc;
        irpt = Interrupts.noInterrupt;
    }

    public void setInterrupt(Interrupts interrupt) {
        irpt = interrupt;
    }

    public Interrupts getInterrupt() {
        return irpt;
    }

    public void run() {
        cpuStop = false;
        // Implementação simplificada do ciclo de execução
        // Em uma implementação completa, aqui seria o fetch-decode-execute

        while (!cpuStop) {
            try {
                // Verifica se há interrupção pendente
                if (irpt != Interrupts.noInterrupt) {
                    ih.handle(irpt);
                    irpt = Interrupts.noInterrupt;
                }

                // Fetch da instrução
                if (pc >= 0 && pc < m.length) {
                    ir = m[pc];

                    // Decode e Execute (simplificado)
                    executeInstruction(ir);

                    // Incrementa PC (pode ser alterado pela instrução)
                    if (!cpuStop) {
                        pc++;
                    }
                } else {
                    // Endereço inválido
                    irpt = Interrupts.intEnderecoInvalido;
                }

            } catch (Exception e) {
                System.out.println("Erro na CPU: " + e.getMessage());
                cpuStop = true;
            }
        }
    }

    private void executeInstruction(Word instruction) {
        // Implementação simplificada de algumas instruções
        switch (instruction.opc) {
            case STOP:
                sysCall.stop();
                cpuStop = true;
                break;
            case LDI:
                if (instruction.ra >= 0 && instruction.ra < reg.length) {
                    reg[instruction.ra] = instruction.p;
                }
                break;
            case ADD:
                if (instruction.ra >= 0 && instruction.ra < reg.length &&
                        instruction.rb >= 0 && instruction.rb < reg.length &&
                        instruction.p >= 0 && instruction.p < reg.length) {
                    reg[instruction.ra] = reg[instruction.rb] + reg[instruction.p];
                }
                break;
            case SYSCALL:
                sysCall.handle();
                break;
            // Adicione mais instruções conforme necessário
            default:
                // Instrução não implementada - apenas continua
                break;
        }
    }

    public void stop() {
        cpuStop = true;
    }

    public boolean isStopped() {
        return cpuStop;
    }
}
