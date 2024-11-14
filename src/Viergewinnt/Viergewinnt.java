package Viergewinnt;

import common.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Viergewinnt extends Spiel implements Protokollierbar {

    private Scanner sc = new Scanner(System.in);

    private static final int[][] RICHTUNGSARRAY = {
            { 1, -1 }, // unten links -> oben rechts
            { 1, 0 }, // unten -> oben
            { 1, 1 }, // unten rechts -> oben links
            { 0, 1 }, // rechts -> links
    };

    public Viergewinnt(Spieler spieler_1, Spieler spieler_2) {
        super(
                new Spieler[] { spieler_1, spieler_2 },
                new ViergewinntSpielfeld());
    }

    /**
     * simuliert die Gravition die bei 4Gewinnt wichtig ist
     *
     * @param y die Spalte
     * @return die Zeile in die der Token fallt durch gravitation
     */
    private int getZeile(int y) {
        int x = super.spielfeld.getXSize() - 1;

        while (x >= 0) {
            if (!super.spielfeld.pruefeBelegt(x, y)) {
                return x;
            }
            x -= 1;
        }
        return x;
    }

    /**
     *
     * @param x       Xkord
     * @param y       Ykord
     * @param spieler
     * @return das gewicht mit welcher man bei den ubergebebnen Kords einen sieg hat
     */
    private int checkProbebility(int x, int y, Spieler spieler) {
        int probebility = 0;
        for (int[] richtung : RICHTUNGSARRAY) {
            int a = richtung[0];
            int b = richtung[1];
            int counterFree = 0; // counter fur die freien Platze
            int counterPasst = 0; // counter fur die gleie farbe belegten Platze
            for (int i = -3; i <= -1; i++) {
                if ( /** pruft ob */
                super.spielfeld.pruefeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler)) {
                    counterPasst++;
                } else if ( /** pruft ob belegt ist oder nicht */
                !super.spielfeld.pruefeBelegt(
                        (x + a * i),
                        (y + b * i))) {
                    if (!((x + a * i) < 0 || (y + b * i) < 0 || (x + a * i) >= spielfeld.getXSize()
                            || (y + b * i) >= spielfeld.getYSize())) {
                        counterFree++;
                    }
                } else {
                    counterPasst = 0;
                    counterFree = 0;
                }
            }
            for (int i = 1; i < 4; i++) {
                if ( /** pruft ob mit gleicher farbe belegt */
                super.spielfeld.pruefeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler)) {
                    counterPasst++;
                } else if ( /** pruft ob belegt ist oder nicht */
                !super.spielfeld.pruefeBelegt(
                        (x + a * i),
                        (y + b * i))) {
                    if (!((x + a * i) < 0 || (y + b * i) < 0 || (x + a * i) >= spielfeld.getXSize()
                            || (y + b * i) >= spielfeld.getYSize())) {
                        counterFree++;
                    }
                } else {
                    break;
                }
            }
            probebility = probebility + counterFree + counterPasst * 10;
        }
        return probebility;
    }

    /**
     *
     * @param x       Xkord
     * @param y       Ykord
     * @param spieler = der momentane Spieler (KI)
     * @return True= der gegner gewint mit dem nachsten Zug
     */
    private boolean checkGefahr(int x, int y, Spieler spieler) {
        Spieler spielergegen;
        if (spieler.equals(super.spieler[1])) {
            spielergegen = super.spieler[0];
        } else {
            spielergegen = super.spieler[1];
        }
        return !this.pruefeWeiterspielen(x, y, spielergegen);
    }

    /**
     * Prüft, ob es vier zusammenhängende Felder des angegebenen Spielers an der
     * Stelle (x,y) in der Richtung (a, b) gibt.
     */
    /**
     *
     * @param x
     * @param y
     * @param a
     * @param b
     * @param spieler
     * @return True = es sind vier in der reihe
     */
    private boolean pruefeVier(int x, int y, int a, int b, Spieler spieler) {
        int counter = 0;

        for (int i = -3; i < 4; i++) {
            if (i == 0 || super.spielfeld.pruefeBelegtSpieler(
                    x + a * i,
                    y + b * i,
                    spieler)) {
                if (counter == 3) {
                    return true;
                }
                counter++;
            } else {
                counter = 0;
            }
        }
        return false;
    }

    /**
     *
     * @param x       xKord
     * @param y       yKord
     * @param spieler
     * @return True = es wird weiter gespielt
     */
    private boolean pruefeWeiterspielen(int x, int y, Spieler spieler) {
        for (int[] richtung : RICHTUNGSARRAY) {
            if (this.pruefeVier(x, y, richtung[0], richtung[1], spieler)) {
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

    @Override
    public void entferne() {
        protokoll.pop();
    }

    @Override
    public boolean spielzugMensch(Spieler spieler) {
        while (true) {
            int y;
            while (true) {
                try {
                    System.out.print(
                            "Bitte wähle deine Eingabespalte " +
                                    spieler.getName() +
                                    ": ");
                    y = this.sc.nextInt() - 1;
                    if (y < 0 || y > (super.spielfeld.getYSize() - 1)) {
                        System.out.println("Bitte wähle eine Spalte innerhalb des Spielfelds.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    this.sc.next();
                }
            }

            int x = this.getZeile(y);
            if (x >= 0 &&
                    x < super.spielfeld.getXSize() &&
                    y >= 0 &&
                    y < super.spielfeld.getYSize()) {
                if (!super.spielfeld.pruefeBelegt(x, y)) {
                    super.spielfeld.setSpielstein(
                            new Spielstein(spieler.getToken(), x, y));
                    Spielzug spielzug = new Spielzug(x, y, spieler);
                    this.protokolliere(spielzug);
                    return this.pruefeWeiterspielen(x, y, spieler);
                } else {
                    System.out.println("Error: Feld ist schon belegt.");
                }
            } else if (x == -1) {
                System.out.println("Error: diese Spalte ist schon voll.");
            } else {
                System.out.println(
                        "Error: feld außerhalb des Spielfeldes gewählt.");
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        int[] bestKords = { -1, -1 };
        int maxProbebility = 0;
        for (int y = 0; y < super.spielfeld.getYSize(); y++) { // fur jede Spalte
            int x = this.getZeile(y);
            if (x < 0) {
                continue;
            }
            if (checkGefahr(x, y, spieler)) {
                super.spielfeld.setSpielstein(
                        new Spielstein(spieler.getToken(), x, y));
                Spielzug spielzug = new Spielzug(x, y, spieler);
                this.protokolliere(spielzug);
                return pruefeWeiterspielen(x, y, spieler);
            } else {
                int probebility = this.checkProbebility(x, y, spieler);
                if (maxProbebility < probebility) {
                    maxProbebility = probebility;
                    bestKords[0] = x;
                    bestKords[1] = y;
                }
            }
        }
        int a = bestKords[0];
        int b = bestKords[1];
        super.spielfeld.setSpielstein(
                new Spielstein(spieler.getToken(), a, b));
        Spielzug spielzug = new Spielzug(a, b, spieler);
        this.protokolliere(spielzug);
        return pruefeWeiterspielen(a, b, spieler);
    }
}
