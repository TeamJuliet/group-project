package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

/**
 * This abstract class specifies the methods that any type of level representation should implement.
 */
public abstract class LevelRepresentation implements Cloneable {
	protected LevelRepresentationParameters parameters;
	
	public LevelRepresentation(LevelRepresentationParameters parameters) {
		this.parameters = parameters;
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

	/**
	 * This is for converting the representation of a design into an actual design instance.
	 *
	 * @return	The associated Design instance
     */
	public abstract Design getDesign();

	/**
	 * This is for calculating the aesthetic fitness of a level representation.
	 *
	 * @return	The aesthetic fitness
     */
	public abstract double getAestheticFitness();

	/**
	 * This is for calculating the constraint fitness of a level representation.
	 *
	 * @return	The constraint fitness
     */
	public abstract double getConstraintFitness();

	/**
	 * This is for generating a string representation of the level representation.
	 *
	 * @return	The string representation
     */
	@Override
	public abstract String toString ();
}
