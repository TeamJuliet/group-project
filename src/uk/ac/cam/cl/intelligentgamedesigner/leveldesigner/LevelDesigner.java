package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;

public class LevelDesigner {
	private static final int populationSize = 100;
	private static final double aestheticThreshold = 0.5;
	
    private LevelRepresentationFactory levelRepresentationFactory;
    private Population<Individual> feasiblePopulation;
    private Population<Individual> infeasiblePopulation;
    
    private static class Individual {
    	public final LevelRepresentation levelRepresentation;
    	private double aestheticFitness;
    	
    	public Individual(LevelRepresentation levelRepresentation) {
    		this.levelRepresentation = levelRepresentation;
    		this.aestheticFitness = levelRepresentation.getAestheticFitness();
    	}
    	
    	public boolean isFeasible() {
        	return this.aestheticFitness >= aestheticThreshold;
        }
    }

    public LevelDesigner(LevelRepresentationFactory levelRepresentationFactory) {
        this.levelRepresentationFactory = levelRepresentationFactory;
    }

    public void run() {
    	feasiblePopulation = new Population<>();
    	infeasiblePopulation = new Population<>();
    	
    	// Generate the random population and split them into the two groups.
    	List<LevelRepresentation> p = levelRepresentationFactory.getLevelRepresentationBatch(populationSize);
    	for (LevelRepresentation l : p) {
    		Individual individual = new Individual(l);
    		if (individual.isFeasible()) {
    			feasiblePopulation.add(individual);
    		} else {
    			infeasiblePopulation.add(individual);
    		}
    	}
    	
    }
    
}
