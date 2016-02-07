package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDesigner {
	private static final int populationSize = 100;
	private static final double aestheticThreshold = 0.5;
	private static final double crossoverProbability = 0.8;

	private LevelDesignerManager manager;
    private List<Individual> feasiblePopulation;
    private List<Individual> infeasiblePopulation;
	private Random random;
    
    private static class Individual {
    	public final LevelRepresentation levelRepresentation;
    	public double aestheticFitness;
		public double difficultyFitness;
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
    	for (int i = 0; i < 1000; i++) {
			List<Individual> newFeasible = new ArrayList<>();
			List<Individual> newInfeasible = new ArrayList<>();
			
			int feasibleSize = feasiblePopulation.size();
			Individual fittest = getFittest(feasiblePopulation);
			if (fittest != null) {
				newFeasible.add(fittest);
				feasibleSize--;
			}

			iterate(feasiblePopulation, feasibleSize, newFeasible, newInfeasible);
			iterate(infeasiblePopulation, infeasiblePopulation.size(), newFeasible, newInfeasible);

			feasiblePopulation = newFeasible;
			infeasiblePopulation = newInfeasible;

			for (Individual individual : newFeasible) {
				individual.difficultyFitness = manager.getDifficultyFitness(individual.getDesign());
			}
			
			if (i % 10 == 0) {
				System.out.println("Iteration " + i);
			}
		}
    	
    	System.out.println("Feasible: " + feasiblePopulation.size());
    	System.out.println("Infeasible: " + infeasiblePopulation.size());
    }
    
    private Individual getFittest(List<Individual> population) {
    	if (population.size() == 0) {
    		return null;
    	}
    	
    	Individual fittest = feasiblePopulation.get(0);
		for (Individual individual : feasiblePopulation) {
			if (individual.getFitness() > fittest.getFitness()) {
				fittest = individual;
			}
		}
		
		return fittest;
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
		Individual fittest = getFittest(feasiblePopulation);

		if (fittest == null) {
			System.out.println("No feasible solutions.");
			return;
		}

		((ArrayLevelRepresentation) fittest.levelRepresentation).printBoard();
		System.out.println("Fitness: " + fittest.levelRepresentation.getAestheticFitness());
	}

}
