public class SysCallHandling {
    private HW hw;
    private ProcessManager processManager;

    public SysCallHandling(HW _hw) {
        hw = _hw;
    }

    public void setProcessManager(ProcessManager pm) {
        this.processManager = pm;
    }

    public void stop() {
        System.out.println("SYSCALL STOP - Terminando processo");
        if (processManager != null) {
            processManager.getScheduler().terminateCurrentProcess();
            processManager.getScheduler().schedule(); // Escala próximo processo
        }
    }

    public void handle() {
        System.out.println("SYSCALL pars:  " + hw.cpu.getReg(8) + " / " + hw.cpu.getReg(9));
        if (hw.cpu.getReg(8) == 1) {
            // leitura
        } else if (hw.cpu.getReg(8) == 2) {
            System.out.println("OUT:   " + hw.mem.pos[hw.cpu.getReg(9)].p);
        } else if (hw.cpu.getReg(8) == 0) {
            // STOP system call
            stop();
        } else {
            System.out.println("  PARAMETRO INVALIDO");
        }
    }
}
