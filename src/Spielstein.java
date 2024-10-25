public class Spielstein implements Comparable<Spielstein> {

    private String typ;
    private int x;
    private int y;

    public Spielstein(String typ, int x, int y) {
        this.typ = typ;
        this.x = x;
        this.y = y;
    }

    public String getTyp() {
        return typ;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Spielstein arg0) {
        if (
            this.getX() == arg0.getX() &&
            this.getY() == arg0.getY() &&
            this.getTyp().equals(arg0.getTyp())
        ) {
            return 0;
        } else {
            return 1;
        }
    }
}
