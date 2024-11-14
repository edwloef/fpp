package common;

public class Spieler {

    private final String name;
    private final Spielerart art;
    private final String token;

    public Spieler(String name, Spielerart art, String token) {
        this.name = name;
        this.art = art;
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public Spielerart getArt() {
        return this.art;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return (
            "{name: " +
            this.name +
            ", art: " +
            this.art +
            ", token: " +
            this.token +
            "}"
        );
    }
}
