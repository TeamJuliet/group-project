package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class LevelRepresentationIngredients extends GeneralLevelRepresentation {

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

    public LevelRepresentationIngredients(Random random) {
        super(random);

        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(new Parameter(random, 1, 5));
    }

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public Design getDesign()
    {
    	Design design = getBaseDesign();
    	
    	// TODO: set other parameters
    }
}
