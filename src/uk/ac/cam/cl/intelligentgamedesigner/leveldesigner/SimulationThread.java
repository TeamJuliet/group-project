package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

public class SimulationThread implements Runnable {

    private GameState level;
    private int playerAbility;

    public SimulationThread (GameState level, int playerAbility) {
        this.level = level;
        this.playerAbility = playerAbility;
    }

    @Override
    public void run() {
        int movesMade = 1;
        try {
            SimulatedPlayerBase simulatedPlayerBase = SimulatedPlayerManager.makeSimulatedPlayer(playerAbility);
            while (level.getGameProgress().movesRemaining > 0 && !level.didFailShuffle()) {
                Move bestMove = simulatedPlayerBase.calculateBestMove(level);
                if (bestMove != null) level.makeFullMove(bestMove);
                movesMade++;
            }
            DebugFilter.println("Game complete. Ability: " + playerAbility + ", moves: " + movesMade, DebugFilterKey.LEVEL_DESIGN);
        } catch (NoMovesFoundException e) {
            System.err.println("Simulated player couldn't make a move.");
            System.err.println("Player ability: " + playerAbility + "; Move: " + movesMade);
            System.err.println("GameState:");
            System.err.println(level.toString());
            System.err.println("Valid moves: " + level.getValidMoves());
        } catch (InvalidMoveException e) {
            System.err.println("Simulated player made an invalid move.");
            System.err.println("Player ability: " + playerAbility + "; Move: " + movesMade);
            System.err.println("GameState:");
            System.err.println(level.toString());
            System.err.println("Valid moves: " + level.getValidMoves());
        }
    }
}
