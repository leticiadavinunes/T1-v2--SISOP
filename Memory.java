public class Memory {
    // Traduz endereço lógico para físico usando tabela de páginas
    public int traduzEndereco(int enderecoLogico, int[] tabelaPaginas) {
        int pagina = enderecoLogico / tamPg;
        int deslocamento = enderecoLogico % tamPg;
        if (pagina < 0 || pagina >= tabelaPaginas.length) {
            throw new IllegalArgumentException("Página inválida");
        }
        int frame = tabelaPaginas[pagina];
        if (frame == -1) {
            throw new IllegalStateException("Página não mapeada");
        }
        return frame * tamPg + deslocamento;
    }

    // Carga paginada: copia cada página do programa para o frame físico
    // correspondente
    public void carregarProgramaPaginado(Word[] programa, int[] tabelaPaginas) {
        int numPaginas = tabelaPaginas.length;
        for (int i = 0; i < numPaginas; i++) {
            int frame = tabelaPaginas[i];
            int inicioFrame = getFrameStart(frame);
            int inicioPagina = i * tamPg;
            for (int j = 0; j < tamPg && (inicioPagina + j) < programa.length; j++) {
                pos[inicioFrame + j] = programa[inicioPagina + j];
            }
        }
    }

    // Retorna o índice inicial do frame
    public int getFrameStart(int frameIndex) {
        return frameIndex * tamPg;
    }

    // Retorna o índice final do frame
    public int getFrameEnd(int frameIndex) {
        return (frameIndex + 1) * tamPg - 1;
    }

    public Word[] pos;
    public int tamPg; // tamanho da página/frame
    public int numFrames; // número de frames
    public boolean[] frameLivre; // indica se o frame está livre
    public PageTable pageTable; // tabela de páginas

    public Memory(int size, int tamPg) {
        this.tamPg = tamPg;
        this.numFrames = size / tamPg;
        this.frameLivre = new boolean[numFrames];
        for (int i = 0; i < numFrames; i++) {
            frameLivre[i] = true;
        }
        pos = new Word[size];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = new Word(Opcode.___, -1, -1, -1);
        }
        this.pageTable = new PageTable(numFrames); // pode ser ajustado conforme o número de páginas do processo
    }
}
