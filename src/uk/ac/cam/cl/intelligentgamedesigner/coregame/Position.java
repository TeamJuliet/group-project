package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

// The coordinates for a position on the grid.
public class Position implements Serializable {

    public final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position original) {
        this.x = original.x;
        this.y = original.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
