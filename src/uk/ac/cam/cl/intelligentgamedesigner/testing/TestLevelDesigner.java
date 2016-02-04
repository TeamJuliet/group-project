package uk.ac.cam.cl.intelligentgamedesigner.testing;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.ArrayLevelRepresentationScore;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelRepresentation;

public class TestLevelDesigner {

	public static void main(String[] args) {
		
		Random random = new Random();
		
		LevelRepresentation levelRep = new ArrayLevelRepresentationScore(random);
		
		
		
		for(int i = 0; i < 2; i++)
		{
			Design design = levelRep.getDesign();
			GameState state = new GameState(design);
			state.debugBoard();
			
			levelRep.mutate();
		}

	}

}
