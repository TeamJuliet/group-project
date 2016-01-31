package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;


public interface simulatedPlayerBase {
    public abstract void playLevel(GameState level);
    public abstract Move findBestMove(GameState level);
}
