package DameEngine;

public class Coordinates {
    private int x, y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void moveX(int dx){
        x += dx;
    }

    public void moveY(int dy){
        y += dy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Coordinates compare){
        return this.x == compare.x && this.y == compare.y;
    }

    static boolean equals(Coordinates compareA, Coordinates compareB){
        return compareA.equals(compareB);
    }
}
