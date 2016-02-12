package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

//Very basic player, simply search for first available move and make it
public class ScorePlayerAlpha extends SimulatedPlayerBase {

    @Override
    public void solve(GameState level) throws NoMovesFoundException {
        while (level.getMovesRemaining() > 0) {
            Move bestMove = calculateBestMove(level);
            try {
                level.makeMove(bestMove);
            } catch (InvalidMoveException e) {
                printInvalidMoveError(e.invalidMove);
                try { // TODO: this is horrible, fix it
                    level.makeMove(level.getValidMoves().get(0));
                } catch (InvalidMoveException exception) {
                    return;
                }
            }
        }
    }

    private void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerAlpha has suggested an invalidMove " + move + ".");
    }

    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if (currentState.getValidMoves().size() > 0)
            return currentState.getValidMoves().get(0);
        throw new NoMovesFoundException(currentState);
    }

}
