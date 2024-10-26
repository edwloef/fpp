import Chomp.*;
import common.*;

public class Main {

    public static void main(String[] args) {
        ChompSpielfeld spielfeld = new ChompSpielfeld();
        spielfeld.setSpielstein(new Spielstein("", 3, 1));
        spielfeld.setSpielstein(new Spielstein("", 2, 2));
        spielfeld.draw();
    }
}
