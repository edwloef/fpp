package common;

public class Spielstein {

    private final String token;
    private final int x;
    private final int y;

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
    public String toString() {
        return (
            "{typ: " + this.token + ", x: " + this.x + ", y: " + this.y + "}"
        );
    }
}
