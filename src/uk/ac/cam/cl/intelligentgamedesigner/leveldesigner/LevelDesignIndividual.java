package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

/**
 * This class is used for encapsulating a level representation an its associated fitnesses.
 */
public class LevelDesignIndividual implements Comparable<LevelDesignIndividual> {

    private final LevelRepresentation levelRepresentation;
    private double aestheticFitness;
    private double constraintFitness;
    private Design design = null;

    public LevelDesignIndividual (LevelRepresentation levelRepresentation) {
        this.levelRepresentation = levelRepresentation;
        this.aestheticFitness = levelRepresentation.getAestheticFitness();
        this.constraintFitness = levelRepresentation.getConstraintFitness();
    }

    /**
     * A get method for the fitness of the individual.
     *
     * @return  The fitness of the individual
     */
    public double getFitness() {
        return aestheticFitness * constraintFitness;
    }

    /**
     * A get method for the level representation.
     *
     * @return  The level representation
     */
    public LevelRepresentation getLevelRepresentation() {
        return levelRepresentation;
    }

    /**
     * A method for getting the corresponding Design of an individual.
     *
     * @return  The corresponding Design instance
     */
    public Design getDesign() {
        if (design == null) {
            design = levelRepresentation.getDesign();
        }
        return design;
    }

    /**
     * For comparing individuals based on fitness.
     *
     * @param individual    The individual to compare against
     * @return  The result of the comparison
     */
    @Override
	public int compareTo(LevelDesignIndividual individual) {
		double compared = this.getFitness() - individual.getFitness();
		
		// Can't just return compared cast to an int, need to be careful with rounding.
		return (compared > 0 ? 1 : (compared < 0 ? -1 : 0));
	}
}
