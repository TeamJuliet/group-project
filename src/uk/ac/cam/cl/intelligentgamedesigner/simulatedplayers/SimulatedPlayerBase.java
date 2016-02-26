package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public abstract class SimulatedPlayerBase {

    /**
     * Function that solves the game. Repeatedly finds and makes best move,
     * until game is over.
     * 
     * @param state
     *            GameState to be solved.
     */
    void solve(GameState state) throws NoMovesFoundException {
        while (!state.isGameOver()) {
            try {
                state.makeFullMove(calculateBestMove(state));
            } catch (InvalidMoveException e) {
                this.printInvalidMoveError(e.invalidMove);
                try { // TODO: Find a better way to resolve this.
                    state.makeFullMove(state.getValidMoves().get(0));
                } catch (InvalidMoveException exception) {
                    return;
                }
            }
        }
    }

    /**
     * Finds best move in a given position.
     * 
     * @param currentState
     *            GameState to be evaluated.
     * @return best move according to the algorithm used.
     */
    abstract Move calculateBestMove(GameState currentState) throws NoMovesFoundException;

    void noMovesFound() {
        System.err.format("WARNING! %s didn't return a valid move!\n", this.getClass().getSimpleName());
    }

    private void printInvalidMoveError(Move move) {
    }
}
