public class Spielzug {
    private int x;
    private int y;
    private String spieler;

    public Spielzug(int x, int y, String spieler) {
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

    public String getSpieler() {
        return spieler;
    }
}
