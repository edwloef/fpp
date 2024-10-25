public class Main {

    public static void main(String[] args) {
        Spieler spielerA = new Spieler("Wedeke", Spielerart.Mensch);
        Spieler spielerB = new Spieler("Edwin", Spielerart.Mensch);
        Viergewinnt game = new Viergewinnt(spielerB, spielerA);
        while (true) {
            if (game.spielzug(spielerB)) {
                System.out.println("Gewonnen " + spielerB.getName());
            }

            if (game.spielzug(spielerA)) {
                System.out.println("Gewonnen " + spielerB.getName());
            }
        }
    }
}
