package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

/**
 * 
 * Very basic player, simply search for first available move and make it.
 *
 */
public class ScorePlayerAlpha extends SimulatedPlayerBase {

    private static Random random = new Random();

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        // Randomly choose a move so that they are not ordered from top to
        // bottom and left to right.
        int numOfMovesAvailable = currentState.getValidMoves().size();
        if (numOfMovesAvailable > 0)
            return currentState.getValidMoves().get(random.nextInt(numOfMovesAvailable));
        throw new NoMovesFoundException(currentState);
    }
}
