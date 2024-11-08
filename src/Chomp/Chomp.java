package Chomp;

import common.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Chomp extends Spiel implements Protokollierbar {

    private Scanner sc = new Scanner(System.in);

    public Chomp(Spieler spieler_1, Spieler spieler_2) {
        super(new Spieler[] { spieler_1, spieler_2 }, new ChompSpielfeld());
    }

    private boolean pruefeWeiterspielen() {
        return !(
            super.spielfeld.pruefeBelegt(1, 0) &&
            super.spielfeld.pruefeBelegt(0, 1)
        );
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
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    this.sc.next();
                }
            }

            int x;
            while (true) {
                try {
                    System.out.print(
                        "Bitte wähle deine Eingabezeile " +
                        spieler.getName() +
                        ": "
                    );
                    x = this.sc.nextInt() - 1;
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    this.sc.next();
                }
            }

            if (
                x >= 0 &&
                x < ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y) &&
                y >= 0 &&
                y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(x) &&
                (x != 0 || y != 0)
            ) {
                super.spielfeld.setSpielstein(new Spielstein("", x, y));
                Spielzug spielzug = new Spielzug(x, y, spieler);
                this.protokolliere(spielzug);
                return this.pruefeWeiterspielen();
            } else {
                System.out.println("Error: invalides Feld.");
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        return false;
    }
}
