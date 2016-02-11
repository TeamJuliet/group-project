package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

public class RandomCandyGenerator extends CandyGenerator {
	public RandomCandyGenerator(GameState gameState) {
		super(gameState);
	}

	@Override
	public Candy generateCandy(int x) {
		Random random = new Random();

		// This ensures ingredients are dropped evenly over the course of the game, and also ensures all ingredients
		// are dropped before the number of moves runs out.
		if (super.ingredientsToDrop > 0) {
			if (random.nextInt(gameState.getMovesRemaining()) < ingredientsToDrop) {
				ingredientsToDrop--;
				return new Candy(null, CandyType.INGREDIENT);
			}
		}

		// If an ingredient wasn't dropped, then drop a normal candy

		int result = random.nextInt(super.gameState.getNumberOfCandyColours());
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
