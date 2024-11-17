package spiele.chomp;

import spiele.common.Spielfeld;
import spiele.common.Spielstein;

public class ChompSpielfeld extends Spielfeld {

    public ChompSpielfeld() {
        super();
        this.setSpielstein(new Spielstein("", 0, 0));
    }

    @Override
    public void draw() {
        System.out.print(Spielfeld.ANSI_CLEAR);

        int len = ("" + this.getMaxYInColumn(0)).length() + 1;

        String full = "▒".repeat(len);
        String empty = " ".repeat(len);

        int width = getMaxYInColumn(0);
        int maxY = width;
        int newMaxY;
        for (int x = 1; x <= this.getMaxXInRow(0); x++) {
            newMaxY = this.getMaxYInColumn(x - 1);
            if (newMaxY > maxY) {
                newMaxY = maxY;
            }

            for (int y = 0; y < newMaxY; y++) {
                System.out.print(full);
            }

            for (int y = newMaxY; y < maxY; y++) {
                System.out.print(empty);
            }

            System.out.print(" ");
            for (int y = maxY; y < width; y++) {
                System.out.print(empty);
            }
            System.out.println(x);

            maxY = newMaxY;
        }

        for (int y = 1; y <= width; y++) {
            StringBuilder outputNum = new StringBuilder("" + y);
            while (outputNum.length() < len) {
                outputNum.append(" ");
            }
            System.out.print(outputNum);
        }
        System.out.println();
    }

    public int getMaxXInRow(int y) {
        for (int x = 1; x < super.getXSize(); x++) {
            if (super.prüfeBelegt(x, y)) {
                return x;
            }
        }

        return super.getXSize();
    }

    public int getMaxYInColumn(int x) {
        for (int y = 1; y < super.getYSize(); y++) {
            if (super.prüfeBelegt(x, y)) {
                return y;
            }
        }

        return super.getYSize();
    }

    @Override
    public boolean unentschieden() {
        return false;
    }
}
