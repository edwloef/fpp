import Chomp.*;
import common.*;

public class Main {

    public static void main(String[] args) {
        Chomp chomp = new Chomp(
            new Spieler("Konrad", Spielerart.Mensch),
            new Spieler("Edwin", Spielerart.Mensch)
        );

        while (chomp.durchgang()) {}
    }
}
