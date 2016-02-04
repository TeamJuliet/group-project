package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class ArrayLevelRepresentationJelly extends ArrayLevelRepresentation {
	private static final int maxJellyLevel = 2;
	
    protected int[][] jellyLevels;

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

        // Initialise the jelly levels
        this.jellyLevels = new int[ArrayLevelRepresentation.maxWidth][ArrayLevelRepresentation.maxHeight];
        for (int x = 0; x < ArrayLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < ArrayLevelRepresentation.maxHeight; y++) {
                // Jelly levels initialised in the range: 0-2
                jellyLevels[x][y] = random.nextInt(maxJellyLevel + 1);
            }
        }
    }

	@Override
	public void mutate() {
		super.mutate();
		
		if (random.nextDouble() > 0.5) { // Mutate one of the jelly levels with 50% probability.
			int x = random.nextInt(maxWidth);
	        int y = random.nextInt(maxHeight);
	        
	        jellyLevels[x][y] = getNewRandomInt(jellyLevels[x][y], 0, maxJellyLevel);
		}
	}

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation parent) {
        ArrayLevelRepresentationJelly child1 = new ArrayLevelRepresentationJelly(new Random());
        ArrayLevelRepresentationJelly child2 = new ArrayLevelRepresentationJelly(new Random());

        super.crossoverWith((ArrayLevelRepresentation) parent, child1, child2);

        ArrayList<LevelRepresentation> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
	}

	@Override
    public Design getDesign()
    {
    	Design design = getBaseDesign();
    	
    	// TODO: set other parameters

        return null;
    }
}
