package common;

public abstract class Spiel {

    protected Spieler[] spieler;
    protected Spielfeld spielfeld;

    public Spiel(Spieler[] spieler, Spielfeld spielfeld) {
        this.spieler = spieler;
        this.spielfeld = spielfeld;
    }

    public boolean durchgang() {
        for (Spieler spiela : this.spieler) {
            this.spielfeld.draw();

            if (!this.spielzug(spiela)) {
                this.spielfeld.draw();
                System.out.println("Gewonnen " + spiela.getName());
                return false;
            }

            if (this.spielfeld.unentschieden()) {
                this.spielfeld.draw();
                System.out.println("Unentschieden");
                return false;
            }
        }
        return true;
    }

    protected abstract boolean spielzug(Spieler spieler);
}
