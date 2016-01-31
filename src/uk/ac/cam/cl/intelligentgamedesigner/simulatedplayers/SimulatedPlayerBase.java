package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public interface SimulatedPlayerBase {
    public abstract void solve() throws NoMovesFoundException;

    public abstract Move calculateBestMove(GameState currentState) throws NoMovesFoundException;

    abstract void printInvalidMoveError(Move move);
}
