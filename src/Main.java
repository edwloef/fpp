import Chomp.*;
import Viergewinnt.*;
import common.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String ANSI_RED = "\033[31m"; // red output color
    private static final String ANSI_BLUE = "\033[34m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color

    public static void main(String[] args) {
        System.out.print(
            "Gib ein, welches Spiel du spielen möchtest: 1 ist Viergewinnt, 2 ist Chomp: "
        );
        Scanner sc = new Scanner(System.in);

        int welchesSpiel;
        while (true) {
            try {
                welchesSpiel = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("keine Zahl");
                sc.next();
                continue;
            }
            if (welchesSpiel != 1 && welchesSpiel != 2) {
                System.out.println("kein valides Spiel");
                sc.next();
                continue;
            }
            break;
        }

        System.out.print(
            "Gib 1 ein, falls du gegen einen Computer spielen willst, sonst gib eine andere Zahl ein: "
        );
        int computerGegner;
        while (true) {
            try {
                computerGegner = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("keine Zahl");
                sc.next();
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

        while (spiel.durchgang()) {}

        sc.close();
    }
}
