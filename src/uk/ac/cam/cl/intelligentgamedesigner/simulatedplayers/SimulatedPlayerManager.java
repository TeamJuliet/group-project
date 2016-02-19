package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class SimulatedPlayerManager {

    private static SimulatedPlayerBase makeSimulatedPlayer(int ability, GameMode mode) {
        SimulatedPlayerBase player;
        int lookAhead = 0;
        int poolSize = 0;
        switch (ability) {
        case 1:
            player = new ScorePlayerBeta();
            break;
        case 2:
            lookAhead = 2;
            poolSize = 4;
            break;
        case 3:
            lookAhead = 4;
            poolSize = 8;
            break;
        default:
            player = new ScorePlayerAlpha();
            break;
        }

        switch (mode) {
        case JELLY:
            player = new DepthPotentialJellyPlayer(lookAhead, poolSize);
            break;
        default:
            player = new DepthPotentialScorePlayer(lookAhead, poolSize);
            break;
        }
        return player;
    }

    public static Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability, level.getLevelDesign().getMode());
        Move move = player.calculateBestMove(level);
        if (move != null)
            return move;
        player.noMovesFound();
        return level.getValidMoves().get(0);
    }

    public static int getMaxAbilityLevel() {
        return 3;
    }

    public static void solve(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability, level.getLevelDesign().getMode());
        player.solve(level);
    }
}
