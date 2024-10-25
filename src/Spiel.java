public abstract class Spiel {
    protected Spieler[] spieler;
    protected Spielfeld spielfeld;

    public Spiel(Spieler[] spieler, Spielfeld spielfeld) {
        this.spieler = spieler;
        this.spielfeld = spielfeld;
    }

    public void durchgang() {
        for (Spieler spiela : spieler) {
            spielzug(spiela);
        }
    };

    protected abstract boolean spielzug(Spieler spieler);
}
