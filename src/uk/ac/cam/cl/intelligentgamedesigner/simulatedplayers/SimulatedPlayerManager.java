package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.EnumMap;
import java.util.HashMap;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class SimulatedPlayerManager {

    EnumMap<GameMode, HashMap<Integer, SimulatedPlayerBase>> players = new EnumMap<>(GameMode.class);

    private static final int MAXIMUM_PLAYER_ABILITY = 7;
    
    private static SimulatedPlayerBase makeSimulatedPlayer(int ability, GameMode mode) {
        SimulatedPlayerBase player;
        int lookAhead = 0;
        int poolSize = 0;
        boolean dimitris = false; // TODO: integrate the players in properly
        switch (ability) {
        case 1:
            player = new ScorePlayerBeta();
            if (mode.equals(GameMode.HIGHSCORE))
                return new ScorePlayerBeta();
            else if (mode.equals(GameMode.JELLY))
                return new RuleBasedJellyPlayer();
            else 
                break;
        case 2:
            lookAhead = 1;
            poolSize = 7;
            break;
        case 3:
            lookAhead = 1;
            poolSize = 15;
            break;
        case 4:
            lookAhead = 2;
            poolSize = 7;
            break;
        case 5:
            lookAhead = 2;
            poolSize = 15;
            break;
        case 6:
            lookAhead = 3;
            poolSize = 7;
            break;
        case 7:
            lookAhead = 3;
            poolSize = 15;
            break;
        default:
            player = new ScorePlayerAlpha();
            return player;
        }

        switch (mode) {
        case JELLY:
            if (dimitris) {
                player = new JellyRemoverPlayerLuna(lookAhead, poolSize);
            } else {
                player = new DepthPotentialJellyPlayer(lookAhead, poolSize);
            }
            break;
        case INGREDIENTS:
            player = new RuleBasedIngredientsPlayer();
            break;
        default:
            if (dimitris) {
                player = new MayanScorePlayer(lookAhead, poolSize);
            } else {
                player = new DepthPotentialScorePlayer(lookAhead, poolSize);
            }
            break;
        }
        return player;
    }

    public Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException {
        GameMode mode = level.levelDesign.getMode();
        checkPlayer(mode, ability);
        SimulatedPlayerBase player = players.get(mode).get(ability);
        Move move = player.calculateBestMove(level);
        if (move != null)
            return move;
        player.noMovesFound();
        return level.getValidMoves().get(0);
    }

    public static int getMaxAbilityLevel() {
        return MAXIMUM_PLAYER_ABILITY; // Decided by the switch in makeSimulatedPlayer()
    }

    private void checkPlayer(GameMode mode, int ability) {
        if (!players.containsKey(mode))
            players.put(mode, new HashMap<Integer, SimulatedPlayerBase>());
        HashMap<Integer, SimulatedPlayerBase> modePlayers = players.get(mode);
        if (!modePlayers.containsKey(ability))
            modePlayers.put(ability, makeSimulatedPlayer(ability, mode));
    }

    public void solve(GameState level, int ability) throws NoMovesFoundException {
        GameMode mode = level.levelDesign.getMode();
        checkPlayer(mode, ability);

        SimulatedPlayerBase player = players.get(mode).get(ability);
        player.solve(level);
    }
}
