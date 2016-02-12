package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;

public class SimulationThread implements Runnable {

    private SimulatedPlayerBase simulatedPlayer;
    private GameState level;

    public SimulationThread (SimulatedPlayerBase simulatedPlayer, GameState level) {
        this.simulatedPlayer = simulatedPlayer;
        this.level = level;
    }

    @Override
    public void run() {
        try {
            simulatedPlayer.solve(level);
        } catch (NoMovesFoundException e) {
            // This should never occur
            e.printStackTrace();
        }
    }
}
