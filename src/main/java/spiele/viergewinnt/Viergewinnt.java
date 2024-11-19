package spiele.viergewinnt;

import spiele.common.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Viergewinnt extends Spiel implements Protokollierbar {

    private static final int[][] RICHTUNGSARRAY = {
        {1, -1}, // unten links → oben rechts
        {1, 0}, // unten → oben
        {1, 1}, // unten rechts → oben links
        {0, 1}, // rechts → links
    };
    private final Scanner sc = new Scanner(System.in);

    public Viergewinnt(Spieler spieler1, Spieler spieler2) {
        super(
            new Spieler[]{spieler1, spieler2},
            new ViergewinntSpielfeld()
        );
    }

    /**
     * gibt die Höhe des niedrigsten unbelegten Feldes der eingegebenen Spalte zurück
     */
    private int getZeile(int y) {
        int x = super.spielfeld.getXSize() - 1;

        while (x >= 0) {
            if (!super.spielfeld.prüfeBelegt(x, y)) {
                return x;
            }
            x -= 1;
        }
        return x;
    }

    /**
     * Prüft, ob es vier zusammenhängende Felder des angegebenen Spielers an der
     * Stelle (x, y) in der Richtung (a, b) gibt.
     */
    private boolean prüfeVier(int x, int y, int a, int b, Spieler spieler) {
        int counter = 0;

        for (int i = -3; i < 4; i++) {
            if (
                i == 0 ||
                    super.spielfeld.prüfeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler
                    )
            ) {
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

    private boolean prüfeWeiterspielen(int x, int y, Spieler spieler) {
        for (int[] richtung : RICHTUNGSARRAY) {
            if (this.prüfeVier(x, y, richtung[0], richtung[1], spieler)) {
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
                            ": "
                    );
                    y = this.sc.nextInt() - 1;
                    if (y < 0 || y > (super.spielfeld.getYSize() - 1)) {
                        System.out.println(
                            "Bitte wähle eine Spalte innerhalb des Spielfelds."
                        );
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    this.sc.next();
                }
            }

            int x = this.getZeile(y);
            if (
                x >= 0 &&
                    x < super.spielfeld.getXSize() &&
                    y < super.spielfeld.getYSize()
            ) {
                if (!super.spielfeld.prüfeBelegt(x, y)) {
                    super.spielfeld.setSpielstein(
                        new Spielstein(spieler.getToken(), x, y)
                    );
                    Spielzug spielzug = new Spielzug(x, y, spieler);
                    this.protokolliere(spielzug);
                    return this.prüfeWeiterspielen(x, y, spieler);
                } else {
                    System.out.println("Error: Feld ist schon belegt.");
                }
            } else if (x == -1) {
                System.out.println("Error: diese Spalte ist schon voll.");
            } else {
                System.out.println(
                    "Error: feld außerhalb des Spielfeldes gewählt."
                );
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        int moveX = 0;
        int moveY = 0;
        int maxVal = 0;

        boolean win = false;
        for (int y = 0; y < super.spielfeld.getYSize(); y++) {
            int x = this.getZeile(y);
            if (x < 0) {
                continue;
            }

            if (!this.prüfeWeiterspielen(x, y, spieler)) {
                win = true;
                moveX = x;
                moveY = y;
                break;
            }
        }

        if (!win) {
            for (int y = 0; y < super.spielfeld.getYSize(); y++) {
                int x = this.getZeile(y);
                if (x < 0) {
                    continue;
                }

                if (this.checkGefahr(x, y, spieler)) {
                    moveX = x;
                    moveY = y;
                    break;
                } else {
                    int val = this.computer(x, y, spieler);

                    if (val > maxVal) {
                        maxVal = val;
                        moveX = x;
                        moveY = y;
                    }
                }
            }
        }

        super.spielfeld.setSpielstein(
            new Spielstein(spieler.getToken(), moveX, moveY)
        );
        Spielzug spielzug = new Spielzug(moveX, moveY, spieler);
        this.protokolliere(spielzug);

        super.spielfeld.draw();
        System.out.println(
            spieler.getName() +
                " hat bei " +
                (moveY + 1) +
                ", " +
                (moveX + 1) +
                " gespielt!"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        return this.prüfeWeiterspielen(moveX, moveY, spieler);
    }

    /**
     * Gibt zurück, ob der Gegner mit dem nächsten Zug gewinnt oder eine Zwickmühle erzeugt.
     */
    private boolean checkGefahr(int x, int y, Spieler spieler) {
        Spieler gegner;
        if (spieler.equals(super.spieler[1])) {
            gegner = super.spieler[0];
        } else {
            gegner = super.spieler[1];
        }

        super.spielfeld.setSpielstein(new Spielstein(gegner.getToken(), x, y));

        int counter = 0;
        for (int y1 = 0; y1 < super.spielfeld.getYSize(); y1++) {
            int x1 = this.getZeile(y1);
            if (x1 < 0) {
                continue;
            }

            super.spielfeld.setSpielstein(
                new Spielstein(gegner.getToken(), x1, y1)
            );

            if (!this.prüfeWeiterspielen(x, y, gegner)) {
                counter++;
            }

            super.spielfeld.removeSpielstein(x1, y1);
        }

        super.spielfeld.removeSpielstein(x, y);

        return counter >= 2 || !this.prüfeWeiterspielen(x, y, gegner);
    }

    /**
     * Gibt einen größeren Wert zurück, je besser der eingegebene Zug für den Computer ist.
     */
    private int computer(int x, int y, Spieler spieler) {
        int val = 0;
        for (int[] richtung : RICHTUNGSARRAY) {
            int a = richtung[0];
            int b = richtung[1];

            int counterFree = 0; // counter für die freien Felder
            int counterSame = 0; // counter für die vom Computer belegten Felder
            for (int i = -3; i <= -1; i++) {
                if (
                    super.spielfeld.prüfeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler
                    )
                ) {
                    // falls das Feld vom Computer belegt ist
                    counterSame++;
                } else if (
                    !super.spielfeld.prüfeBelegt((x + a * i), (y + b * i))
                ) {
                    if (
                        !((x + a * i) < 0 ||
                            (y + b * i) < 0 ||
                            (x + a * i) >= spielfeld.getXSize() ||
                            (y + b * i) >= spielfeld.getYSize())
                    ) {
                        // falls das Feld innerhalb des Spielfeldes liegt und unbelegt ist
                        counterFree++;
                    }
                } else {
                    counterSame = 0;
                    counterFree = 0;
                }
            }
            for (int i = 1; i < 4; i++) {
                if (
                    super.spielfeld.prüfeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler
                    )
                ) {
                    // falls das Feld vom Computer belegt ist
                    counterSame++;
                } else if (
                    !super.spielfeld.prüfeBelegt((x + a * i), (y + b * i))
                ) {
                    if (
                        !((x + a * i) < 0 ||
                            (y + b * i) < 0 ||
                            (x + a * i) >= spielfeld.getXSize() ||
                            (y + b * i) >= spielfeld.getYSize())
                    ) {
                        // falls das Feld innerhalb des Spielfeldes liegt und unbelegt ist
                        counterFree++;
                    }
                } else {
                    break;
                }
            }

            // vom Computer belegte Felder werden zehnfach von leeren Feldern gewichtet
            val += counterFree + counterSame * 10;
        }

        return val;
    }
}
