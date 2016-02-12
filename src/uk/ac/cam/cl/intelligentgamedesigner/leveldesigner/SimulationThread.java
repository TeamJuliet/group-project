package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase1;

public class SimulationThread implements Runnable {

    private SimulatedPlayerBase1 simulatedPlayer;

    public SimulationThread (SimulatedPlayerBase1 simulatedPlayer) {
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
