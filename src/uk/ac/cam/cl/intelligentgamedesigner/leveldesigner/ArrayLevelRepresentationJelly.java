package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

/**
 * A class for representing jelly levels.
 */
public class ArrayLevelRepresentationJelly extends ArrayLevelRepresentation {
	public static final int maxJellyLevel = 2;

    /*
        Parameters list:
        ----------------
        [0] - number of moves available

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public ArrayLevelRepresentationJelly(LevelRepresentationParameters parameters) {
        super(parameters);
		board.initialiseJellyLevels();
    }

	/**
	 * This method clones the current representation.
	 *
	 * @return The clone
	 */
    @Override
    public ArrayLevelRepresentationJelly clone() {
    	ArrayLevelRepresentationJelly clone = (ArrayLevelRepresentationJelly) super.clone();

    	return clone;
    }

	/**
	 * This extends the mutation to alter jelly levels.
	 */
	@Override
	public void mutate() {
		super.mutate();
		
		if (parameters.random.nextDouble() > 0.5) { // Mutate one of the jelly levels with 50% probability.
			board.mutateJellyLevels();
		}
	}

	/**
	 * This returns a design corresponding to the representation.
	 *
	 * @return The design
	 */
	@Override
    public Design getDesign() {
    	Design design = super.getDesign();
    	
    	design.setGameMode(GameMode.JELLY);
    	
    	return design;
    }

	/**
	 * This extends the aesthetic fitness function for jelly levels.
	 *
	 * @return	The fitness
     */
	@Override
    public double getAestheticFitness() {
    	double fitness = super.getAestheticFitness();
    	
    	double jellyFitness = AestheticChecker.calculateJellyFitness(board);
    	
    	return fitness + jellyFitness * 0.2;
    }
}
