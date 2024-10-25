import java.util.Scanner;

//x = Zeile
//y = Spalte

public class Viergewinnt extends Spiel implements Protokollierbar {

    Scanner sc = new Scanner(System.in);
    boolean spielstein_typ = false;

    private static final String ANSI_RED = "\033[31m"; // red output color
    private static final String ANSI_BLUE = "\033[34m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color

    private static final int[][] RICHTUNGSARRAY = {
        { -1, -1 }, // oben links
        { 0, -1 }, // links
        { 1, -1 }, // unten links
        { 1, 0 }, // unten
        { 1, 1 }, // unten rechts
        { 0, 1 }, // rechts
        { -1, 1 }, // oben rechts
    };

    public Viergewinnt(Spieler spieler_1, Spieler spieler_2) {
        super(new Spieler[] { spieler_1, spieler_2 }, new Spielfeld(9, 9));
    }

    private int getZeile(int y) {
        int x = super.spielfeld.getXSize() - 1;

        while (x >= 0) {
            if (!super.spielfeld.getBelegung(x, y)) {
                return x;
            }
            x -= 1;
        }
        return x;
    }

    private String getFarbe() {
        if (this.spielstein_typ) {
            return ANSI_RED + "◉" + ANSI_RESET;
        } else {
            return ANSI_BLUE + "◉" + ANSI_RESET;
        }
    }

    private boolean pruefeVier(int x, int y, int a, int b) {
        for (int i = 1; i < 4; i++) {
            if (
                super.spielfeld.getBelegungTyp(
                    this.getFarbe(),
                    x + a * i,
                    y + b * i
                )
            ) {
                if (i == 3) {
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }

    protected boolean pruefeWeiterspielen(int x, int y) {
        for (int[] richtung : RICHTUNGSARRAY) {
            if (!this.pruefeVier(x, y, richtung[0], richtung[1])) {
                return false;
            }
            System.out.println();
        }
        return true;
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
            this.spielstein_typ = this.spielstein_typ ^ true;
            System.out.print(
                "Bitte wähle deine Eingabespalte " + spieler.getName() + ": "
            );
            int y = this.sc.nextInt() - 1;
            int x = this.getZeile(y);
            if (
                x >= 0 &&
                x < super.spielfeld.getXSize() &&
                y >= 0 &&
                y < super.spielfeld.getYSize()
            ) {
                if (!super.spielfeld.getBelegung(x, y)) {
                    super.spielfeld.addSpielstein(getFarbe(), x, y);
                    Spielzug spielzug = new Spielzug(x, y, spieler);
                    this.protokolliere(spielzug);
                    return this.pruefeWeiterspielen(x, y);
                } else {
                    System.out.println("Error: Feld ist schon belegt.");
                }
            } else {
                System.out.println(
                    "Error: feld außerhalb des Spielfeldes gewählt."
                );
            }
        }
    }
}
