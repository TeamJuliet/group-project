package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class Scoring {
	// Scoring values (in accordance with the spec)
	static final int SCORE_MATCHED_3                = 60;	// When 3 candies are matched
	static final int SCORE_MATCHED_4                = 120;	// When 4 candies are matched
	static final int SCORE_MATCHED_5                = 200;	// When 5 candies are matched (include T, L and + shapes)
	static final int SCORE_CANDY_ELIMINATED         = 60;	// When a candy is eliminated by some special candy
	static final int SCORE_MATCHED_A_JELLY          = 1000;	// When a jelly is part of a match
	static final int SCORE_ACTIVATED_WRAPPED_CANDY  = 540;	// When a wrapped candy is activated
	static final int SCORE_ACTIVATED_BOMB           = 3000;	// When a bomb is activated
	static final int SCORE_BROUGHT_INGREDIENT_DOWN  = 10000;// When an ingredient reaches the bottom
	
}
