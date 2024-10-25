public class Spielzug {

    private int x;
    private int y;
    private Spieler spieler;

    public Spielzug(int x, int y, Spieler spieler) {
        this.x = x;
        this.y = y;
        this.spieler = spieler;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Spieler getSpieler() {
        return spieler;
    }
}
