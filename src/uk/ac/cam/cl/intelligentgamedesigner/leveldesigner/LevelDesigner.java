package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelDesigner implements Runnable {
	private static final int populationSize = 100;
	private static final int iterations = 2500;
	private static final double elitePercentage = 0.05;
	private static final double feasibleThreshold = 0.0;
	private static final double crossoverProbability = 0.8;

	private LevelDesignerManager manager;
    private List<LevelDesignIndividual> feasiblePopulation;
    private List<LevelDesignIndividual> infeasiblePopulation;
	private Random random;
	private int threadID;
	
	/**
	 * Constructs a LevelDesigner, generating a random population.
	 * 
	 * @param manager The LevelDesignerManager instance for this.
	 * @param random The Random instance to use to generate rangom numbers.
	 * @param threadID The ID of the thread, used when notifying the manager.
	 */
    public LevelDesigner(LevelDesignerManager manager, Random random, int threadID) {
		this.manager = manager;
		this.random = random;
		this.threadID = threadID;

		feasiblePopulation = new ArrayList<>();
		infeasiblePopulation = new ArrayList<>();

		// Generate the random population.
		List<LevelRepresentation> p = manager.getPopulation(populationSize, threadID);
		for (LevelRepresentation l : p) {
			LevelDesignIndividual individual = new LevelDesignIndividual(l);
			infeasiblePopulation.add(individual);
		}
    }
    
    /**
     * Generates and improves levels using a genetic algorithm, notifying the manager of changes.
     */
	@Override
    public void run() {
    	long startTime = System.currentTimeMillis();
    	
    	for (int i = 1; i < iterations + 1; i++) {
			List<LevelDesignIndividual> newFeasible = new ArrayList<>();
			List<LevelDesignIndividual> newInfeasible = new ArrayList<>();
			
			// Copy over the elite. Always copy over at least 1.
			int feasibleSize = feasiblePopulation.size();
			if (feasibleSize > 0) {
				int eliteNumber = Math.max(1, (int) (feasibleSize * elitePercentage + 0.5));
				for (int j = 0; j < eliteNumber; j++) {
					newFeasible.add(feasiblePopulation.get(j));
					feasibleSize--;
				}
			}
			
			iterate(feasiblePopulation, feasibleSize, newFeasible, newInfeasible);
			iterate(infeasiblePopulation, infeasiblePopulation.size(), newFeasible, newInfeasible);

			feasiblePopulation = newFeasible;
			infeasiblePopulation = newInfeasible;
			
			// Sort the individuals so they are in descending order of fitness.
			Collections.sort(feasiblePopulation, Collections.reverseOrder());
			
			if (i % 10 == 0) {
				DebugFilter.println("Iteration " + i, DebugFilterKey.LEVEL_DESIGN);
				DebugFilter.println("Top: ", DebugFilterKey.LEVEL_DESIGN);
				DebugFilter.println("Num feasible: " + feasiblePopulation.size(), DebugFilterKey.LEVEL_DESIGN);

                if (feasiblePopulation.size() > 0) {
                    manager.notifyInterfacePhase1(feasiblePopulation.get(0).getLevelRepresentation(), threadID);
                } else {
					manager.notifyInterfacePhase1(null, threadID);
				}
			}
			
			manager.notifyInterfacePhase1(i / (double) iterations, threadID);
		}
    	
    	DebugFilter.println("", DebugFilterKey.LEVEL_DESIGN);
		DebugFilter.println("Feasible: " + feasiblePopulation.size(), DebugFilterKey.LEVEL_DESIGN);
		DebugFilter.println("Infeasible: " + infeasiblePopulation.size(), DebugFilterKey.LEVEL_DESIGN);
    	double time = (System.currentTimeMillis() - startTime) / 1000.0;
		DebugFilter.println("Time: " + time, DebugFilterKey.LEVEL_DESIGN);

		// If there are still other LevelDesign instances running phase 1, wait for them to complete
		synchronized (manager) {
			if (!manager.isPhase1Complete(threadID)) {
				try {
					manager.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// Pass the top level design to the LevelDesignManager for it to assign an appropriate number of moves and
		// any objectives where appropriate.
		if (feasiblePopulation.size() > 0) {
			manager.notifyInterfacePhase2(feasiblePopulation.get(0).getLevelRepresentation(), threadID);
		}
    }
	
	/**
	 * Selects an individual from a population, with the probability of it being selected
	 * proportionate to its fitness.
	 * 
	 * @param population The population to select from.
	 * @param totalFitness The total fitness of the population.
	 * @return The individual that was selected.
	 */
	private LevelDesignIndividual stochasticSelection(List<LevelDesignIndividual> population, double totalFitness) {
		int length = population.size();
		while (true) {
			LevelDesignIndividual individual = population.get(random.nextInt(length));
			double fitness = individual.getFitness();
			double randomValue = random.nextDouble();
			if ((fitness == 0.0 && randomValue < 0.01) || (randomValue < fitness / totalFitness)) {
				return individual;
			}
		}
	}
    
	/**
	 * Perform one iteration of crossover and mutation on a particular population,
	 * separating it into feasible and infeasible at the end.
	 * 
	 * @param current The current population to iterate.
	 * @param numberToGenerate The number of new individuals to generate.
	 * @param newFeasible The list to hold the new feasible population.
	 * @param newInfeasible The list to hold the new infeasible population.
	 */
    private void iterate(List<LevelDesignIndividual> current,
						 int numberToGenerate,
						 List<LevelDesignIndividual> newFeasible,
						 List<LevelDesignIndividual> newInfeasible) {
    	
    	List<LevelRepresentation> newRepresentations = new ArrayList<>();

		// Calculate total fitness.
		double totalFitness = 0.0;
		for (LevelDesignIndividual individual : current) {
			totalFitness += individual.getFitness();
		}
		
		// Perform crossover to generate new level representations.
		// If there is an odd number to generate, generate one more, then remove one at random.
		int currentPopulationSize = current.size();
    	for (int i = 0; i < (numberToGenerate + 1) / 2; i++) {
    		// Get the parents using weighted random based on their fitness.
			LevelRepresentation mother = stochasticSelection(current, totalFitness).getLevelRepresentation();
			LevelRepresentation father = mother;
			if (currentPopulationSize > 1) {
				while (father == mother) {
					father = stochasticSelection(current, totalFitness).getLevelRepresentation();
				}
			}

			LevelRepresentation daughter = mother.clone();
			LevelRepresentation son = father.clone();

			if (random.nextDouble() <= crossoverProbability) {
				son.crossoverWith(daughter);
			}
			
			newRepresentations.add(son);
			newRepresentations.add(daughter);
		}
    	
    	// If numberToGenerate is odd, remove one to keep size constant.
    	if (numberToGenerate % 2 == 1) {
    		newRepresentations.remove(random.nextInt(newRepresentations.size()));
    	}
    	
    	// Mutate and split into the two new populations.
    	for (LevelRepresentation l : newRepresentations) {
    		l.mutate();
			LevelDesignIndividual mutated = new LevelDesignIndividual(l);
    		if (mutated.getFitness() > feasibleThreshold) {
				newFeasible.add(mutated);
			} else {
				newInfeasible.add(mutated);
			}
    	}
    }
    
	public void printIndividuals() {
		List<LevelDesignIndividual> population = new ArrayList<>(feasiblePopulation);
		Collections.sort(population);
		for (LevelDesignIndividual individual : population) {
			DebugFilter.println("", DebugFilterKey.LEVEL_DESIGN);
			((ArrayLevelRepresentation) individual.getLevelRepresentation()).printRepresentation();
			DebugFilter.println("Fitness: " + individual.getFitness(), DebugFilterKey.LEVEL_DESIGN);
		}
	}

}
