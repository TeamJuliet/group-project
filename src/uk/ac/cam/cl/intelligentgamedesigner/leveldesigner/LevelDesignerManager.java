package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDesignerManager extends SwingWorker {
    private long seed = 1;
    private Specification specification;
    private Random originalRandom;
    protected LevelDesigner levelDesigner;

    public LevelDesignerManager (Specification specification) {
        this.specification = specification;

        this.originalRandom = new Random(seed);
        this.levelDesigner = new LevelDesigner(this, this.originalRandom);
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        this.levelDesigner.run();

        return null;
    }

    @Override
    public void done () {
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_DONE, null, null);
    }
    
    public void notifyInterface(List<LevelRepresentation> top) {
    	firePropertyChange(PropertyChanges.PROPERTY_CHANGE_DESIGNS, null, top);
    }

    public void notifyInterface(int iterationNumber) {
        firePropertyChange(PropertyChanges.PROPERTY_CHANGE_PROGRESS, null, iterationNumber);
    }

    public List<LevelRepresentation> getPopulation(int size) {

        // Calculate the number of candy colours to be used
        int numberOfCandyColours = getNumberOfCandyColours();

        List<LevelRepresentation> population = new ArrayList<>();

        switch (specification.getGameMode()) {
            case HIGHSCORE:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationScore(originalRandom, numberOfCandyColours));
                }
                break;
            case JELLY:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationJelly(originalRandom, numberOfCandyColours));
                }
                break;
            default:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationIngredients(originalRandom, numberOfCandyColours));
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
    public int getNumberOfCandyColours () {
        double[] choices = {0.0, 0.5, 1.0};

        int c;
        while (true) {
            c = originalRandom.nextInt(3);
            if (originalRandom.nextDouble() > Math.abs(choices[c] - specification.getTargetDifficulty())) {
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
}
