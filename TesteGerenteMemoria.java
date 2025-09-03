public class TesteGerenteMemoria {
    public static void main(String[] args) {
        // =============================
        // 1.1 Teste de valores básicos
        // =============================
        int tamMem = 1024;
        int tamPg = 16; // de acordo com o exemplo do enunciado
        Memory mem = new Memory(tamMem, tamPg);

        System.out.println("Frames esperados: 64 | Frames calculados: " + mem.numFrames);
        System.out.println("Frame 0: inicio=" + mem.getFrameStart(0) + ", fim=" + mem.getFrameEnd(0));
        System.out.println("Frame 1: inicio=" + mem.getFrameStart(1) + ", fim=" + mem.getFrameEnd(1));
        System.out.println("Frame 62: inicio=" + mem.getFrameStart(62) + ", fim=" + mem.getFrameEnd(62));
        System.out.println("Frame 63: inicio=" + mem.getFrameStart(63) + ", fim=" + mem.getFrameEnd(63));

        // =============================
        // 1.2 Teste de alocação e desalocação
        // =============================
        MemoryManager gm = new MemoryManager(mem);

        int nroPalavras = 40; // exemplo: programa com 40 palavras
        int numPaginas = (int) Math.ceil((double) nroPalavras / tamPg);
        int[] tabelaPaginas = new int[numPaginas];

        boolean alocou = gm.aloca(nroPalavras, tabelaPaginas);
        System.out.println("\nAlocou? " + alocou);
        System.out.print("Frames alocados: ");
        for (int f : tabelaPaginas) {
            System.out.print(f + " ");
        }
        System.out.println();

        // Marca frames como ocupados
        for (int f : tabelaPaginas) {
            System.out.println("Frame " + f + " ocupado? " + !mem.frameLivre[f]);
        }

        // Desaloca
        gm.desaloca(tabelaPaginas);
        for (int f : tabelaPaginas) {
            System.out.println("Frame " + f + " livre? " + mem.frameLivre[f]);
        }

        // =============================
        // 1.3 Teste de carga paginada
        // =============================
        Word[] programa = new Word[nroPalavras];
        for (int i = 0; i < nroPalavras; i++) {
            programa[i] = new Word(Opcode.DATA, -1, -1, i + 100); // só para teste
        }

        gm.aloca(nroPalavras, tabelaPaginas);
        mem.carregarProgramaPaginado(programa, tabelaPaginas);

        // Verifica se os dados foram carregados corretamente
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
    }
}
