public class Programs {
    public Program[] progs;

    public Programs() {
        progs = new Program[] {}; // Adapte conforme necessário
    }

    public Word[] retrieveProgram(String pname) {
        for (Program p : progs) {
            if (p != null && p.name.equals(pname))
                return p.image;
        }
        return null;
    }
}
