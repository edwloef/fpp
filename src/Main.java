import Chomp.*;
import common.*;

public class Main {

    public static void main(String[] args) {
        Chomp chomp = new Chomp(
            new Spieler("Edwin", Spielerart.Mensch),
            new Spieler("Wedeke", Spielerart.Mensch)
        );

        while (chomp.durchgang()) {}
    }
}
