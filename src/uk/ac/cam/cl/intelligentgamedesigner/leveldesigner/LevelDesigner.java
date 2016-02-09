package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelDesigner {
	private static final int populationSize = 100;
	private static final int iterations = 1000;
	private static final double elitePercentage = 0.05;
	private static final double feasibleThreshold = 0.5;
	private static final double crossoverProbability = 0.8;
	private static final int maxTopLevels = 5;

	private LevelDesignerManager manager;
    private List<LevelDesignIndividual> feasiblePopulation;
    private List<LevelDesignIndividual> infeasiblePopulation;
	private Random random;

    public LevelDesigner(LevelDesignerManager manager, Random random) {
		this.manager = manager;
		this.random = random;

		feasiblePopulation = new ArrayList<>();
		infeasiblePopulation = new ArrayList<>();

		// Generate the random population.
		List<LevelRepresentation> p = manager.getPopulation(populationSize);
		for (LevelRepresentation l : p) {
			LevelDesignIndividual individual = new LevelDesignIndividual(l);
			infeasiblePopulation.add(individual);
		}
    }

    public void run() {
    	long startTime = System.currentTimeMillis();
    	
    	for (int i = 0; i < iterations; i++) {
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

			for (LevelDesignIndividual individual : newFeasible) {
				individual.setDifficultyFitness(0.0);//manager.getDifficultyFitness(individual.getDesign()));
			}
			
			// Sort the individuals so they are in descending order of fitness.
			Collections.sort(feasiblePopulation, Collections.reverseOrder());
			
			if (i % 100 == 0) {
				System.out.println("Iteration " + i);
				List<LevelRepresentation> l = new ArrayList<>();
				int max = Math.min(feasiblePopulation.size(), maxTopLevels);
				for (int j = 0; j < max; j++) {
					l.add(feasiblePopulation.get(j).getLevelRepresentation());
				}
				manager.notifyInterface(l);
			}
		}
    	
    	System.out.println();
    	System.out.println("Feasible: " + feasiblePopulation.size());
    	System.out.println("Infeasible: " + infeasiblePopulation.size());
    	double time = (System.currentTimeMillis() - startTime) / 1000.0;
    	System.out.println("Time: " + time);
    }

	private LevelDesignIndividual stochasticSelection(List<LevelDesignIndividual> population, double totalFitness) {
		int length = population.size();
		while (true) {
			LevelDesignIndividual individual = population.get(random.nextInt(length));
			if (random.nextDouble() < individual.getFitness() / totalFitness) {
				return individual;
			}
		}
	}
    
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
    		if (mutated.getFeasibility() > feasibleThreshold) {
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
			System.out.println();
			((ArrayLevelRepresentation) individual.getLevelRepresentation()).printRepresentation();
			System.out.println("Fitness: " + individual.getFitness());
		}
	}

}
