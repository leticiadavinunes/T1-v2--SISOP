public class InterruptHandling {
    private HW hw;
    private ProcessManager processManager;

    public InterruptHandling(HW _hw) {
        hw = _hw;
    }

    public void setProcessManager(ProcessManager pm) {
        this.processManager = pm;
    }

    public void handle(Interrupts irpt) {
        System.out.println("Interrupcao " + irpt + "   pc: " + hw.cpu.getPC());

        switch (irpt) {
            case intTimer:
                handleTimerInterrupt();
                break;
            case intEnderecoInvalido:
            case intInstrucaoInvalida:
            case intOverflow:
                System.out.println("Erro fatal - terminando processo atual");
                if (processManager != null) {
                    processManager.getScheduler().terminateCurrentProcess();
                }
                break;
            default:
                break;
        }
    }

    private void handleTimerInterrupt() {
        System.out.println("Timer interrupt - fazendo preempção");
        if (processManager != null) {
            // O escalonador já vai tratar a preempção
            processManager.getScheduler().schedule();
        }
    }
}
