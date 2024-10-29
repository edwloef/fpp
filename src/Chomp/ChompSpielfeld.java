package Chomp;

import common.*;

public class ChompSpielfeld extends Spielfeld {

    public ChompSpielfeld() {
        super(4, 7);
    }

    @Override
    public void draw() {
        System.out.print(Spielfeld.ANSI_CLEAR);

        int old_max_y = this.getMaxYInColumn(0);
        int max_y = old_max_y;
        int new_max_y = max_y;

        int max_x = this.getMaxXInRow(0);
        for (int x = 1; x <= max_x; x++) {
            new_max_y = this.getMaxYInColumn(x - 1);
            if (new_max_y > max_y || new_max_y == 0) {
                new_max_y = max_y;
            }

            for (int y = 0; y < new_max_y; y++) {
                System.out.print("▒▒");
            }

            for (int y = new_max_y; y < max_y; y++) {
                System.out.print("  ");
            }

            System.out.print(" ");
            for (int y = max_y; y < old_max_y; y++) {
                System.out.print("  ");
            }
            System.out.println(x);

            max_y = new_max_y;
        }

        System.out.print(" ");
        for (int y = 1; y <= old_max_y; y++) {
            System.out.print(y + " ");
        }
        System.out.println();
    }

    public int getMaxXInRow(int y) {
        for (int x = 0; x < super.getXSize(); x++) {
            if (super.pruefeBelegt(x, y)) {
                return x;
            }
        }

        return super.getXSize();
    }

    public int getMaxYInColumn(int x) {
        for (int y = 0; y < super.getYSize(); y++) {
            if (super.pruefeBelegt(x, y)) {
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
