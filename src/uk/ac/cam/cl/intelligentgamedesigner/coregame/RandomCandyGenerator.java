package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

/**
 * 
 * Generator that simply creates a random candy using the default java random generator.
 *
 */
public class RandomCandyGenerator extends CandyGenerator {
	
	@Override
	public Candy generateCandy(int x) {
		Random random = new Random();

		if (shouldGenerateIngredient(random.nextInt(100) < 2))
			return new Candy (null, CandyType.INGREDIENT);

		// If an ingredient wasn't dropped, then drop a normal candy
		int result = random.nextInt(super.design.getNumberOfCandyColours());
		return new Candy(CandyColour.values()[result], CandyType.NORMAL);
	}

}
