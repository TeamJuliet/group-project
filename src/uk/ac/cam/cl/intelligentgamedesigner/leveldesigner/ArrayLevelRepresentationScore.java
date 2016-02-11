package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class ArrayLevelRepresentationScore extends ArrayLevelRepresentation {

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

    public ArrayLevelRepresentationScore(Random random) {
        super(random);

        // Score to reach is initialised in the range: 1000-500000
        this.parameters.add(new Parameter(random, 1000, 500000));
    }
    
    @Override
    public Design getDesign() {
    	Design design = super.getDesign();
    
    	design.setGameMode(GameMode.HIGHSCORE);
        design.setObjectiveTarget(parameters.get(2).getValue());
    	
        return design;
    }
}
