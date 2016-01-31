package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

// A move consisting of a two positions.
public class Move implements Serializable {
    public final Position p1, p2;

    public Move(Position p1, Position p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}
