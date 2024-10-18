public abstract class Spiel {
    protected Spieler[] spieler;
    protected Spielfeld spielfeld;

    public Spiel(Spieler[] spieler, Spielfeld spielfeld) {
        this.spieler = spieler;
        this.spielfeld = spielfeld;
    }

    public void durchgang() {
        for (Spieler spieler : spieler) {
            spielzug(spieler);
        }
    };

    protected abstract void spielzug(Spieler spieler);
}
