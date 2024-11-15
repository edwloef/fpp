package Chomp;

import common.*;

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
        int max_y = width;
        int new_max_y;
        for (int x = 1; x <= this.getMaxXInRow(0); x++) {
            new_max_y = this.getMaxYInColumn(x - 1);
            if (new_max_y > max_y) {
                new_max_y = max_y;
            }

            for (int y = 0; y < new_max_y; y++) {
                System.out.print(full);
            }

            for (int y = new_max_y; y < max_y; y++) {
                System.out.print(empty);
            }

            System.out.print(" ");
            for (int y = max_y; y < width; y++) {
                System.out.print(empty);
            }
            System.out.println(x);

            max_y = new_max_y;
        }

        for (int y = 1; y <= width; y++) {
            StringBuilder output_num = new StringBuilder("" + y);
            while (output_num.length() < len) {
                output_num.append(" ");
            }
            System.out.print(output_num);
        }
        System.out.println();
    }

    public int getMaxXInRow(int y) {
        for (int x = 1; x < super.getXSize(); x++) {
            if (super.pruefeBelegt(x, y)) {
                return x;
            }
        }

        return super.getXSize();
    }

    public int getMaxYInColumn(int x) {
        for (int y = 1; y < super.getYSize(); y++) {
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
