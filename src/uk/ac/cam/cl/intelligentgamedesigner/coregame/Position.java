package uk.ac.cam.cl.intelligentgamedesigner.coregame;

// The coordinates for a position on the grid.
public class Position {
    final private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
