package spiele.common;

public abstract class Spiel {

    protected final Spieler[] spieler;
    protected final Spielfeld spielfeld;

    public Spiel(Spieler[] spieler, Spielfeld spielfeld) {
        this.spieler = spieler;
        this.spielfeld = spielfeld;
    }

    /**
     * Gibt false zurück, wenn das Spiel vorbei ist.
     */
    public boolean durchgang() {
        for (Spieler s : this.spieler) {
            this.spielfeld.draw();

            if (!this.spielzug(s)) {
                this.spielfeld.draw();
                System.out.println("Gewonnen " + s.getName());
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

    /**
     * Gibt false zurück, wenn der spielende Spieler gewonnen hat.
     */
    protected boolean spielzug(Spieler spieler) {
        if (spieler.getArt() == Spielerart.Mensch) {
            return this.spielzugMensch(spieler);
        } else if (spieler.getArt() == Spielerart.Computer) {
            return this.spielzugComputer(spieler);
        } else {
            return false;
        }
    }

    protected abstract boolean spielzugMensch(Spieler spieler);

    protected abstract boolean spielzugComputer(Spieler spieler);
}
