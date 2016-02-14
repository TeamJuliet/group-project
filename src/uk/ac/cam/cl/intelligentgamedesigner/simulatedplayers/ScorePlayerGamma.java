package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class ScorePlayerGamma extends SimulatedPlayerBase {


    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        // Look through all possible moves and try to make special candies if
        // possible
        // Try to bring special candies together and combine them
        //
        return null;
    }

    private void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerDelta has suggested an invalidMove " + move + ".");
    }

}
