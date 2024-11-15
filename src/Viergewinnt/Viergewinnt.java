package Viergewinnt;

import common.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Viergewinnt extends Spiel implements Protokollierbar {

    private final Scanner sc = new Scanner(System.in);

    private static final int[][] RICHTUNGSARRAY = {
        { 1, -1 }, // unten links -> oben rechts
        { 1, 0 }, // unten -> oben
        { 1, 1 }, // unten rechts -> oben links
        { 0, 1 }, // rechts -> links
    };

    public Viergewinnt(Spieler spieler_1, Spieler spieler_2) {
        super(
            new Spieler[] { spieler_1, spieler_2 },
            new ViergewinntSpielfeld()
        );
    }

    /**
     * gibt die Höhe des niedrigsten unbelegten Feldes der eingegebenen Spalte zurück
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
     * Prüft, ob es vier zusammenhängende Felder des angegebenen Spielers an der
     * Stelle (x,y) in der Richtung (a, b) gibt.
     */
    private boolean pruefeVier(int x, int y, int a, int b, Spieler spieler) {
        int counter = 0;

        for (int i = -3; i < 4; i++) {
            if (
                i == 0 ||
                super.spielfeld.pruefeBelegtSpieler(
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
                if (!super.spielfeld.pruefeBelegt(x, y)) {
                    super.spielfeld.setSpielstein(
                        new Spielstein(spieler.getToken(), x, y)
                    );
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
                    "Error: feld außerhalb des Spielfeldes gewählt."
                );
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        int move_x = 0;
        int move_y = 0;
        int max_val = 0;

        boolean win = false;
        for (int y = 0; y < super.spielfeld.getYSize(); y++) {
            int x = this.getZeile(y);
            if (x < 0) {
                continue;
            }

            if (!this.pruefeWeiterspielen(x, y, spieler)) {
                win = true;
                move_x = x;
                move_y = y;
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
                    move_x = x;
                    move_y = y;
                    break;
                } else {
                    int val = this.computer(x, y, spieler);

                    if (val > max_val) {
                        max_val = val;
                        move_x = x;
                        move_y = y;
                    }
                }
            }
        }

        super.spielfeld.setSpielstein(
            new Spielstein(spieler.getToken(), move_x, move_y)
        );
        Spielzug spielzug = new Spielzug(move_x, move_y, spieler);
        this.protokolliere(spielzug);

        super.spielfeld.draw();
        System.out.println(
            spieler.getName() +
            " hat bei " +
            (move_y + 1) +
            ", " +
            (move_x + 1) +
            " gespielt!"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        return this.pruefeWeiterspielen(move_x, move_y, spieler);
    }

    /**
     * Gibt zurück, ob der Gegner mit dem nachsten Zug gewinnt.
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
                    super.spielfeld.pruefeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler
                    )
                ) {
                    // falls das Feld vom Computer belegt ist
                    counterSame++;
                } else if (
                    !super.spielfeld.pruefeBelegt((x + a * i), (y + b * i))
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
                    super.spielfeld.pruefeBelegtSpieler(
                        x + a * i,
                        y + b * i,
                        spieler
                    )
                ) {
                    // falls das Feld vom Computer belegt ist
                    counterSame++;
                } else if (
                    !super.spielfeld.pruefeBelegt((x + a * i), (y + b * i))
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
