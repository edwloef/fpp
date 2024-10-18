public class Spieler {
    private String name;
    private Spielerart art;

    public Spieler(String name, Spielerart art) {
        this.name = name;
        this.art = art;
    }

    public String getName() {
        return name;
    }

    public Spielerart getArt() {
        return art;
    }
}
