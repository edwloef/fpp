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
