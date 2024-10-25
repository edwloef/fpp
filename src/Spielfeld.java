import java.util.ArrayList;

public class Spielfeld {

    private int x_size;
    private int y_size;
    private ArrayList<Spielstein> spielsteine;
    private String[][] belegung;

    private static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    public Spielfeld(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
        this.spielsteine = new ArrayList<>();
        this.belegung = new String[y_size][y_size];
    }

    public int getXSize() {
        return this.x_size;
    }

    public int getYSize() {
        return this.y_size;
    }

    public boolean getBelegung(int x, int y) {
        if (x < 0 || y < 0 || x >= this.x_size || y >= this.y_size) {
            return false;
        }
        return !(this.belegung[x][y] == null);
    }

    public boolean getBelegungTyp(String typ, int x, int y) {
        if (this.getBelegung(x, y) == false) {
            return false;
        }
        return this.belegung[x][y].equals(typ);
    }

    public void addSpielstein(String typ, int x, int y) {
        Spielstein spielstein = new Spielstein(typ, x, y);
        this.spielsteine.add(spielstein);
        this.belegung[x][y] = typ;
    }

    public void removeSpielstein(String typ, int x, int y) {
        int i;
        for (i = 0; i < this.spielsteine.size(); i++) {
            if (this.spielsteine.get(i).getTyp().equals(typ)) {
                break;
            }
        }
        this.spielsteine.remove(i);
        this.belegung[x][y] = null;
    }

    public void replaceSpielstein(Spielstein spielstein) {
        int i;
        for (i = 0; i < this.spielsteine.size(); i++) {
            if (this.spielsteine.get(i).equals(spielstein)) {
                break;
            }
        }
        this.spielsteine.set(i, spielstein);
        this.belegung[spielstein.getX()][spielstein.getY()] =
            spielstein.getTyp();
    }

    public void draw() {
        System.out.print(ANSI_CLEAR);

        for (int i = 0; i < this.y_size; i++) {
            for (int j = 0; j < 2 * this.x_size + 1; j++) {
                System.out.print("-");
            }
            System.out.println();

            for (int j = 0; j < this.x_size; j++) {
                System.out.print("|");

                String stein = this.belegung[i][j];
                if (stein == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(stein);
                }
            }
            System.out.print("|");

            System.out.println();
        }

        for (int j = 0; j < this.x_size; j++) {
            System.out.print("-" + (j + 1));
        }
        System.out.println("-");
    }
}
