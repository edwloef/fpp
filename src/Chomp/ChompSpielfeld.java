package Chomp;

import common.*;

public class ChompSpielfeld extends Spielfeld {

    public ChompSpielfeld() {
        super(4, 7);
    }

    @Override
    public void draw() {
        System.out.print(Spielfeld.ANSI_CLEAR);

        int max_y = this.getMaxYInRow(0);
        int new_max_y = max_y;

        System.out.print("┌");
        for (int i = 0; i < max_y; i++) {
            System.out.print("--");
        }
        System.out.println("┐");

        int max_x = this.getMaxXInRow(0);
        for (int x = 1; x < max_x; x++) {
            System.out.print("|");

            new_max_y = this.getMaxYInRow(x);
            if (new_max_y > max_y) {
                new_max_y = max_y;
            }

            for (int y = 0; y < new_max_y; y++) {
                System.out.print("  ");
            }

            boolean modified = true;
            for (int y = new_max_y; y < max_y; y++) {
                if (modified) {
                    System.out.print("┌-");
                    modified = false;
                } else {
                    System.out.print("--");
                }
            }

            if (new_max_y != max_y) {
                System.out.println("┘");
            } else {
                System.out.println("|");
            }

            max_y = new_max_y;
        }

        System.out.print("└");

        for (int y = 0; y < max_y; y++) {
            System.out.print("--");
        }

        System.out.println("┘");
    }

    private int getMaxXInRow(int y) {
        for (int x = 0; x < super.getXSize(); x++) {
            if (super.prüfeBelegt(x, y)) {
                return x;
            }
        }

        return super.getXSize();
    }

    private int getMaxYInRow(int x) {
        for (int y = 0; y < super.getYSize(); y++) {
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
