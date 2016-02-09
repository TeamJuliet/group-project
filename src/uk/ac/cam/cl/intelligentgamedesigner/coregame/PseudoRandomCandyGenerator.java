package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class PseudoRandomCandyGenerator extends CandyGenerator {

	public PseudoRandomCandyGenerator(DesignParameters designParameters) {
		super(designParameters);
	}

	private int curNum = 38, prime = 361, anotherPrime = 991;

	public int nextPseudoRandom() {
		// System.out.println(curNum + " " + CandyColour.values().length);
		curNum = (curNum * prime) % anotherPrime;
		return curNum;
	}

	@Override
	public Candy generateCandy(int x) {
		/* TODO: Currently, this generates an ingredient with a 1/20 chance if the game mode is INGREDIENTS.
		Eventually, we need to change this so that the ingredients are distributed evenly over the course of the
		game, and that the generator never generates more ingredients than the maximum number specified in the game design
		*/
		if (super.designParameters.getGameMode() == GameMode.INGREDIENTS &&
				nextPseudoRandom() % 20 == 3) return new Candy(null, CandyType.INGREDIENT);
		int result = nextPseudoRandom() % super.designParameters.getNumberOfCandyColours();

		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
