package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public abstract class LevelRepresentation implements Cloneable {
	protected Random random;
	
	public LevelRepresentation(Random r) {
		random = r;
	}
	
	public LevelRepresentation clone() {
		LevelRepresentation clone = null;
		try {
			clone = (LevelRepresentation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	/**
	 * Slightly alters the level representation.
	 */
	public abstract void mutate();
	
	/**
	 * Performs crossover in place using the given LevelRepresentation and this.
	 * Note that levelRepresenation should be an instance of the same class as this.
	 * @param levelRepresentation the level representation to perform crossover with.
	 */
	public abstract void crossoverWith(LevelRepresentation levelRepresentation);
	
	public abstract Design getDesign();
	
	public abstract double getAestheticFitness();

	public abstract double getConstraintFitness();

	public abstract void printRepresentation();
}
