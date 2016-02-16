package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;

public class SimulationThread implements Runnable {

    private GameState level;
    private int playerAbility;

    public SimulationThread (GameState level, int playerAbility) {
        this.level = level;
        this.playerAbility = playerAbility;
    }

    @Override
    public void run() {
        try {
            SimulatedPlayerManager.solve(level, playerAbility);
        } catch (NoMovesFoundException e) {
            // This should never occur
            e.printStackTrace();
        }
    }
}
