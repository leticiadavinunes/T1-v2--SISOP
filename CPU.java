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

    public void setContext(int _pc) {
        pc = _pc;
        irpt = Interrupts.noInterrupt;
    }

    public void run() {
        cpuStop = false;
        // Implementação simplificada: apenas para evitar erro de compilação
        // Implemente o ciclo de execução conforme necessário
    }
}
