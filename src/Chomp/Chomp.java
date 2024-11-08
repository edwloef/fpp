package Chomp;

import common.*;
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
    public boolean spielzug(Spieler spieler) {
        while (true) {
            System.out.print(
                "Bitte wähle deine Eingabespalte " + spieler.getName() + ": "
            );
            int y = this.sc.nextInt() - 1;
            System.out.print(
                "Bitte wähle deine Eingabezeile " + spieler.getName() + ": "
            );
            int x = this.sc.nextInt() - 1;
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
}
