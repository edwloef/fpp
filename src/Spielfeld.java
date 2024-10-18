import java.util.ArrayList;

public class Spielfeld {
    private int x_size;
    private int y_size;
    private ArrayList<Spielstein> spielsteine;

    public Spielfeld(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
        this.spielsteine = new ArrayList<>();
    }

    public int getXSize() {
        return x_size;
    }

    public int getYSize() {
        return y_size;
    }

    public void addSpielstein(Spielstein spielstein) {
        spielsteine.add(spielstein);
    }

    public void removeSpielstein(Spielstein spielstein) {
        for (int i = 0; i < spielsteine.size(); i++) {
            if (spielsteine.get(i).equals(spielstein)) {
                spielsteine.remove(i);
                return;
            }
        }
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
