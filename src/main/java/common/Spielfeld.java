package common;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Spielfeld {

    protected static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen
    protected final Spielstein[][] spielsteine;

    public Spielfeld() {
        Scanner sc = new Scanner(System.in);
        int ySize;
        int xSize;
        while (true) {
            while (true) {
                try {
                    System.out.print("Breite des Spielfelds: ");
                    ySize = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    sc.next();
                }
            }

            while (true) {
                try {
                    System.out.print("Höhe des Spielfelds: ");
                    xSize = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    sc.next();
                }
            }

            if (xSize > 0 && ySize > 0 && (xSize > 1 || ySize > 1)) {
                break;
            }

            System.out.println("Keine valide Spielfeldgröße");
        }

        this.spielsteine = new Spielstein[xSize][ySize];
    }

    public int getXSize() {
        return this.spielsteine.length;
    }

    public int getYSize() {
        return this.spielsteine[0].length;
    }

    /**
     * Gibt true zurück, wenn das Feld belegt ist.
     */
    public boolean prüfeBelegt(int x, int y) {
        if (x < 0 || y < 0 || x >= this.getXSize() || y >= this.getYSize()) {
            return false;
        }

        return this.spielsteine[x][y] != null;
    }

    /**
     * Gibt true zurück, wenn das Feld mit einem Stein des angegebenen Spielers belegt ist.
     */
    public boolean prüfeBelegtSpieler(int x, int y, Spieler spieler) {
        if (!this.prüfeBelegt(x, y)) {
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
