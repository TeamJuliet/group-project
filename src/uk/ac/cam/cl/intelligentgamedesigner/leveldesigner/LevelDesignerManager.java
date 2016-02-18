package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScorePlayerAlpha;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;

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
    protected Void doInBackground() throws Exception {
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

        GameState[] gameStates = new GameState[numberOfSimulations];
        int[] abilityDistribution = new int[numberOfSimulations];
        Thread[] simulationThreads = new Thread[numberOfSimulations];

        for (int t = 0; t < numberOfSimulations; t++) {
            // Generate a new game
            gameStates[t] = new GameState(design);

            // Generate an ability level you'd like for testing that game
            abilityDistribution[t] = t;

            // Create and start a thread for running that simulation
            simulationThreads[t] = new Thread(new SimulationThread(gameStates[t], abilityDistribution[t]));

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

        // Evaluate the performance of the players, and how 'fun' the simulations were
        double totalDifficulty = 0;
        double totalFun = 0;
        for (int t = 0; t < numberOfSimulations; t++) {
            totalDifficulty += DifficultyChecker.estimateDifficulty(gameStates[t], design);
            // TODO: Get a list of ProcessStateStatsViewer and CandiesAccumulatorView for FunChecker
            totalFun += FunChecker.getFunFitness(null);
        }

        double estimatedDifficulty = totalDifficulty / (double) numberOfSimulations;
        double difficultyFitness = Math.abs(estimatedDifficulty - specification.getTargetDifficulty());

        double funFitness = totalFun / (double) numberOfSimulations;

        return (difficultyFitness + funFitness) / 2.0;
    }
}
