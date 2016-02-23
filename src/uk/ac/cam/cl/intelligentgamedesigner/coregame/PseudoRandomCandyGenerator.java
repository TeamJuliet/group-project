package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

/**
 * 
 * Candy Generator that creates a deterministic sequence of candies.
 *
 */
public class PseudoRandomCandyGenerator extends CandyGenerator {

	public PseudoRandomCandyGenerator() {
		super();
	}

	private int curNum = 38, prime = 361, anotherPrime = 991;

	private int nextPseudoRandom() {
		// System.out.println(curNum + " " + CandyColour.values().length);
		curNum = (curNum * prime) % anotherPrime;
		return curNum;
	}

	// TODO: Remove this.
	private static Random random = new Random();
	
	@Override
	public Candy generateCandy(int x) {
		// This ensures a new ingredient is introduced whenever a user clears one on the board. It also introduces
		// another ingredient with a small probability
		if (super.ingredientsToDrop > 0) {
			if (previousNumberOfIngredientsRemaining > gameStateProgress.getIngredientsRemaining()
					|| nextPseudoRandom() % 100 < 2) {
				ingredientsToDrop--;
				previousNumberOfIngredientsRemaining--;
				return new Candy(null, CandyType.INGREDIENT);
			}
		}
		
		// int num = nextPseudoRandom();
		int num = random.nextInt();
		if (num < 0) num = -num;
		// This line just adds some bombs for testing.
		// if (num % 23 == 2) return new Candy(null, CandyType.BOMB);
		// If an ingredient wasn't dropped, then drop a normal candy
		int result = num % super.design.getNumberOfCandyColours();
		CandyType type = CandyType.NORMAL;
		return new Candy(CandyColour.values()[result], type);
	}
}