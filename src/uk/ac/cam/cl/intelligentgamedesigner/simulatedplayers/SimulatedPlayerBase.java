package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public abstract class SimulatedPlayerBase {
    public void solve(GameState level) throws NoMovesFoundException {
        while (level.getGameProgress().movesRemaining > 0) {
            Move bestMove = calculateBestMove(level);
            try {
                level.makeFullMove(bestMove);
            } catch (InvalidMoveException e) {
                this.printInvalidMoveError(e.invalidMove);
                try { // TODO: this is horrible, fix it
                    level.makeFullMove(level.getValidMoves().get(0));
                } catch (InvalidMoveException exception) {
                    return;
                }
            }
        }
    }

    public abstract Move calculateBestMove(GameState currentState) throws NoMovesFoundException;

    public void noMovesFound() {
        System.err.format("WARNING! %s didn't return a valid move!\n", this.getClass().getSimpleName());
    }

    private void printInvalidMoveError(Move move) {
    }
}
