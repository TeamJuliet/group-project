package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.DesigningLevelScreen;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.InterfaceManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelDesignerManager extends SwingWorker {
    public static int NUMBER_TO_DISPLAY = DesigningLevelScreen.BOARD_COUNT;

    private long seed = 1;                      // The seed for debugging purposes
    private Specification specification;        // The specification
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
            levelDesigners[l]   = new LevelDesigner(this, this.originalRandoms[l], l, specification.getAccuracy());
        }
    }

    /**
     * This method is executed in the background, running separate instances of LevelDesign to generate a variety of
     * levels.
     *
     * @return
     * @throws InterruptedException
     */
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

    /**
     * This is called when this instance finishes executing doInBackground(), and indicates to the user interface
     * that the generation process is complete.
     */
    @Override
    public void done () {
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PHASE2_DONE, null, null);
    }




    // ******** PHASE 1 METHODS ********

    /**
     * This is for notifying the user interface whenever a level board has changed.
     *
     * @param topLevel  The board that has changed
     * @param threadID  The thread identifier for the calling LevelDesign instance
     */
    public synchronized void notifyInterfacePhase1(LevelRepresentation topLevel, int threadID) {
        this.topDesigns[threadID] = topLevel.getDesign();

        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_DESIGNS, null, this.topDesigns);
    }

    /**
     * This is for notifying the user interface whenever the progress has changed.
     *
     * @param progressValue The progress - a double between 0 and 1 inclusive
     * @param threadID      The thread identifier for the calling LevelDesign instance
     */
    public synchronized void notifyInterfacePhase1(double progressValue, int threadID) {
        this.progress[threadID] = progressValue;

        double min = progress[0];
        for (int t = 0; t < NUMBER_TO_DISPLAY; t++) {
            if (progress[t] < min) min = progress[t];
        }

        if (min > totalProgress) {
            totalProgress = min;
            firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PROGRESS, null, totalProgress);
        }
    }

    /**
     * This is a method used to synchronise the threads after completing phase 1. That is, the threads must wait
     * until they've all completed phase 1 before they can start phase 2.
     *
     * @param threadID  The identifier of the calling thread
     * @return          Whether the thread can proceed
     */
    public synchronized boolean isPhase1Complete(int threadID) {
        this.progress[threadID] = 1;

        for (int i = 0; i < NUMBER_TO_DISPLAY; i++) {
            if (progress[i] < 1) return false;
        }

        // If phase 1 has completed:

        // Notify the user interface
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PHASE1_DONE, null, null);

        // Reset the progress values and topDesigns ready for phase 2
        for (int i = 0; i < NUMBER_TO_DISPLAY; i++) {
            progress[i] = 0;
            topDesigns[i] = null;
        }


        // Notify all waiting threads that they can commence phase 2
        notifyAll();

        return true;
    }

    // ******** PHASE 2 METHODS ********

    /**
     * This method invokes the process of running the simulated players on a level design to assign the objectives
     * and the number of moves. It also notifies the user interface of progress as it proceeds.
     *
     * @param topLevel  The level to assess
     * @param threadID  The identifier of the calling thread
     */
    public synchronized void notifyInterfacePhase2(LevelRepresentation topLevel, int threadID) {
        Design topDesign = topLevel.getDesign();
        assignObjectiveAndMoves(topDesign, threadID);

        this.topDesigns[threadID] = topDesign;
        this.progress[threadID] = 1;

        totalProgress = 0;
        for (int i = 0; i < NUMBER_TO_DISPLAY; i++) {
            if (progress[i] == 1) totalProgress += (1.0 / (double) NUMBER_TO_DISPLAY);
        }

        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PROGRESS, null, totalProgress);
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_OBJECTIVES, null, this.topDesigns);

        DebugFilter.println("****************************", DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("THREAD " + threadID + " COMPLETE.", DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("Objective target: " + topDesign.getObjectiveTarget(), DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("Moves available:  " + topDesign.getNumberOfMovesAvailable(), DebugFilterKey.LEVEL_DESIGN);
    }




    // ******** FACTORY METHODS ********

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
        int min = specification.getMinCandies();
        int max = specification.getMaxCandies();

        DebugFilter.println("THREAD " + threadID, DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("MIN CANDIES:       " + specification.getMinCandies(), DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("MAX CANDIES:       " + specification.getMaxCandies(), DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("ICING DENSITY:     " + specification.getDesiredIcing(), DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("LIQUORICE DENSITY: " + specification.getDesiredLiquorice(), DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("JELLY DENSITY:     " + specification.getDesiredJelly(), DebugFilterKey.LEVEL_DESIGN);

        List<LevelRepresentation> population = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int numberOfCandyColours = originalRandoms[threadID].nextInt(max - min + 1) + min;
            LevelRepresentationParameters parameters = new LevelRepresentationParameters(
                    this.originalRandoms[threadID],
                    numberOfCandyColours,
                    this.specification.getDesiredIcing(),
                    this.specification.getDesiredLiquorice(),
                    this.specification.getDesiredJelly());

            switch (specification.getGameMode()) {
                case HIGHSCORE:
                    population.add(new ArrayLevelRepresentationScore(parameters));
                    break;
                case JELLY:
                    population.add(new ArrayLevelRepresentationJelly(parameters));
                    break;
                default:
                    population.add(new ArrayLevelRepresentationIngredients(parameters));
                    break;
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
            if (originalRandoms[threadID].nextDouble() > Math.abs(choices[c] - specification.getTargetFailRate())) {
                return c + 4;
            }
        }
    }




    // ******** MOVES AND OBJECTIVE ASSIGNMENT ********

    /**
     * This is a wrapper for assigning the objective and the number of moves to a design.
     *
     * @param design    The design to assign the parameters to
     */
    private void assignObjectiveAndMoves (Design design, int threadID) {

        // Specify the distribution of abilities that we want to run:
        int loopNumber = 1;
        int numberOfPlayersPerBatch = 5;
        if (specification.getAccuracy() == LevelDesignerAccuracy.MEDIUM) loopNumber *= 4;
        if (specification.getAccuracy() == LevelDesignerAccuracy.HIGH) loopNumber *= 8;

        int numberOfSimulations = numberOfPlayersPerBatch * loopNumber;
        int[] abilityDistribution = new int[numberOfSimulations];

        for (int i = 0; i < loopNumber * numberOfPlayersPerBatch; i += numberOfPlayersPerBatch) {
            abilityDistribution[i]      = 0;
            abilityDistribution[i + 1]  = 1;
            abilityDistribution[i + 2]  = 2;
            abilityDistribution[i + 3]  = 3;
            abilityDistribution[i + 3]  = 5;
        }

        switch (specification.getGameMode()) {
            case HIGHSCORE:
                // For highscore levels we pick a sensible number of moves and then adjust the objective score to meet
                // the target difficulty
                assignMovesHighscoreOrIngredients(design, threadID);
                assignHighscore(design, abilityDistribution);
                break;
            case JELLY:
                // For jelly levels we just set the number of moves depending on how long the players take
                // to clear the jellies
                assignMovesJelly(design, abilityDistribution);
                break;
            default:
                // For ingredient levels, we set the number of moves and then estimate the number of ingredients
                assignMovesHighscoreOrIngredients(design, threadID);
                assignIngredients(design, abilityDistribution);
                break;
        }
    }

    /**
     * This fixes the number of moves to for a given level design, such that easier levels are likely to be shorter
     * and harder levels are likely to be longer. This is called before the highscore objective is set.
     *
     * @param design    The level design to assign the moves to
     */
    private void assignMovesHighscoreOrIngredients (Design design, int threadID) {
        int minMoves = specification.getMinMoves();
        int maxMoves = specification.getMaxMoves();

        design.setNumberOfMovesAvailable(weightedParameterSelection(minMoves, maxMoves, threadID));
    }

    /**
     * This method assigns the highscore to a given level design by running simulated players on it. It assumes the
     * number of moves has already been set.
     *
     * @param design    The level design to assign the highscore to
     */
    private void assignHighscore (Design design, int[] abilityDistribution) {

        // Ensure that the simulated player doesn't encounter game over whilst making their moves
        design.setObjectiveTarget(Integer.MAX_VALUE);

        // Run simulations with the given abilities in multiple threads
        List<List<RoundStatistics>> gameStatistics = runSimulations(design, abilityDistribution);

        // Analyse the perfomance of the players
        DebugFilter.println("PLAYER PERFORMANCE: ", DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("------------------- ", DebugFilterKey.LEVEL_DESIGN);
        ArrayList<Integer> scores = new ArrayList<Integer>();
        int tempCount = 0;
        for (List<RoundStatistics> stats : gameStatistics) {
            if (stats.size() > 0) {
                scores.add(stats.get(stats.size() - 1).progress.score);
                DebugFilter.println("ABILITY " + abilityDistribution[tempCount] + " REACHED: " +
                        stats.get(stats.size() - 1).progress.score, DebugFilterKey.LEVEL_DESIGN);
            } else {
                scores.add(0);
            }
            tempCount++;
        }

        double objectiveTarget = findParameterWhichGivesFailRate(scores);
        int scoreRoundedTo10   = (((int) objectiveTarget + 5) / 10) * 10;
        design.setObjectiveTarget(scoreRoundedTo10);
    }

    /**
     * This fixes the number of moves to for a jelly level by running simulated players on it and assessing how long
     * they took to clear the jellies.
     */
    private void assignMovesJelly (Design design, int[] abilityDistribution) {
        // Give the players up to 100 moves
        design.setNumberOfMovesAvailable(100);

        // Run simulations with the given abilities in multiple threads
        List<List<RoundStatistics>> gameStatistics = runSimulations(design, abilityDistribution);

        // Analyse the perfomance of the players
        double numMoves = 0;
        DebugFilter.println("PLAYER PERFORMANCE: ", DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("------------------- ", DebugFilterKey.LEVEL_DESIGN);
        int tempCount1 = 0;
        for (List<RoundStatistics> stats : gameStatistics) {
            int tempCount2 = 0;
            for (RoundStatistics roundStatistics : stats) {
                int target = (specification.getGameMode() == GameMode.JELLY ?
                        roundStatistics.progress.jelliesRemaining : roundStatistics.progress.ingredientsRemaining);

                if (target > 0) {
                    numMoves++;
                    tempCount2++;
                } else {
                    break;
                }
            }
            DebugFilter.println("ABILITY " + abilityDistribution[tempCount1] + " REQUIRED: " + tempCount2 + " MOVES",
                    DebugFilterKey.LEVEL_DESIGN);
            tempCount1++;
        }

        // For now, convert the target difficulty range from [0.0, 1.0] to [1.5, 0.5] and multiply this by the
        // average number of moves taken:
        double difficultyShift = 1.5 - specification.getTargetFailRate();
        double numberOfMovesAvailable = difficultyShift * (numMoves / (double) gameStatistics.size());

        design.setNumberOfMovesAvailable((int) numberOfMovesAvailable);
    }

    private void assignIngredients (Design design, int[] abilityDistribution) {

        // Ensure the players don't face game over
        design.setObjectiveTarget(Integer.MAX_VALUE);

        // Run simulations with the given abilities in multiple threads
        List<List<RoundStatistics>> gameStatistics = runSimulations(design, abilityDistribution);

        // Analyse the perfomance of the players
        DebugFilter.println("PLAYER PERFORMANCE: ", DebugFilterKey.LEVEL_DESIGN);
        DebugFilter.println("------------------- ", DebugFilterKey.LEVEL_DESIGN);
        ArrayList<Integer> ingredientsCleared = new ArrayList<Integer>();
        int tempCount = 0;
        for (List<RoundStatistics> stats : gameStatistics) {
            if (stats.size() > 0) {
                int numCleared = Integer.MAX_VALUE - stats.get(stats.size() - 1).progress.ingredientsRemaining;
                ingredientsCleared.add(numCleared);
                DebugFilter.println("ABILITY " + abilityDistribution[tempCount] + " CLEARED: " +
                        numCleared, DebugFilterKey.LEVEL_DESIGN);
            } else {
                ingredientsCleared.add(0);
            }
            tempCount++;
        }

        int numIngredientsToClear = Math.max((int) findParameterWhichGivesFailRate(ingredientsCleared), 1);

        design.setObjectiveTarget(numIngredientsToClear);
    }




    // ******** HELPER FUNCTIONS ********

    /**
     * This function returns an integer in the range [min, max], such that a number towards min is more likely to
     * be picked for a low target difficulty, and a number towards max is more likely to be picked for a high target
     * difficulty.
     *
     * @param min       The minimum parameter value
     * @param max       The maximum parameter value
     * @param threadID  The threadID, to identify the correct Random instance to use
     * @return          The parameter described above
     */
    private int weightedParameterSelection (int min, int max, int threadID) {
        int suggestedNumber;

        while (true) {
            suggestedNumber = originalRandoms[threadID].nextInt(max - min + 1) + min;

            // Map the suggested parameter in the range [min, max]
            // to a double in the range [0.0, 1.0]
            double mappedValue = (suggestedNumber - min) / (double) (max - min);
            double diff = Math.abs(specification.getTargetFailRate() - mappedValue);

            // This means we're more likely to pick a lower parameter for easy levels, and a higher parameter for
            // harder levels
            if (originalRandoms[threadID].nextDouble() > diff) break;
        }

        return suggestedNumber;
    }

    private double findParameterWhichGivesFailRate (ArrayList<Integer> parameters) {
        // Sort the scores, and scan through them until the approximate fail rate is correct
        Collections.sort(parameters);
        double fraction = 1 / (double) parameters.size();
        double proportionFailed = 0;
        int count = 0;
        while (proportionFailed < specification.getTargetFailRate()) {
            proportionFailed += fraction;
            count++;
        }

        double upper;
        if (count >= parameters.size()) {
            double averageDiff = 0;
            for (int i = 1; i < parameters.size(); i++) {
                averageDiff += parameters.get(i) - parameters.get(i - 1);
            }
            averageDiff = averageDiff / (double) (parameters.size() - 1);
            upper = parameters.get(parameters.size() - 1) + averageDiff;
        } else {
            upper = parameters.get(count);
        }
        double lower = parameters.get(count - 1);
        return (upper + lower) / 2;
    }

    /**
     * This method is a helper method for running simulations in parallel with players of the abilities specified in
     * abilityDistribution.
     *
     * @param design                The level design to run the players on
     * @param abilityDistribution   The abilities of the simulated players to be run
     * @return                      The game statistics for all of the simulations
     */
    private static List<List<RoundStatistics>> runSimulations (Design design,
                                                            int[] abilityDistribution) {
        int numberOfSimulations = abilityDistribution.length;
        GameState[] gameStates = new GameState[numberOfSimulations];
        List<List<RoundStatistics>> gameStatistics = new ArrayList<>(numberOfSimulations);
        Thread[] simulationThreads = new Thread[numberOfSimulations];

        for (int t = 0; t < numberOfSimulations; t++) {
            gameStates[t] = new GameState(design);      // Generate a new game
            gameStatistics.add(new ArrayList<RoundStatistics>());      // Generate a reference to be passed to each simulation
            simulationThreads[t] = new Thread(new SimulationThread(gameStates[t], abilityDistribution[t],
                    gameStatistics.get(t)));            // Create the thread to run that simulation

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

        return gameStatistics;
    }

    // Given a design and a simulated player ability, this returns the proportion of players who passed the level
    public static double calculatePassRate (int ability, Design design) {
        int numberOfSimulations = 20 - (ability * 2);
        int[] abilityDistribution = new int[numberOfSimulations];
        for (int i = 0; i < numberOfSimulations; i++) abilityDistribution[i] = ability;

        // Run the simulations
        List<List<RoundStatistics>> gameStatistics = runSimulations(design, abilityDistribution);

        // Calculate the number who passed
        int numPassed = 0;
        for (List<RoundStatistics> stats : gameStatistics) {
            switch (design.getMode()) {
                case HIGHSCORE:
                    if (stats.get(stats.size() - 1).progress.score >= design.getObjectiveTarget()) numPassed++;
                    break;
                case JELLY:
                    if (stats.get(stats.size() - 1).progress.jelliesRemaining == 0) numPassed++;
                    break;
                default:
                    if (stats.get(stats.size() - 1).progress.ingredientsRemaining == 0) numPassed++;
                    break;
            }
        }

        return numPassed / (double) numberOfSimulations;
    }
}