package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LevelDesigner {
	private static final int populationSize = 100;
	private static final int iterations = 1000;
	private static final double aestheticThreshold = 0.5;
	private static final double elitePercentage = 0.05;
	private static final double crossoverProbability = 0.8;

	private LevelDesignerManager manager;
    private List<Individual> feasiblePopulation;
    private List<Individual> infeasiblePopulation;
	private Random random;
    
    private static class Individual implements Comparable<Individual> {
    	private final LevelRepresentation levelRepresentation;
    	private final double aestheticFitness;
		private double difficultyFitness;
		private Design design = null;
    	
    	public Individual(LevelRepresentation levelRepresentation) {
    		this.levelRepresentation = levelRepresentation;
			this.aestheticFitness = levelRepresentation.getAestheticFitness();
    	}
    	
    	public boolean isFeasible() {
        	return this.aestheticFitness >= aestheticThreshold;
        }

		public double getFitness() {
			double fitness = aestheticFitness;
			if (isFeasible()) {
				// TODO this needs to be tweaked, need to decide how fitnesses will be combined.
				fitness += difficultyFitness;
			}
			return fitness;
		}

		public Design getDesign() {
			if (design == null) {
				design = levelRepresentation.getDesign();
			}
			return design;
		}

		@Override
		public int compareTo(Individual individual) {
			double compared = this.getFitness() - individual.getFitness();
			// Can't just return compared cast to an int, need to be careful with rounding.
			return (compared > 0 ? 1 : compared < 0 ? -1 : 0);
		}
    }

    public LevelDesigner(LevelDesignerManager manager, Random random) {
		this.manager = manager;
		this.random = random;

		feasiblePopulation = new ArrayList<>();
		infeasiblePopulation = new ArrayList<>();

		// Generate the random population.
		List<LevelRepresentation> p = manager.getPopulation(populationSize);
		for (LevelRepresentation l : p) {
			Individual individual = new Individual(l);
			infeasiblePopulation.add(individual);
		}
    }

    public void run() {
    	long startTime = System.currentTimeMillis();
    	
    	for (int i = 0; i < iterations; i++) {
			List<Individual> newFeasible = new ArrayList<>();
			List<Individual> newInfeasible = new ArrayList<>();
			
			// Copy over the elite. Always copy over at least 1.
			int feasibleSize = feasiblePopulation.size();
			if (feasibleSize > 0) {
				int eliteNumber = Math.max(1, (int) (feasibleSize * elitePercentage + 0.5));
				Collections.sort(feasiblePopulation);
				for (int j = 0; j < eliteNumber; j++) {
					feasibleSize--;
					newFeasible.add(feasiblePopulation.get(feasibleSize));
				}
			}
			
			iterate(feasiblePopulation, feasibleSize, newFeasible, newInfeasible);
			iterate(infeasiblePopulation, infeasiblePopulation.size(), newFeasible, newInfeasible);

			feasiblePopulation = newFeasible;
			infeasiblePopulation = newInfeasible;

			for (Individual individual : newFeasible) {
				individual.difficultyFitness = manager.getDifficultyFitness(individual.getDesign());
			}
			
			if (i % 100 == 0) {
				System.out.println("Iteration " + i);
			}
		}
    	
    	System.out.println();
    	System.out.println("Feasible: " + feasiblePopulation.size());
    	System.out.println("Infeasible: " + infeasiblePopulation.size());
    	double time = (System.currentTimeMillis() - startTime) / 1000.0;
    	System.out.println("Time: " + time);
    }

	private Individual stochasticSelection(List<Individual> population, double totalFitness) {
		int length = population.size();
		while (true) {
			Individual individual = population.get(random.nextInt(length));
			if (random.nextDouble() < individual.getFitness() / totalFitness) {
				return individual;
			}
		}
	}
    
    private void iterate(List<Individual> current,
						 int numberToGenerate,
						 List<Individual> newFeasible,
						 List<Individual> newInfeasible) {
    	
    	List<LevelRepresentation> newRepresentations = new ArrayList<>();

		// Calculate total fitness.
		double totalFitness = 0.0;
		for (Individual individual : current) {
			totalFitness += individual.getFitness();
		}
		
		// Perform crossover to generate new level representations.
		// If there is an odd number to generate, generate one more, then remove one at random.
		int currentPopulationSize = current.size();
    	for (int i = 0; i < (numberToGenerate + 1) / 2; i++) {
    		// Get the parents using weighted random based on their fitness.
			LevelRepresentation mother = stochasticSelection(current, totalFitness).levelRepresentation;
			LevelRepresentation father = mother;
			if (currentPopulationSize > 1) {
				while (father == mother) {
					father = stochasticSelection(current, totalFitness).levelRepresentation;
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
    		Individual mutated = new Individual(l);
    		if (mutated.isFeasible()) {
				newFeasible.add(mutated);
			} else {
				newInfeasible.add(mutated);
			}
    	}
    }

	public void printBestIndividual() {
		Collections.sort(feasiblePopulation);
		
		for (Individual individual : feasiblePopulation) {
			System.out.println();
			((ArrayLevelRepresentation) individual.levelRepresentation).printBoard();
			System.out.println("Fitness: " + individual.getFitness());
		}
	}

}
