package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

// A move consisting of a two positions.
public class Move implements Serializable {
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
        if (other == this)
            return true;
        if (!(other instanceof Move))
            return false;
        Move otherMove = (Move) other;
        if (p1.equals(otherMove.p1) && p2.equals(otherMove.p2))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return (p1.hashCode() + p2.hashCode())%7919;
    }
}
