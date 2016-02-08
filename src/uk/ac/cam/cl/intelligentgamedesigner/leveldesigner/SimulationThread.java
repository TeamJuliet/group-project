package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;

public class SimulationThread implements Runnable {

    private SimulatedPlayerBase simulatedPlayer;

    public SimulationThread (SimulatedPlayerBase simulatedPlayer) {
        this.simulatedPlayer = simulatedPlayer;
    }

    @Override
    public void run() {
        try {
            simulatedPlayer.solve();
        } catch (NoMovesFoundException e) {
            // This should never occur
            e.printStackTrace();
        }
    }
}
