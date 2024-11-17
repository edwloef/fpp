import spiele.chomp.Chomp;
import spiele.common.Spiel;
import spiele.common.Spieler;
import spiele.common.Spielerart;
import spiele.viergewinnt.Viergewinnt;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String ANSI_RED = "\033[31m"; // red output color
    private static final String ANSI_BLUE = "\033[34m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int welchesSpiel;
        while (true) {
            try {
                System.out.print(
                    "Gib ein, welches Spiel du spielen möchtest: 1 ist Vier Gewinnt, 2 ist Chomp: "
                );
                welchesSpiel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("keine Zahl");
                continue;
            }
            if (welchesSpiel != 1 && welchesSpiel != 2) {
                System.out.println("kein valides Spiel");
                continue;
            }
            break;
        }

        int computerGegner;
        while (true) {
            try {
                System.out.print(
                    "Gib 1 ein, falls du gegen einen Computer spielen willst, sonst gib eine andere Zahl ein: "
                );
                computerGegner = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("keine Zahl");
                continue;
            }
            break;
        }

        System.out.print("Gib deinen Namen ein: ");
        Spieler spieler1 = new Spieler(
            sc.next(),
            Spielerart.Mensch,
            ANSI_BLUE + "◉" + ANSI_RESET
        );

        Spieler spieler2;
        if (computerGegner == 1) {
            spieler2 = new Spieler(
                "Computer",
                Spielerart.Computer,
                ANSI_RED + "◉" + ANSI_RESET
            );
        } else {
            System.out.print("Gib den Namen des Gegenspielers ein: ");
            spieler2 = new Spieler(
                sc.next(),
                Spielerart.Mensch,
                ANSI_RED + "◉" + ANSI_RESET
            );
        }

        Spiel spiel;
        if (welchesSpiel == 1) {
            spiel = new Viergewinnt(spieler1, spieler2);
        } else {
            spiel = new Chomp(spieler1, spieler2);
        }

        while (spiel.durchgang()) {
        }
    }
}
