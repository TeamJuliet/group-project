package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * This CandyGenerator ensures that any candy generated will be a candy of unmovable candy type.
 * It should be used when we want to explore the outcome of a game without filling the game with
 * normal candies that can be matched.
 * 
 * Note: this candy type is not matchable nor movable.
 *
 */
public class UnmovableCandyGenerator extends CandyGenerator {
	
	@Override
	public Candy generateCandy(int x) {
	    // Always, simply return an unmovable candy.
		return new Candy(null, CandyType.UNMOVABLE);
	}
}
