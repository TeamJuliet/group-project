package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class ArrayLevelRepresentationIngredients extends ArrayLevelRepresentation {

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of ingredients

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public ArrayLevelRepresentationIngredients(Random random, int numberOfCandyColours) {
        super(random, numberOfCandyColours);

        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(new Parameter(random, 1, 5));
    }

	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	design.setGameMode(GameMode.INGREDIENTS);
        design.setObjectiveTarget(parameters.get(1).getValue());

        return design;
    }
}
