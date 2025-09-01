public class PageTable {
    private int[] pageToFrame;
    private int numPages;

    public PageTable(int numPages) {
        this.numPages = numPages;
        this.pageToFrame = new int[numPages];
        for (int i = 0; i < numPages; i++) {
            pageToFrame[i] = -1; // -1 indica página não mapeada
        }
    }

    public void setFrame(int page, int frame) {
        pageToFrame[page] = frame;
    }

    public int getFrame(int page) {
        return pageToFrame[page];
    }

    public boolean isMapped(int page) {
        return pageToFrame[page] != -1;
    }
}
