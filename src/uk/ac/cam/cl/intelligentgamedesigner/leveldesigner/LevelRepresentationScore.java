package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class LevelRepresentationScore extends LevelRepresentation {

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours
        [2] - score to reach

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    @Override
    public LevelRepresentationScore(Random random)
    {
        super(random);

        // Score to reach is initialised in the range: 1000-500000
        this.parameters.add(random.nextInt(499001) + 1000);
    }
    
    @Override
    public Design getDesign()
    {
    	
    }

    @Override
    public void mutate() {

    }
}
