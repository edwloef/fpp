package common;

public class Spieler {

    private String name;
    private Spielerart art;

    public Spieler(String name, Spielerart art) {
        this.name = name;
        this.art = art;
    }

    public String getName() {
        return this.name;
    }

    public Spielerart getArt() {
        return this.art;
    }

    @Override
    public String toString() {
        return "{name: " + this.name + ", art: " + this.art + "}";
    }
}
