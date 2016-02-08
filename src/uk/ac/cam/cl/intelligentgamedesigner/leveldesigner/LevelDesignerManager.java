package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScorePlayerAlpha;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDesignerManager {
    private long seed = 1;
    private Specification specification;
    private Random originalRandom;
    protected LevelDesigner levelDesigner;

    public LevelDesignerManager (Specification specification) {
        this.specification = specification;

        this.originalRandom = new Random(seed);
        this.levelDesigner = new LevelDesigner(this, this.originalRandom);
    }

    public void run() {
        this.levelDesigner.run();
    }

    public List<LevelRepresentation> getPopulation(int size) {
        List<LevelRepresentation> population = new ArrayList<>();

        switch (specification.getGameMode()) {
            case HIGHSCORE:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationScore(originalRandom));
                }
                break;
            case JELLY:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationJelly(originalRandom));
                }
                break;
            default:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationIngredients(originalRandom));
                }
        }

        return population;
    }

    /**
     * This method will run appropriate simulated players on the given design and evaluate how difficult the level is
     * based on their performance. The difficulty fitness will be a value between 0 and 1.
     *
     * @param design    The design of the level
     * @return          The difficult fitness, 0 <= d <= 1
     */
    public double getDifficultyFitness (Design design) {

        // TODO: Make this more conservative - initially try the design on simple players before testing it on
        // TODO: advanced ones (which are likely to be more expensive to run)

        int numberOfSimulations = 10;

        GameState[] gameStates = new GameState[numberOfSimulations];
        SimulatedPlayerBase[] simulatedPlayers = new SimulatedPlayerBase[numberOfSimulations];
        Thread[] simulationThreads = new Thread[numberOfSimulations];

        for (int t = 0; t < numberOfSimulations; t++) {
            gameStates[t] = new GameState(design);

            switch (design.getMode()) {
                case HIGHSCORE:
                    simulatedPlayers[t] = new ScorePlayerAlpha(gameStates[t]);
                    break;
                case JELLY:
                    System.err.println("Jelly level players are not yet supported.");
                    return 0;
                default:
                    System.err.println("Ingredients level players are not yet supported.");
                    return 0;
            }

            simulationThreads[t] = new Thread(new SimulationThread(simulatedPlayers[t]));

            simulationThreads[t].setDaemon(true);
            simulationThreads[t].start();
        }

        // Wait for all of the players to finish playing
        for (int t = 0; t < numberOfSimulations; t++) {
            try {
                simulationThreads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Evaluate the performance of the players
        double totalDifficulty = 0;
        for (int t = 0; t < numberOfSimulations; t++) {

            switch (design.getMode()) {
                case HIGHSCORE:
                    totalDifficulty += evaluateScoreLevelPerformance(gameStates[t], design);
                    break;
                case JELLY:
                    totalDifficulty += evaluateJellyLevelPerformance(gameStates[t], design);
                    break;
                default:
                    totalDifficulty += evaluateIngredientsLevelPerformance(gameStates[t], design);
                    break;
            }
        }

        return totalDifficulty / (double) numberOfSimulations;
    }

    /**
     * Currently, this will return a fitness between 0 (trivial) and 1 (impossible).
     *
     * The simulated players play until they have no more moves remaining, thus the only values that can be used to
     * judge performance are the score and the target score. The values will be returned following this scale:
     *
     * 0 -------------------------------------- 0.5 -------------------------------------- 1
     * EASY                                     OK                                      HARD
     * score - target score >> 0         score - target = 0              score - target << 0
     *
     * For this I will use 1 minus the sigmoid function centered on the target score and scaled appropriately.
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private double evaluateScoreLevelPerformance (GameState gameState, Design design) {
        double center = design.getObjectiveTarget();
        double x = gameState.getScore();

        return 1 - (1 / (1 + (Math.exp(-(x - center) / center))));
    }

    /**
     * For jelly levels, the players will either manage to clear all of the jellies, or they will not. Thus, the only
     * value that can be used to judge performance is the number of cells remaining with jelly layers above them.
     * Other metrics such as how scattered the remaining jellies are may also be taken into account at a later date.
     *
     * Currently this returns difficulty as 1 - e^(- (num jellies remaining / c)) for some constant c (= 10 for now)
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private double evaluateJellyLevelPerformance (GameState gameState, Design design) {

        Cell[][] gameBoard = gameState.getBoard();
        double numberOfJelliesRemaining = 0;

        for (int x = 0; x < gameState.getWidth(); x++) {
            for (int y = 0; y < gameState.getHeight(); y++) {
                numberOfJelliesRemaining += gameBoard[x][y].getJellyLevel();
            }
        }

        return 1 - Math.exp(-(numberOfJelliesRemaining / 10.0));
    }

    /**
     * For ingredients levels, the players will either manage to clear all of the ingredients, or they will not.
     * Thus, the only value that can be used to judge performance is the number of ingredients remaining (and perhaps
     * the initial number to start out with).
     *
     * Currently this returns difficulty as 1 - e^(- (num ingredients remaining / c)) for some constant c (= 2 for now)
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private double evaluateIngredientsLevelPerformance (GameState gameState, Design design) {

        return 1 - Math.exp(-(gameState.getIngredientsRemaining() / 2.0));
    }
}
