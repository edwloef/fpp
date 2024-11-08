package common;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Spielfeld {

    protected Spielstein[][] spielsteine;

    protected static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    private Scanner sc = new Scanner(System.in);

    public Spielfeld() {
        int y_size;
        while (true) {
            try {
                System.out.print("Breite des Spielfelds: ");
                y_size = this.sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Bitte gib eine Zahl ein!");
                this.sc.next();
            }
        }

        int x_size;
        while (true) {
            try {
                System.out.print("Höhe des Spielfelds: ");
                x_size = this.sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Bitte gib eine Zahl ein!");
                this.sc.next();
            }
        }

        this.spielsteine = new Spielstein[x_size][y_size];
    }

    /**
     * erstellt eine Kopie des eingegebenen Spielfeldes
     */
    protected Spielfeld(Spielfeld orig) {
        this.spielsteine = new Spielstein[orig.getXSize()][orig.getYSize()];

        for (int x = 0; x < this.getXSize(); x++) {
            for (int y = 0; y < this.getYSize(); y++) {
                this.spielsteine[x][y] = orig.spielsteine[x][y];
            }
        }
    }

    public int getXSize() {
        return this.spielsteine.length;
    }

    public int getYSize() {
        return this.spielsteine[0].length;
    }

    /**
     *   Gibt true zurück, wenn das Feld belegt ist.
     */
    public boolean pruefeBelegt(int x, int y) {
        if (x < 0 || y < 0 || x >= this.getXSize() || y >= this.getYSize()) {
            return false;
        }

        return this.spielsteine[x][y] != null;
    }

    /**
     *   Gibt true zurück, wenn das Feld mit einem Stein des angegebenen Spielers belegt ist.
     */
    public boolean pruefeBelegtSpieler(int x, int y, Spieler spieler) {
        if (!this.pruefeBelegt(x, y)) {
            return false;
        }

        return this.spielsteine[x][y].getToken().equals(spieler.getToken());
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
