public class SysCallHandling {
    private HW hw;

    public SysCallHandling(HW _hw) {
        hw = _hw;
    }

    public void stop() {
        System.out.println("SYSCALL STOP");
    }

    public void handle() {
        System.out.println("SYSCALL pars:  " + hw.cpu.getReg(8) + " / " + hw.cpu.getReg(9));
        if (hw.cpu.getReg(8) == 1) {
            // leitura
        } else if (hw.cpu.getReg(8) == 2) {
            System.out.println("OUT:   " + hw.mem.pos[hw.cpu.getReg(9)].p);
        } else {
            System.out.println("  PARAMETRO INVALIDO");
        }
    }
}
