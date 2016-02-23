package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

public class LevelDesignIndividual implements Comparable<LevelDesignIndividual> {

    private final LevelRepresentation levelRepresentation;
    private double aestheticFitness;
    private double constraintFitness;
    private double difficultyFitness;
    private Design design = null;

    public LevelDesignIndividual (LevelRepresentation levelRepresentation) {
        this.levelRepresentation = levelRepresentation;
        this.aestheticFitness = levelRepresentation.getAestheticFitness();
        this.constraintFitness = levelRepresentation.getConstraintFitness();
    }

    public double getFeasibility() {
        // TODO: This should take constraint fitness into account
        return this.aestheticFitness;
    }

    public LevelRepresentation getLevelRepresentation () {
        return levelRepresentation;
    }

    public double getFitness() {
        double fitness = aestheticFitness;
        // TODO this needs to be tweaked, need to decide how fitnesses will be combined.
        fitness += difficultyFitness * 2;
        return fitness / 3;
    }

    public Design getDesign() {
        if (design == null) {
            design = levelRepresentation.getDesign();
        }
        return design;
    }

    public void setDifficultyFitness(double difficultyFitness) {
        this.difficultyFitness = difficultyFitness;
    }
    
    @Override
	public int compareTo(LevelDesignIndividual individual) {
		double compared = this.getFitness() - individual.getFitness();
		// Can't just return compared cast to an int, need to be careful with rounding.
		return (compared > 0 ? 1 : compared < 0 ? -1 : 0);
	}
}
