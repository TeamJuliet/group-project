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

    @Override
    public void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerAlpha has suggested an invalidMove " + move + ".");
    }

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        for (int x = 0; x < level.width; x++) {
            for (int y = 0; y < level.height; y++) {
                Move horizontalSwap = new Move(new Position(x, y), new Position(x + 1, y));
                if (level.isMoveValid(horizontalSwap)) {
                    return horizontalSwap;
                }

                Move verticalSwap = new Move(new Position(x, y), new Position(x, y + 1));
                if (level.isMoveValid(horizontalSwap)) {
                    return verticalSwap;
                }
            }
        }
        throw new NoMovesFoundException(currentState);
    }

}
