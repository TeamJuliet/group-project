package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class ArrayLevelRepresentationIngredients extends ArrayLevelRepresentation {

    public ArrayLevelRepresentationIngredients(Random random, int numberOfCandyColours) {
        super(random, numberOfCandyColours);
    }

	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	design.setGameMode(GameMode.INGREDIENTS);

        return design;
    }
}
