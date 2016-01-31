package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

public class RandomCandyGenerator extends CandyGenerator {
	public RandomCandyGenerator(DesignParameters designParameters) {
		super(designParameters);
	}

	@Override
	public Candy generateCandy(int x) {
		Random random = new Random();
		int result = random.nextInt(CandyColour.values().length);
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
