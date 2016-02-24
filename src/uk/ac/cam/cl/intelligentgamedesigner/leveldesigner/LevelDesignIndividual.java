package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

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

    public double getFitness() {
        return (this.aestheticFitness + this.constraintFitness) / 2;
    }

    public LevelRepresentation getLevelRepresentation() {
        return levelRepresentation;
    }

    public Design getDesign() {
        if (design == null) {
            design = levelRepresentation.getDesign();
        }
        return design;
    }
    
    @Override
	public int compareTo(LevelDesignIndividual individual) {
		double compared = this.getFitness() - individual.getFitness();
		// Can't just return compared cast to an int, need to be careful with rounding.
		return (compared > 0 ? 1 : compared < 0 ? -1 : 0);
	}
}
