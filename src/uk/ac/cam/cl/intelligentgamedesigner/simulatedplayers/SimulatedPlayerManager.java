package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.*;

public class SimulatedPlayerManager {

    private static SimulatedPlayerBase makeSimulatedPlayer(int ability) {
        SimulatedPlayerBase player;
        switch (ability) {
        case 1:
            player = new ScorePlayerBeta();
            break;
        case 2:
            player = new ScorePlayerGamma();
            break;
        case 3:
            player = new DepthPotentialScorePlayer(4, 4);
            break;
        default:
            player = new ScorePlayerAlpha();
            break;
        }
        return player;
    }

    public static Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability);
        return player.calculateBestMove(level);
    }

    public static int getMaxAbilityLevel() {
        return 3;
    }

    public static void solve(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability);
        player.solve(level);
    }
}
