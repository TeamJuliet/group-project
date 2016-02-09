package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

public class RandomCandyGenerator extends CandyGenerator {
	public RandomCandyGenerator(DesignParameters designParameters) {
		super(designParameters);
	}

	@Override
	public Candy generateCandy(int x) {
		Random random = new Random();
		/* TODO: Currently, this generates an ingredient with a 1/20 chance if the game mode is INGREDIENTS.
		Eventually, we need to change this so that the ingredients are distributed evenly over the course of the
		game, and that the generator never generates more ingredients than the maximum number specified in the game design
		*/
		if (super.designParameters.getGameMode() == GameMode.INGREDIENTS &&
				random.nextInt(20) == 3) return new Candy(null, CandyType.INGREDIENT);
		int result = random.nextInt(super.designParameters.getNumberOfCandyColours());
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
