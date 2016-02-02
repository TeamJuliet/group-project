package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public abstract class LevelRepresentation implements Cloneable {
	protected Random random;
	
	LevelRepresentation(Random r) {
		random = r;
	}
	
	public abstract void mutate();
	
	public abstract List<LevelRepresentation> crossoverWith(LevelRepresentation l);
	
	public abstract Design getDesign();
}
