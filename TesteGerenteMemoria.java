public class TesteGerenteMemoria {
    public static void main(String[] args) {
        // ====================================================
        // 1.1 Teste de valores básicos (frames e endereços)
        // ====================================================
        int tamMem = 1024;
        int tamPg = 16; // tamanho de página
        Memory mem = new Memory(tamMem, tamPg);

        System.out.println("Frames esperados: 64 | Frames calculados: " + mem.numFrames);
        System.out.println("Frame 0: inicio=" + mem.getFrameStart(0) + ", fim=" + mem.getFrameEnd(0));
        System.out.println("Frame 1: inicio=" + mem.getFrameStart(1) + ", fim=" + mem.getFrameEnd(1));
        System.out.println("Frame 62: inicio=" + mem.getFrameStart(62) + ", fim=" + mem.getFrameEnd(62));
        System.out.println("Frame 63: inicio=" + mem.getFrameStart(63) + ", fim=" + mem.getFrameEnd(63));

        // ====================================================
        // 1.2 Teste de alocação e desalocação
        // ====================================================
        MemoryManager gm = new MemoryManager(mem);

        // =============================
        // Cenário 1: consegue alocar
        // =============================
        int nroPalavras1 = 40; // precisa de 3 páginas (16 palavras cada)
        int numPaginas1 = (int) Math.ceil((double) nroPalavras1 / tamPg);
        int[] tabelaPaginas1 = new int[numPaginas1];

        boolean alocou1 = gm.aloca(nroPalavras1, tabelaPaginas1);
        System.out.println("\nCenário 1 - Alocou 40 palavras? " + alocou1);
        System.out.print("Frames alocados: ");
        for (int f : tabelaPaginas1) System.out.print(f + " ");
        System.out.println();

        // Desaloca para liberar memória
        gm.desaloca(tabelaPaginas1);

        // =============================
        // Cenário 2: não consegue alocar (memória insuficiente)
        // =============================
        int nroPalavras2 = tamMem + 10; // maior que a memória total
        int numPaginas2 = (int) Math.ceil((double) nroPalavras2 / tamPg);
        int[] tabelaPaginas2 = new int[numPaginas2];

        boolean alocou2 = gm.aloca(nroPalavras2, tabelaPaginas2);
        System.out.println("\nCenário 2 - Alocou " + nroPalavras2 + " palavras? " + alocou2);

        // =============================
        // Cenário 3: aloca parcialmente, depois tenta alocar demais
        // =============================
        int nroPalavras3a = 500; // consegue
        int numPaginas3a = (int) Math.ceil((double) nroPalavras3a / tamPg);
        int[] tabelaPaginas3a = new int[numPaginas3a];
        boolean alocou3a = gm.aloca(nroPalavras3a, tabelaPaginas3a);
        System.out.println("\nCenário 3a - Alocou 500 palavras? " + alocou3a);

        int nroPalavras3b = 600; // agora não vai caber
        int numPaginas3b = (int) Math.ceil((double) nroPalavras3b / tamPg);
        int[] tabelaPaginas3b = new int[numPaginas3b];
        boolean alocou3b = gm.aloca(nroPalavras3b, tabelaPaginas3b);
        System.out.println("Cenário 3b - Alocou 600 palavras com memória parcialmente ocupada? " + alocou3b);

        // Limpeza final
        gm.desaloca(tabelaPaginas3a);

        // ====================================================
        // 1.3 Teste de carga paginada
        // ====================================================
        int nroPalavras = 40;
        int numPaginas = (int) Math.ceil((double) nroPalavras / tamPg);
        int[] tabelaPaginas = new int[numPaginas];
        gm.aloca(nroPalavras, tabelaPaginas);

        Word[] programa = new Word[nroPalavras];
        for (int i = 0; i < nroPalavras; i++) {
            programa[i] = new Word(Opcode.DATA, -1, -1, i + 100);
        }

        mem.carregarProgramaPaginado(programa, tabelaPaginas);

        System.out.println("\nConteúdo dos frames alocados:");
        for (int p = 0; p < tabelaPaginas.length; p++) {
            int frame = tabelaPaginas[p];
            int inicio = mem.getFrameStart(frame);
            System.out.print("Frame " + frame + ": ");
            for (int j = 0; j < tamPg && (p * tamPg + j) < programa.length; j++) {
                System.out.print(mem.pos[inicio + j].p + " ");
            }
            System.out.println();
        }

        // ====================================================
        // 1.4 Teste de tradução de endereço
        // ====================================================
        int enderecoLogico = 25;
        int enderecoFisico = mem.traduzEndereco(enderecoLogico, tabelaPaginas);
        System.out.println("\nEndereço lógico " + enderecoLogico + " -> físico " + enderecoFisico);

        enderecoLogico = nroPalavras - 1;
        enderecoFisico = mem.traduzEndereco(enderecoLogico, tabelaPaginas);
        System.out.println("Endereço lógico " + enderecoLogico + " -> físico " + enderecoFisico);

        gm.desaloca(tabelaPaginas);
    }
}
