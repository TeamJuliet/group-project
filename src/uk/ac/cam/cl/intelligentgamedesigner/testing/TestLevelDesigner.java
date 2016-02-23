package uk.ac.cam.cl.intelligentgamedesigner.testing;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.ArrayLevelRepresentationScore;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesigner;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelRepresentation;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;

public class TestLevelDesigner {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		LevelRepresentation levelRep = new ArrayLevelRepresentationScore(random, 6);

		LevelDesignerManager ldm = new LevelDesignerManager(new Specification(1, GameMode.HIGHSCORE));
		
		LevelDesigner ld = new LevelDesigner(ldm, random, 1);
		
		ld.run();
		
		
		ld.printIndividuals();
		
	
		
		for(int i = 0; i < 10; i++)
		{
			Design design = levelRep.getDesign();
			GameState state = new GameState(design);
			// THIS METHOD HAS BEEN DEPRECIATED
			// state.debugBoard();
			
			levelRep.mutate();
		}

	}

}
