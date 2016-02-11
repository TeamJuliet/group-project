package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class PseudoRandomCandyGenerator extends CandyGenerator {

	public PseudoRandomCandyGenerator(GameState gameState) {
		super(gameState);
	}

	private int curNum = 38, prime = 361, anotherPrime = 991;

	public int nextPseudoRandom() {
		// System.out.println(curNum + " " + CandyColour.values().length);
		curNum = (curNum * prime) % anotherPrime;
		return curNum;
	}

	@Override
	public Candy generateCandy(int x) {
		// This ensures ingredients are dropped evenly over the course of the game, and also ensures all ingredients
		// are dropped before the number of moves runs out.
		if (super.ingredientsToDrop > 0) {
			if (nextPseudoRandom() % gameState.getMovesRemaining() < ingredientsToDrop) {
				ingredientsToDrop--;
				return new Candy(null, CandyType.INGREDIENT);
			}
		}

		// If an ingredient wasn't dropped, then drop a normal candy
		int result = nextPseudoRandom() % super.gameState.getNumberOfCandyColours();
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}
}