import Viergewinnt.*;
import common.*;

public class Main {

    public static void main(String[] args) {
        Spieler spielerA = new Spieler("Wedeke", Spielerart.Mensch);
        Spieler spielerB = new Spieler("Edwin", Spielerart.Mensch);
        Viergewinnt game = new Viergewinnt(spielerB, spielerA);
        while (game.durchgang()) {}
    }
}
