package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class LevelRepresentationIngredients extends LevelRepresentation {

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours
        [2] - number of ingredients

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public LevelRepresentationIngredients(Random random)
    {
        super(random);

        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(random.nextInt(5) + 1);
    }

    @Override
    public Design getDesign()
    {
    	
    }
    
    @Override
    public void mutate() {

    }
}
