package chomp;

import common.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Chomp extends Spiel implements Protokollierbar {

    private final Scanner sc = new Scanner(System.in);

    public Chomp(Spieler spieler1, Spieler spieler2) {
        super(new Spieler[]{spieler1, spieler2}, new ChompSpielfeld());
    }

    private boolean prüfeWeiterspielen() {
        return !(
            super.spielfeld.prüfeBelegt(1, 0) &&
                super.spielfeld.prüfeBelegt(0, 1)
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
                return this.prüfeWeiterspielen();
            } else {
                System.out.println("Error: invalides Feld.");
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        int moveX = 0;
        int moveY = 0;
        long maxVal = Long.MIN_VALUE;

        int maxX = Integer.MAX_VALUE;
        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);
            if (x < maxX) {
                maxX = x;
            }

            for (x = 0; x < maxX; x++) {
                if (super.spielfeld.prüfeBelegt(x, y)) {
                    continue;
                }

                super.spielfeld.setSpielstein(new Spielstein("", x, y));

                long val = this.computer(true, 0);

                if (val > maxVal) {
                    maxVal = val;
                    moveX = x;
                    moveY = y;
                } else if (val == maxVal && Math.random() < 0.5) {
                    moveX = x;
                    moveY = y;
                }

                super.spielfeld.removeSpielstein(x, y);
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

        return this.prüfeWeiterspielen();
    }

    /**
     * Gibt einen größeren Wert zurück, je besser das eingegebene Spielfeld für den Computer ist.
     * Das eingegebene Spielfeld ist gut, falls der Computer oft in wenigen Zügen gewinnen kann,
     * und schlecht, falls der Mensch oft in wenigen Zügen gewinnen kann.
     */
    private long computer(boolean player, int done) {
        if (
            super.spielfeld.prüfeBelegt(1, 0) &&
                super.spielfeld.prüfeBelegt(0, 1)
        ) {
            if (player) {
                return (
                    (long) super.spielfeld.getXSize() * super.spielfeld.getYSize() -
                        done
                ); // falls der Computer das simulierte Spiel gewonnen hat
            } else {
                return (
                    done -
                        (long) super.spielfeld.getXSize() * super.spielfeld.getYSize()
                ); // falls der Mensch das simulierte Spiel gewonnen hat
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
                if (spielfeld.prüfeBelegt(x, y)) {
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
        int width = ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
        int height = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(0);
        int maxX = Integer.MAX_VALUE;

        for (
            int y = 0;
            y < ((ChompSpielfeld) super.spielfeld).getMaxYInColumn(0);
            y++
        ) {
            int x = ((ChompSpielfeld) super.spielfeld).getMaxXInRow(y);

            if (x < maxX) {
                maxX = x;
            }

            depth += maxX;
        }

        return ((width * height) / depth);
    }
}
