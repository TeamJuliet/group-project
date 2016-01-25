package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

public class RandomCandyGenerator extends CandyGenerator {
	public RandomCandyGenerator(DesignParameters designParameters) {
		super(designParameters);
	}

	public Candy getCandy() {
		Random random = new Random();
		int result = random.nextInt(CandyColour.values().length);
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
