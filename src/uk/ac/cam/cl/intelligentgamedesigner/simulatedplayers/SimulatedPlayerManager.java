package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.EnumMap;
import java.util.HashMap;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class SimulatedPlayerManager {
    
    EnumMap<GameMode, HashMap<Integer,SimulatedPlayerBase>> players = new EnumMap<>(GameMode.class);

    private static SimulatedPlayerBase makeSimulatedPlayer(int ability, GameMode mode) {
        SimulatedPlayerBase player;
        int lookAhead = 0;
        int poolSize = 0;
        boolean dimitris = false; // TODO: find better name
        switch (ability) {
        case 1:
            player = new ScorePlayerBeta();
            return player;
        case 2:
            lookAhead = 1;
            poolSize = 2;
            break;
        case 3:
            lookAhead = 1;
            poolSize = 4;
            break;
        case 4:
            lookAhead = 2;
            poolSize = 4;
            break;
        case 5:
            lookAhead = 2;
            poolSize = 8;
            break;
        case 6:
            lookAhead = 3;
            poolSize = 8;
            break;
        case 7:
            lookAhead = 3;
            poolSize = 16;
            break;
        default:
            player = new ScorePlayerAlpha();
            return player;
        }

        switch (mode) {
        case JELLY:
            if (dimitris) {
                player = new JellyRemoverPlayerLuna(lookAhead, poolSize);
                System.out.println("Dimitris");
            } else {
                player = new DepthPotentialJellyPlayer(lookAhead, poolSize);
                System.out.println("Artem");
            }
            break;
        default:
            if (dimitris) {
                player = new MayanScorePlayer(lookAhead, poolSize);
                System.out.println("Dimitris");
            } else {
                player = new DepthPotentialScorePlayer(lookAhead, poolSize);
                System.out.println("Artem");
            }
            break;
        }
        return player;
    }

    public Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException {
        GameMode mode = level.getLevelDesign().getMode();
        if(!players.containsKey(mode)) players.put(mode, new HashMap<Integer, SimulatedPlayerBase>());
        HashMap<Integer, SimulatedPlayerBase> modePlayers = players.get(mode);
        if(!modePlayers.containsKey(ability)) modePlayers.put(ability, makeSimulatedPlayer(ability, mode));
        SimulatedPlayerBase player = modePlayers.get(ability);
        Move move = player.calculateBestMove(level);
        if (move != null)
            return move;
        player.noMovesFound();
        return level.getValidMoves().get(0);
    }

    public static int getMaxAbilityLevel() {
        return 7; // Decided by the switch in makeSimulatedPlayer()
    }

    public static void solve(GameState level, int ability) throws NoMovesFoundException {
        SimulatedPlayerBase player = makeSimulatedPlayer(ability, level.getLevelDesign().getMode());
        player.solve(level);
    }
}
