import java.util.Scanner;

//x = Zeile
//y = Spalte

public class Viergewinnt extends Spiel implements Protokollierbar {

    Scanner sc = new Scanner(System.in);
    boolean spielstein_typ = false;

    private static final int[][] richtungsArray = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 },
            { 1, -1 },
            { -1, 1 } };

    public Viergewinnt(Spieler spieler_1, Spieler spieler_2) {
        super(new Spieler[] { spieler_1, spieler_2 }, new Spielfeld(9, 9));
    }

    private int getZeile(int y) {
        int x = 0;
        while (x < spielfeld.getXSize()) {
            if (spielfeld.getBelegung(x, y)) {
                return x;
            }
            x += 1;
        }
        return x;
    }

    private String getFarbe(boolean spielstein_typ) {
        if (spielstein_typ) {
            return "Farbe_1";
        } else {
            return "Farbe_2";
        }
    }

    private boolean pruefeVier(int x, int y, int a, int b) {
        for (int i = 1; i < 4; i++) {
            if (this.spielfeld.getBelegung(x + a * i, y + b * i)) {
                if (i == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    protected  boolean pruefeEnde(int x, int y) {
        for (int[] richtung : Viergewinnt.richtungsArray) {
            if (pruefeVier(x, y, richtung[0], richtung[1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void protokolliere(Spielzug spielzug) {
        protokoll.push(spielzug);
    }

    public void entferne() {
        protokoll.pop();
    }

    @Override
    public boolean spielzug(Spieler spieler) {
        while (true) {
            System.out.println("Bitte wähle deiner Eingabespalte "+ spieler.getName() + " :");
            int y = sc.nextInt();
            int x = getZeile(y);
            if (x > 0 && x <= spielfeld.getXSize() && y > 0 && y <= spielfeld.getYSize()) {
                if (!spielfeld.getBelegung(x - 1, y - 1)) {
                    spielfeld.addSpielstein(getFarbe(spielstein_typ), x - 1, y - 1);
                    spielstein_typ = spielstein_typ ^ true;
                    Spielzug spielzug = new Spielzug(x, y, spieler);
                    protokolliere(spielzug);
                    return pruefeEnde(x, y);
                } else {
                    System.out.println("Error: Feld ist schon belegt.");
                }
            } else {
                System.out.println("Error: feld außerhalb des Spielfeldes gewählt.");
            }
        }

    }

}