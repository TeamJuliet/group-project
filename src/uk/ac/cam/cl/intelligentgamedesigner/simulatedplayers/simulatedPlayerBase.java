package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public interface simulatedPlayerBase {
    public abstract void solve();

    abstract void printInvalidMoveError(Move move);
}
