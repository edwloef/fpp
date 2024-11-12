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
        int max_x = 0;
        int max_y = 0;
        long max_val = Long.MIN_VALUE;

        int depth = 0;
        int minx = Integer.MAX_VALUE;
        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);

            if (x < minx) {
                minx = x;
            }
            depth += minx;
        }
        depth = 200 / depth;

        minx = Integer.MAX_VALUE;
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
                if (super.spielfeld.pruefeBelegt(x, y)) {
                    continue;
                }

                super.spielfeld.setSpielstein(new Spielstein("", x, y));

                long val = this.computer(true, depth);

                if (val > max_val) {
                    max_val = val;
                    max_x = x;
                    max_y = y;
                } else if (val == max_val) {
                    if (Math.random() < 0.5) {
                        max_x = x;
                        max_y = y;
                    }
                }

                super.spielfeld.removeSpielstein(x, y);
            }
        }

        super.spielfeld.setSpielstein(
            new Spielstein(spieler.getToken(), max_x, max_y)
        );

        super.spielfeld.draw();
        System.out.println(
            spieler.getName() +
            " hat bei " +
            max_x +
            ", " +
            max_y +
            " gespielt!"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        return this.pruefeWeiterspielen();
    }

    private long computer(boolean player, int limit) {
        if (
            super.spielfeld.pruefeBelegt(1, 0) &&
            super.spielfeld.pruefeBelegt(0, 1)
        ) {
            if (player) {
                return 1;
            } else {
                return -10;
            }
        } else if (limit == 0) {
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

                sum += this.computer(!player, limit - 1);

                super.spielfeld.removeSpielstein(x, y);
            }
        }

        return sum;
    }
}
