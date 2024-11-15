package common;

public class Spielzug {

    private final int x;
    private final int y;
    private final Spieler spieler;

    public Spielzug(int x, int y, Spieler spieler) {
        this.x = x;
        this.y = y;
        this.spieler = spieler;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Spieler getSpieler() {
        return this.spieler;
    }

    @Override
    public String toString() {
        return "{x: " + x + ", y: " + y + ", spieler: " + spieler + "}";
    }
}
