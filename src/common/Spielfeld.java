package common;

public abstract class Spielfeld {

    protected Spielstein[][] spielsteine;

    protected static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    public Spielfeld(int x_size, int y_size) {
        this.spielsteine = new Spielstein[x_size][y_size];
    }

    public int getXSize() {
        return this.spielsteine.length;
    }

    public int getYSize() {
        return this.spielsteine[0].length;
    }

    public boolean prüfeBelegt(int x, int y) {
        if (x < 0 || y < 0 || x >= this.getXSize() || y >= this.getYSize()) {
            return false;
        }

        return this.spielsteine[x][y] != null;
    }

    public boolean prüfeBelegtTyp(String typ, int x, int y) {
        if (!this.prüfeBelegt(x, y)) {
            return false;
        }

        return this.spielsteine[x][y].getTyp().equals(typ);
    }

    public void setSpielstein(Spielstein spielstein) {
        this.spielsteine[spielstein.getX()][spielstein.getY()] = spielstein;
    }

    public void removeSpielstein(int x, int y) {
        this.spielsteine[x][y] = null;
    }

    public abstract void draw();

    public abstract boolean unentschieden();
}
