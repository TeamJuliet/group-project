package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

//Very basic player, simply search for first available move and make it
public class ScorePlayerAlpha implements SimulatedPlayerBase {
    GameState level;

    public ScorePlayerAlpha(GameState level) {
        this.level = level;
    }

    @Override
    public void solve() throws NoMovesFoundException {
        while (level.getGameProgress().movesRemaining > 0) {
            Move bestMove = calculateBestMove(level);
            try {
                level.makeMove(bestMove);
                while (level.makeSmallMove()) {

                }
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

    public static Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if (currentState.getValidMoves().size() > 0)
            return currentState.getValidMoves().get(0);
        throw new NoMovesFoundException(currentState);
    }

}
