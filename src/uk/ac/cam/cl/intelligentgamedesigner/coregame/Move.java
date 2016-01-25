package uk.ac.cam.cl.intelligentgamedesigner.coregame;

// A move consisting of a two positions.
public class Move {
    final private Position p1, p2;

    public Move(Position p1, Position p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Position getP1() {
        return p1;
    }

    public Position getP2() {
        return p2;
    }
}
