package common;

public class Spielstein implements Comparable<Spielstein> {

    private String token;
    private int x;
    private int y;

    public Spielstein(String typ, int x, int y) {
        this.token = typ;
        this.x = x;
        this.y = y;
    }

    public String getToken() {
        return this.token;
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
            this.getToken().equals(arg0.getToken())
        ) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return (
            "{typ: " + this.token + ", x: " + this.x + ", y: " + this.y + "}"
        );
    }
}
