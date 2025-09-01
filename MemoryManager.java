public class MemoryManager implements GM {
    private Memory memory;

    public MemoryManager(Memory memory) {
        this.memory = memory;
    }

    @Override
    public boolean aloca(int nroPalavras, int[] tabelaPaginas) {
        int tamPg = memory.tamPg;
        int numPaginas = (int) Math.ceil((double) nroPalavras / tamPg);
        int[] framesAlocados = new int[numPaginas];
        int count = 0;

        for (int i = 0; i < memory.numFrames && count < numPaginas; i++) {
            if (memory.frameLivre[i]) {
                framesAlocados[count] = i;
                memory.frameLivre[i] = false;
                count++;
            }
        }

        if (count < numPaginas) {
            for (int i = 0; i < count; i++) {
                memory.frameLivre[framesAlocados[i]] = true;
            }
            return false;
        }

        for (int i = 0; i < numPaginas; i++) {
            tabelaPaginas[i] = framesAlocados[i];
        }
        return true;
    }

    @Override
    public void desaloca(int[] tabelaPaginas) {
        for (int frame : tabelaPaginas) {
            if (frame >= 0 && frame < memory.numFrames) {
                memory.frameLivre[frame] = true;
            }
        }
    }

    public int traduzEndereco(int enderecoLogico, int[] tabelaPaginas) {
        int pagina = enderecoLogico / memory.tamPg;
        int deslocamento = enderecoLogico % memory.tamPg;
        int frame = tabelaPaginas[pagina];
        return frame * memory.tamPg + deslocamento;
    }
}
