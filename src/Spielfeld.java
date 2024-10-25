public class Spielfeld {

    public Spielstein[][] spielsteine;

    private static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    public Spielfeld(int x_size, int y_size) {
        this.spielsteine = new Spielstein[y_size][x_size];
    }

    public int getXSize() {
        return this.spielsteine.length;
    }

    public int getYSize() {
        return this.spielsteine[0].length;
    }

    public boolean getBelegung(int x, int y) {
        if (x < 0 || y < 0 || x >= this.getXSize() || y >= this.getYSize()) {
            return false;
        }

        return !(this.spielsteine[x][y] == null);
    }

    public boolean getBelegungTyp(String typ, int x, int y) {
        if (this.getBelegung(x, y) == false) {
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

    public void draw() {
        System.out.print(ANSI_CLEAR);

        for (int i = 0; i < this.getXSize(); i++) {
            for (int j = 0; j < 2 * this.getYSize() + 1; j++) {
                System.out.print("-");
            }
            System.out.println();

            for (int j = 0; j < this.getYSize(); j++) {
                System.out.print("|");

                Spielstein stein = this.spielsteine[i][j];
                if (stein == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(stein.getTyp());
                }
            }
            System.out.print("|");

            System.out.println();
        }

        for (int j = 0; j < this.getYSize(); j++) {
            System.out.print("-" + (j + 1));
        }
        System.out.println("-");
    }
}
