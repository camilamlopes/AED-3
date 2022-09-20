
public class Livro {
    protected int id;
    protected String name;
    protected int pages;

    public Livro() {

    }

    public Livro(int id, String name, int pages) {
        this.id = id;
        this.name = name;
        this.pages = pages;
    }

    public String toString() {
        return "id: " + this.id + "| name: " + this.name + " | pages: " + this.pages;
    }

   
}
