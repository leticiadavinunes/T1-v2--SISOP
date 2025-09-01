public class HW {
    public Memory mem;
    public CPU cpu;

    public HW(int tamMem, int tamPg) {
        mem = new Memory(tamMem, tamPg);
        cpu = new CPU(mem, true); // true liga debug
    }
}
