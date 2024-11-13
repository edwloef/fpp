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
        int move_x = 0;
        int move_y = 0;
        long max_val = Long.MIN_VALUE;

        int max_x = Integer.MAX_VALUE;
        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);
            if (x < max_x) {
                max_x = x;
            }

            for (x = 0; x < max_x; x++) {
                if (super.spielfeld.pruefeBelegt(x, y)) {
                    continue;
                }

                super.spielfeld.setSpielstein(new Spielstein("", x, y));

                long val = this.computer(true, 0);

                if (val > max_val) {
                    max_val = val;
                    move_x = x;
                    move_y = y;
                } else if (val == max_val && Math.random() < 0.5) {
                    move_x = x;
                    move_y = y;
                }

                super.spielfeld.removeSpielstein(x, y);
            }
        }

        super.spielfeld.setSpielstein(
            new Spielstein(spieler.getToken(), move_x, move_y)
        );

        super.spielfeld.draw();
        System.out.println(
            spieler.getName() +
            " hat bei " +
            (move_x + 1) +
            ", " +
            (move_y + 1) +
            " gespielt!"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        return this.pruefeWeiterspielen();
    }

    /**
     * Gibt einen größeren Wert zurück, je besser das eingegebene Spielfeld für den Computer ist.
     *
     * Das eingegebene Spielfeld ist gut, falls der Computer oft in wenigen Zügen gewinnen kann,
     * und schlecht, falls der Mensch oft in wenigen Zügen gewinnen kann.
     */
    private long computer(boolean player, int done) {
        if (
            super.spielfeld.pruefeBelegt(1, 0) &&
            super.spielfeld.pruefeBelegt(0, 1)
        ) {
            if (player) {
                return (this.getDepth() - done); // falls der Computer das simulierte Spiel gewonnen hat
            } else {
                return -(this.getDepth() - done); // falls der Mensch das simulierte Spiel gewonnen hat
            }
        } else if ((this.getDepth() - done) <= 0) {
            return 0;
        }

        int minx = Integer.MAX_VALUE;
        long sum = 0;
        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);
            if (x < minx) {
                minx = x;
            }

            for (x = 0; x < minx; x++) {
                if (spielfeld.pruefeBelegt(x, y)) {
                    continue;
                }

                super.spielfeld.setSpielstein(new Spielstein("", x, y));

                sum += this.computer(!player, done + 1);

                super.spielfeld.removeSpielstein(x, y);
            }
        }

        return sum;
    }

    private int getDepth() {
        int depth = 0;
        int max_x = Integer.MAX_VALUE;

        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);

            if (x < max_x) {
                max_x = x;
            }

            depth += max_x;
        }

        return 100 / depth;
    }
}
