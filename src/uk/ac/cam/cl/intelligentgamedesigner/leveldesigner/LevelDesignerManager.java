package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.DesigningLevelScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDesignerManager extends SwingWorker {
    public static int NUMBER_TO_DISPLAY = DesigningLevelScreen.BOARD_COUNT;

    private long seed = 1;                      // The seed for debugging purposes
    private Specification specification;        // The
    private Random originalRandoms[];           // Can't use one random for debugging because of concurrency
    private LevelDesigner[] levelDesigners;     // The level designers
    private Design[] topDesigns;                // The top design of each thread
    private double[] progress;                  // The progress of each thread
    private double totalProgress;               // For keeping track of the slowest thread

    public LevelDesignerManager (Specification specification) {
        this.specification      = specification;
        this.originalRandoms    = new Random[NUMBER_TO_DISPLAY];
        this.levelDesigners     = new LevelDesigner[NUMBER_TO_DISPLAY];
        this.topDesigns         = new Design[NUMBER_TO_DISPLAY];
        this.progress           = new double[NUMBER_TO_DISPLAY];
        this.totalProgress      = 0;

        for (int l = 0; l < NUMBER_TO_DISPLAY; l++) {
            // NOTE: If you want to debug, change this to: new Random(seed)
            originalRandoms[l]  = new Random(System.nanoTime());
            levelDesigners[l]   = new LevelDesigner(this, this.originalRandoms[l], l);
        }
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        Thread[] generationThreads = new Thread[NUMBER_TO_DISPLAY];

        // Start generations
        for (int t = 0; t < NUMBER_TO_DISPLAY; t++) {
            generationThreads[t] = new Thread(levelDesigners[t]);
            generationThreads[t].setDaemon(true);
            generationThreads[t].start();
        }

        // Wait for generations to finish
        for (int t = 0; t < NUMBER_TO_DISPLAY; t++) {
            generationThreads[t].join();
        }

        return null;
    }

    @Override
    public void done () {
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PHASE2_DONE, null, null);
    }

    /**
     * This is for notifying the user interface whenever a level board has changed.
     *
     * @param top       The board that has changed
     * @param threadID  The thread identifier for the calling LevelDesign instance
     */
    public synchronized void notifyInterface(LevelRepresentation topLevel, int threadID) {
        this.topDesigns[threadID] = topLevel.getDesign();

        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_DESIGNS, null, this.topDesigns);
    }

    /**
     * This is for notifying the user interface whenever the progress has changed.
     *
     * @param progressValue The progress - a double between 0 and 1 inclusive
     * @param threadID      The thread identifier for the calling LevelDesign instance
     */
    public synchronized void notifyInterface(double progressValue, int threadID) {
        this.progress[threadID] = progressValue;

        double min = progress[0];
        for (int t = 0; t < NUMBER_TO_DISPLAY; t++) {
            if (progress[t] < min) min = progress[t];
        }

        if (min > totalProgress) {
            totalProgress = min;
            firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PROGRESS, null, totalProgress);
        }

        // This is true when the final thread finishes phase 1
        if (totalProgress >= 1.0) {
            // Notify the interface that the board layouts have been generated
            firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PHASE1_DONE, null, null);

            // Reset the progress
            for (int i = 0; i < NUMBER_TO_DISPLAY; i++) {
                progress[i] = 0;
            }
            totalProgress = 0;
        }
    }

    public synchronized void assignObjectives(LevelRepresentation topLevel, int threadID) {
        Design topDesign = topLevel.getDesign();
        runPlayersAndAssignObjectives(topDesign);

        this.topDesigns[threadID] = topDesign;
        this.progress[threadID] = 1;

        totalProgress = 0;
        for (int i = 0; i < NUMBER_TO_DISPLAY; i++) {
            if (progress[i] == 1) totalProgress += (1.0 / (double) NUMBER_TO_DISPLAY);
        }

        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PROGRESS, null, totalProgress);
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_OBJECTIVES, null, this.topDesigns);
    }

    /**
     * This is a factory method for producing the appropriate level representations for the calling LevelDesign
     * instance.
     *
     * @param size      The requested size of the population to be generated
     * @param threadID  The thread identifier for the calling LevelDesign instance
     * @return          A list of level representations - i.e. a population
     */
    public synchronized List<LevelRepresentation> getPopulation(int size, int threadID) {

        // Calculate the number of candy colours to be used
        int numberOfCandyColours = getNumberOfCandyColours(threadID);

        List<LevelRepresentation> population = new ArrayList<>();

        switch (specification.getGameMode()) {
            case HIGHSCORE:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationScore(originalRandoms[threadID], numberOfCandyColours));
                }
                break;
            case JELLY:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationJelly(originalRandoms[threadID], numberOfCandyColours));
                }
                break;
            default:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationIngredients(originalRandoms[threadID], numberOfCandyColours));
                }
        }

        return population;
    }

    /**
     * This method returns the number of candy colours that should be used in the design, weighting its decision on the
     * target difficulty given in the specification (i.e. it's more likely to return 4 for easy levels, 5 for medium
     * levels and 6 for hard levels).
     *
     * @return The candy colour to be used.
     */
    private int getNumberOfCandyColours (int threadID) {
        double[] choices = {0.0, 0.5, 1.0};

        int c;
        while (true) {
            c = originalRandoms[threadID].nextInt(3);
            if (originalRandoms[threadID].nextDouble() > Math.abs(choices[c] - specification.getTargetDifficulty())) {
                return c + 4;
            }
        }
    }

    /**
     * This method will run appropriate simulated players on the given design and evaluate how fun and appropriately
     * difficult the level is based on their performance and GameState-generated statistics. The gameplay fitness
     * will be a value between 0 and 1.
     *
     * @param design    The design of the level
     * @return          The gameplay fitness, 0 <= d <= 1
     */
    public double getGameplayFitness (Design design) {

        // TODO: Make this more conservative - initially try the design on simple players before testing it on
        // TODO: advanced ones (which are likely to be more expensive to run)

        // For now, just create one player of each ability
        int numberOfSimulations = 1;//SimulatedPlayerManager.getMaxAbilityLevel();

        // The games to be played
        GameState[] gameStates = new GameState[numberOfSimulations];

        // The different abilities of players we want for each simulation
        int[] abilityDistribution = new int[numberOfSimulations];

        // Each simulation will add information to each list in this list of lists
        List<List<RoundStatistics>> gameStatistics = new ArrayList<>(numberOfSimulations);

        // The actual threads to run the simulations
        Thread[] simulationThreads = new Thread[numberOfSimulations];

        for (int t = 0; t < numberOfSimulations; t++) {
            // Generate a new game
            gameStates[t] = new GameState(design);

            // Generate a reference to a list of statistics which can be passed to a simulation
            gameStatistics.add(new ArrayList<>());

            // Generate an ability level you'd like for testing that game
            abilityDistribution[t] = t;

            // Create and start a thread for running that simulation
            simulationThreads[t] = new Thread(new SimulationThread(gameStates[t], abilityDistribution[t],
                    gameStatistics.get(t)));

            simulationThreads[t].setDaemon(true);
            simulationThreads[t].start();
        }

        try {
            // Wait for all of the players to finish playing
            for (int t = 0; t < numberOfSimulations; t++) {
                simulationThreads[t].join();
            }
        } catch (InterruptedException e) {
            // If the user clicks 'Back' during the level generation process, the simulations may be interrupted
            DebugFilter.println("Simulation interrupted.", DebugFilterKey.LEVEL_DESIGN);
        }

        // Evaluate the performance of the players, and how 'fun' the simulations were
        double totalDifficulty = 0;
        double totalFun = 0;
        for (int t = 0; t < numberOfSimulations; t++) {
            totalDifficulty += DifficultyChecker.estimateDifficulty(gameStates[t], design);
            totalFun += FunChecker.getFunFitness(gameStatistics.get(t));
        }

        double estimatedDifficulty = totalDifficulty / (double) numberOfSimulations;
        double difficultyFitness = 1 - Math.abs(estimatedDifficulty - specification.getTargetDifficulty());
        double funFitness = totalFun / (double) numberOfSimulations;

        return (difficultyFitness + funFitness) / 2.0;
    }

    /**
     * This method will run a given design on simulated players and assign the number of moves, score to reach and
     * number of ingredients where appropriate.
     *
     * @param design
     */
    private void runPlayersAndAssignObjectives (Design design) {
        switch (specification.getGameMode()) {
            case HIGHSCORE:
                design.setObjectiveTarget(1000);
                break;
            case INGREDIENTS:
                design.setObjectiveTarget(3);
                break;
        }

        design.setNumberOfMovesAvailable(100);
    }
}
