package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public interface SimulatedPlayerBase {
    public abstract void solve() throws NoMovesFoundException;

    public static Move calculateBestMove(GameState currentState) throws NoMovesFoundException{
        return currentState.getValidMoves().get(0);
    }
}
