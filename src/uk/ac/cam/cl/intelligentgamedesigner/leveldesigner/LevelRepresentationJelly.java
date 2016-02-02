package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class LevelRepresentationJelly extends LevelRepresentation {

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

    public LevelRepresentationJelly(Random random)
    {
        super(random);

        // Initialise the jelly levels
        this.jellyLevels = new int[LevelRepresentation.maxWidth][LevelRepresentation.maxHeight];
        for (int x = 0; x < LevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < LevelRepresentation.maxHeight; y++) {
                // Jelly levels initialised in the range: 0-2
                jellyLevels[x][y] = random.nextInt(3);
            }
        }
    }
    
    @Override
    public Design getDesign()
    {
    	
    }

    @Override
    public void mutate() {

    }
}
