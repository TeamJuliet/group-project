package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public interface SimulatedPlayerBase {
    public abstract void solve();

    abstract void printInvalidMoveError(Move move);
}
