import java.util.ArrayList;

public class Spielfeld {
    private int x_size;
    private int y_size;
    private ArrayList<Spielstein> spielsteine;
    private String[][] belegung;

    public Spielfeld(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
        this.spielsteine = new ArrayList<>();
        this.belegung = new String[y_size][y_size];
    }

    public int getXSize() {
        return x_size;
    }

    public int getYSize() {
        return y_size;
    }

    public boolean getBelegung(int x, int y) {
        if (x < 0 || y < 0 || x >= this.x_size || y >= this.y_size) {
            return false;
        }
        return !(belegung[x][y].isEmpty());
    }

    public void addSpielstein(String typ, int x, int y) {
        Spielstein spielstein = new Spielstein(typ, x, y);
        spielsteine.add(spielstein);
        belegung[x][y] = typ;
    }

    public void removeSpielstein(String typ, int x, int y) {
        for (int i = 0; i < spielsteine.size(); i++) {
            if (spielsteine.get(i).getTyp().equals(typ)) {
                spielsteine.remove(i);
                return;
            }
        }
        belegung[x][y] = typ;
    }

    public void replaceSpielstein(Spielstein spielstein) {
        for (int i = 0; i < spielsteine.size(); i++) {
            if (spielsteine.get(i).equals(spielstein)) {
                spielsteine.set(i, spielstein);
                return;
            }
        }
    }

    public void draw() {
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < 2 * x_size + 1; j++) {
                System.out.print("-");
            }
            System.out.println();

            for (int j = 0; j < x_size; j++) {
                System.out.print("|");

                Spielstein stein = null;
                for (Spielstein spielstein : spielsteine) {
                    if (spielstein.getX() == i && spielstein.getY() == j) {
                        stein = spielstein;
                        break;
                    }
                }

                if (stein == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(stein.getTyp());
                }
            }

            System.out.print("|");
        }

        for (int j = 0; j < 2 * x_size + 1; j++) {
            System.out.print("-");
        }
        System.out.println();
    };
}
