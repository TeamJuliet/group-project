package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * An immutable class that contains a move on the board specified by two
 * positions, the positions of the cells that will be swapped.
 * 
 * Note: that a position might be valid on a certain GameState and invalid in others.
 *
 */
public class Move implements Serializable {
    /**
     * The positions of the two cells to be swapped.
     */
    public final Position p1, p2;

    public Move(Position p1, Position p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return "Swap " + p1 + " with " + p2;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Move))
            return false;
        Move otherMove = (Move) other;
        if (p1.equals(otherMove.p1) && p2.equals(otherMove.p2))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return (p1.hashCode() + p2.hashCode()) % 7919;
    }
}
