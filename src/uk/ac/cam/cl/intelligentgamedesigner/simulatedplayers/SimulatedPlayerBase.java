package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public abstract class SimulatedPlayerBase {

    public void solve(GameState game) throws NoMovesFoundException {
        while (!game.isGameOver()) {
            try {
                game.makeFullMove(calculateBestMove(game));
            } catch (InvalidMoveException e) {
                this.printInvalidMoveError(e.invalidMove);
                try { // TODO: this is horrible, fix it
                    game.makeFullMove(game.getValidMoves().get(0));
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
