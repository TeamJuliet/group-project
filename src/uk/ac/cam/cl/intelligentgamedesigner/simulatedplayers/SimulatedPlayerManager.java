package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class SimulatedPlayerManager {

    public static SimulatedPlayerBase makeSimulatedPlayer(int ability) {
        SimulatedPlayerBase player;
        switch (ability) {
        case 1:
            player = new ScorePlayerBeta();
            break;
        case 2:
            player = new DepthPotentialScorePlayer(2,4);
            break;
        case 3:
            player = new DepthPotentialScorePlayer(4, 8);
            break;
        default:
            player = new ScorePlayerAlpha();
            break;
        }
        return player;
    }

    public static Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability);
        Move move = player.calculateBestMove(level);
        if (move != null) return move;
        player.noMovesFound();
        return level.getValidMoves().get(0);
    }

    public static int getMaxAbilityLevel() {
        return 3;
    }

    public static void solve(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability);
        player.solve(level);
    }
}
