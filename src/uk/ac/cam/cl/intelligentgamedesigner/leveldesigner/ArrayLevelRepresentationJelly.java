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
        jellyLevels = new RandomBoard<>(maxWidth, maxHeight, random, values);
        for (int x = 0; x < ArrayLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < ArrayLevelRepresentation.maxHeight; y++) {
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
			int x = random.nextInt(maxWidth);
	        int y = random.nextInt(maxHeight);
	        
	        jellyLevels.set(x, y, getNewRandomInt(jellyLevels.get(x, y), 0, maxJellyLevel));
		}
	}

	@Override
	public ArrayLevelRepresentationJelly[] crossoverWith(LevelRepresentation parent) {
		ArrayLevelRepresentation[] children = super.crossoverWith(parent);
		ArrayLevelRepresentationJelly a = (ArrayLevelRepresentationJelly) children[0];
		ArrayLevelRepresentationJelly b = (ArrayLevelRepresentationJelly) children[1];
        
        // Todo crossover jelly board too.
		
		ArrayLevelRepresentationJelly[] jellyChildren = {a, b};
        return jellyChildren;
	}

	@Override
    public Design getDesign() {
    	Design design = getDesign();
    	
    	// TODO: set other parameters

        return design;
    }
}
