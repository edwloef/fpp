package common;

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
        return this.typ;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
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

    @Override
    public String toString() {
        return "{typ: " + this.typ + ", x: " + this.x + ", y: " + this.y + "}";
    }
}
