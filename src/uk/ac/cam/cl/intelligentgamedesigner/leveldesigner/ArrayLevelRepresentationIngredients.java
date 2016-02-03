package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class ArrayLevelRepresentationIngredients extends ArrayLevelRepresentation {

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

    public ArrayLevelRepresentationIngredients(Random random) {
        super(random);

        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(new Parameter(random, 1, 5));
    }

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation parent) {
		ArrayLevelRepresentationIngredients child1 = new ArrayLevelRepresentationIngredients(new Random());
		ArrayLevelRepresentationIngredients child2 = new ArrayLevelRepresentationIngredients(new Random());

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
