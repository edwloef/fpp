package Viergewinnt;

import common.*;

public class ViergewinntSpielfeld extends Spielfeld {

    public ViergewinntSpielfeld() {
        super(8, 8);
    }

    @Override
    public void draw() {
        System.out.print(Spielfeld.ANSI_CLEAR);

        System.out.print("┌");
        for (int j = 0; j < 2 * super.getYSize() - 1; j++) {
            System.out.print("-");
        }
        System.out.println("┐");

        for (int x = 0; x < super.getXSize(); x++) {
            System.out.print("|");

            for (int y = 0; y < super.getYSize() - 1; y++) {
                Spielstein stein = super.spielsteine[x][y];
                if (stein == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(stein.getTyp());
                }
                System.out.print(" ");
            }

            Spielstein stein = super.spielsteine[x][super.getYSize() - 1];
            if (stein == null) {
                System.out.print(" ");
            } else {
                System.out.print(stein.getTyp());
            }

            System.out.println("|");
        }

        System.out.print("└");
        for (int y = 1; y < super.getYSize(); y++) {
            System.out.print(y + "-");
        }
        System.out.println(super.getYSize() + "┘");
    }

    @Override
    public boolean unentschieden() {
        for (Spielstein spielstein : super.spielsteine[0]) {
            if (spielstein == null) {
                return false;
            }
        }
        return true;
    }
}
