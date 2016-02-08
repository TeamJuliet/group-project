package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class ArrayLevelRepresentationJelly extends ArrayLevelRepresentation {
	private static final int maxJellyLevel = 2;
	
    protected RandomBoard<Integer> jellyLevels;

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

        // Initialise the jelly levels.
        Integer[] values = new Integer[maxJellyLevel + 1];
        for (int i = 0; i < values.length; i++) {
        	values[i] = i;
        }
        jellyLevels = new RandomBoard<>(board.width, board.height, random, values);
        for (int x = 0; x < jellyLevels.width; x++) {
            for (int y = 0; y < jellyLevels.height; y++) {
                // Jelly levels initialised in the range: 0-2
                jellyLevels.set(x, y, random.nextInt(maxJellyLevel + 1));
            }
        }
    }
    
    @Override
    public ArrayLevelRepresentationJelly clone() {
    	ArrayLevelRepresentationJelly clone = (ArrayLevelRepresentationJelly) super.clone();
    	
    	// Copy the jelly levels array.
    	clone.jellyLevels = new RandomBoard<>(jellyLevels);
    	
    	return clone;
    }

	@Override
	public void mutate() {
		super.mutate();
		
		if (random.nextDouble() > 0.5) { // Mutate one of the jelly levels with 50% probability.
			jellyLevels.mutate();
		}
	}

	@Override
	public void crossoverWith(LevelRepresentation levelRepresentation) {
		super.crossoverWith(levelRepresentation);
		ArrayLevelRepresentationJelly l = (ArrayLevelRepresentationJelly) levelRepresentation;
		jellyLevels.crossoverWith(l.jellyLevels);
	}

	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	// TODO: set other parameters
    	
    	return design;
    }
}
