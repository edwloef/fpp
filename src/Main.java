import Chomp.*;
import common.*;

public class Main {

    private static final String ANSI_RED = "\033[31m"; // red output color
    private static final String ANSI_BLUE = "\033[34m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color

    public static void main(String[] args) {
        Chomp chomp = new Chomp(
            new Spieler(
                "Konrad",
                Spielerart.Mensch,
                ANSI_RED + "◉" + ANSI_RESET
            ),
            new Spieler(
                "Edwin",
                Spielerart.Mensch,
                ANSI_BLUE + "◉" + ANSI_RESET
            )
        );

        while (chomp.durchgang()) {}
    }
}
