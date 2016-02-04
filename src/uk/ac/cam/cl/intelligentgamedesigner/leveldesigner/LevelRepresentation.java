package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.List;
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
	
	public abstract List<LevelRepresentation> crossoverWith(LevelRepresentation l);
	
	public abstract Design getDesign();
	
	public abstract double getAestheticFitness();
}
