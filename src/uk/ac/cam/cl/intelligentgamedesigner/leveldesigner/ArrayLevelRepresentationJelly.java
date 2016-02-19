package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class ArrayLevelRepresentationJelly extends ArrayLevelRepresentation {
	public static final int maxJellyLevel = 2;

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public ArrayLevelRepresentationJelly(Random random) {
        super(random);
		board.initialiseJellyLevels();
    }
    
    @Override
    public ArrayLevelRepresentationJelly clone() {
    	ArrayLevelRepresentationJelly clone = (ArrayLevelRepresentationJelly) super.clone();

    	return clone;
    }

	@Override
	public void mutate() {
		super.mutate();
		
		if (random.nextDouble() > 0.5) { // Mutate one of the jelly levels with 50% probability.
			board.mutateJellyLevels();
		}
	}

	@Override
	public void crossoverWith(LevelRepresentation levelRepresentation) {
		super.crossoverWith(levelRepresentation);
		ArrayLevelRepresentationJelly l = (ArrayLevelRepresentationJelly) levelRepresentation;
	}

	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	design.setGameMode(GameMode.JELLY);
    	
    	return design;
    }
}
