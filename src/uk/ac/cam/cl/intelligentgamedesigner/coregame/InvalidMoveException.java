package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class InvalidMoveException extends Exception {
    public final Move invalidMove;

    public InvalidMoveException(Move move) {
        invalidMove = move;
    }
}
