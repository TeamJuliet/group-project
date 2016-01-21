package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Created by devankuleindiren on 21/01/2016.
 */
public class Move {

    private int x;                  // x coordinate of move
    private int y;                  // y coordinate of move
    private boolean direction;      // true => right, false => down

    public Move (int x, int y, boolean direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX () { return x; }
    public int getY () { return y; }
    public boolean getDirection () { return direction; }
}
