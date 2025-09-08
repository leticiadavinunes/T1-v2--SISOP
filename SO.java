public class SO {
    public InterruptHandling ih;
    public SysCallHandling sc;
    public Utilities utils;
    public ProcessManager processManager;
    public MemoryManager memoryManager;
    public Programs programs;
    private HW hw;

    public SO(HW hw) {
        this.hw = hw;

        // Inicializa componentes básicos
        ih = new InterruptHandling(hw);
        sc = new SysCallHandling(hw);
        hw.cpu.setAddressOfHandlers(ih, sc);
        utils = new Utilities(hw);

        // Inicializa gerenciamento de memória e processos
        memoryManager = new MemoryManager(hw.mem);
        programs = new Programs();
        processManager = new ProcessManager(hw, memoryManager, programs);

        // Conecta os handlers com o process manager
        ih.setProcessManager(processManager);
        sc.setProcessManager(processManager);

        System.out.println("Sistema Operacional inicializado com escalonamento de processos");
    }

    // Comando para carregar um programa
    public boolean loadProgram(String programName) {
        return processManager.loadProgram(programName);
    }

    // Comando execAll - executa todos os processos de forma escalonada
    public void execAll() {
        processManager.execAll();
    }

    // Inicia execução contínua
    public void startContinuousExecution() {
        processManager.startContinuousExecution();
    }

    // Para execução contínua
    public void stopContinuousExecution() {
        processManager.stopContinuousExecution();
    }

    // Mostra status do sistema
    public void showSystemStatus() {
        System.out.println("=== Status do Sistema Operacional ===");
        processManager.showStatus();

        // Mostra status da memória
        System.out.println("=== Status da Memória ===");
        int framesLivres = 0;
        for (boolean livre : hw.mem.frameLivre) {
            if (livre)
                framesLivres++;
        }
        System.out.println("Frames livres: " + framesLivres + "/" + hw.mem.numFrames);
        System.out.println("Tamanho da página: " + hw.mem.tamPg);
        System.out.println("=====================================");
    }

    // Interface de linha de comando simples
    public void commandInterface() {
        System.out.println("=== Interface do Sistema Operacional ===");
        System.out.println("Comandos disponíveis:");
        System.out.println("- loadProgram <nome>: Carrega um programa");
        System.out.println("- execAll: Executa todos os processos escalonados");
        System.out.println("- startContinuous: Inicia execução contínua");
        System.out.println("- stopContinuous: Para execução contínua");
        System.out.println("- status: Mostra status do sistema");
        System.out.println("- help: Mostra esta ajuda");
        System.out.println("========================================");
    }
}
