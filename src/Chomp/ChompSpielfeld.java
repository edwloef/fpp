package Chomp;

import common.*;

public class ChompSpielfeld extends Spielfeld {

    public ChompSpielfeld() {
        super();
    }

    @Override
    public void draw() {
        System.out.print(Spielfeld.ANSI_CLEAR);

        int old_max_y = this.getMaxYInColumn(0);
        int max_y = old_max_y;
        int new_max_y = max_y;

        int len = ("" + this.getYSize()).length() + 1;

        String full = "";
        for (int i = 0; i < len; i++) {
            full += "▒";
        }

        String empty = "";
        for (int i = 0; i < len; i++) {
            empty += " ";
        }

        int max_x = this.getMaxXInRow(0);
        for (int x = 1; x <= max_x; x++) {
            new_max_y = this.getMaxYInColumn(x - 1);
            if (new_max_y > max_y || new_max_y == 0) {
                new_max_y = max_y;
            }

            for (int y = 0; y < new_max_y; y++) {
                System.out.print(full);
            }

            for (int y = new_max_y; y < max_y; y++) {
                System.out.print(empty);
            }

            System.out.print(" ");
            for (int y = max_y; y < old_max_y; y++) {
                System.out.print(empty);
            }
            System.out.println(x);

            max_y = new_max_y;
        }

        for (int y = 1; y <= old_max_y; y++) {
            String output_num = "" + y;
            while (output_num.length() < len) {
                output_num += " ";
            }
            System.out.print(output_num);
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
