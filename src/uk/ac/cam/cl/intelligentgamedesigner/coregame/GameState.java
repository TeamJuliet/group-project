package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Created by devankuleindiren on 21/01/2016.
 */
public class GameState {
    int[][] board;
    int movesRemaining;
    int score;
    CandyGenerator candyGenerator;

    public GameState (Design design) {

    }

    int getCell (int x, int y) {
        return 0;
    }

    void makeMove (Move move) throws InvalidMoveException {

    }
}
