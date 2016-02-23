package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * The coordinates for a position on the board grid addressing a cell. Note: a
 * position might be valid on some gameStates and invalid on others.
 *
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * The x and y coordinates on the board with (0, 0) being on the top left
     * corner.
     */
    public final int x, y;

    /**
     * Create a position that will have as coordinates the ones specified.
     * 
     * @param x
     *            the x coordinate.
     * @param y
     *            the y coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     * @param original Original position to copy.
     */
    public Position(Position original) {
        this.x = original.x;
        this.y = original.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Position))
            return false;
        Position otherMove = (Position) other;
        if (x == otherMove.x && y == otherMove.y)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return (x * 2729 + y * 3547) % 4397;
    }
}
