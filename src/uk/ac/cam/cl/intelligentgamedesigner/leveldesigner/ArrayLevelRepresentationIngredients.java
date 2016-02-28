package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class ArrayLevelRepresentationIngredients extends ArrayLevelRepresentation {

    public ArrayLevelRepresentationIngredients(LevelRepresentationParameters parameters) {
        super(parameters);
    }

    /**
     * This returns a design corresponding to the representation.
     *
     * @return The design
     */
	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	design.setGameMode(GameMode.INGREDIENTS);

        return design;
    }
}
