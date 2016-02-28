package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

import java.util.List;

public class SimulationThread implements Runnable {

    private GameState level;
    private int playerAbility;
    private List<RoundStatistics> gameStatistics;

    /**
     * This constructor gives the simulation thread everything it needs to run independently.
     *
     * @param level             The game level to be played
     * @param playerAbility     The ability of the player to play it
     * @param gameStatistics    Somewhere to store all of the statistics about the simulation
     */
    public SimulationThread (GameState level, int playerAbility, List<RoundStatistics> gameStatistics) {
        this.level = level;
        this.playerAbility = playerAbility;
        this.gameStatistics = gameStatistics;
    }

    /**
     * This method runs a simulated player with the ability given in the constructor and saves statistics about the
     * game after each round.
     */
    @Override
    public void run() {
        int movesMade = 1;
        try {
            SimulatedPlayerManager playerManager = new SimulatedPlayerManager();
            while (!level.isGameOver() && !level.didFailShuffle()) {

                // Query the appropriate simulated player for the next best move
                Move bestMove = playerManager.calculateBestMove(level, playerAbility);
                if (bestMove != null) level.makeFullMove(bestMove);
                movesMade++;

                // Add the statistics for this given round
                gameStatistics.add(level.getRoundStatistics());
            }
            /*DebugFilter.println("Game complete. Ability: " + playerAbility + ", moves: " + movesMade, DebugFilterKey
                    .LEVEL_DESIGN);*/

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
