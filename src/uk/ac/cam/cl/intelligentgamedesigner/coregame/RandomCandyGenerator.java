package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

/**
 * 
 * Generator that simply creates a random candy using the default java random generator.
 *
 */
public class RandomCandyGenerator extends CandyGenerator {
	
	/**
	 * Constructor using the level design and the progress of the game state.
	 * @param design The level design used to create the game state.
	 * @param gameStateProgress The progress of the game.
	 */
	public RandomCandyGenerator() {
		super();
	}

	@Override
	public Candy generateCandy(int x) {
		Random random = new Random();

		// This ensures a new ingredient is introduced whenever a user clears one on the board. It also introduces
		// another ingredient with a small probability
		if (super.ingredientsToDrop > 0) {
			if (previousNumberOfIngredientsRemaining > gameStateProgress.getIngredientsRemaining()
					|| random.nextInt(100) < 2) {
				ingredientsToDrop--;
				previousNumberOfIngredientsRemaining--;
				return new Candy(null, CandyType.INGREDIENT);
			}	
		}

		// If an ingredient wasn't dropped, then drop a normal candy
		int result = random.nextInt(super.design.getNumberOfCandyColours());
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
