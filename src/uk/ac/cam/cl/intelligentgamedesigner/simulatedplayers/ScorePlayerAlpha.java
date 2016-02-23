package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

//Very basic player, simply search for first available move and make it
public class ScorePlayerAlpha extends SimulatedPlayerBase {

    private void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerAlpha has suggested an invalidMove " + move + ".");
    }

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if (currentState.getValidMoves().size() > 0)
            return currentState.getValidMoves().get(0);
        throw new NoMovesFoundException(currentState);
    }
}
