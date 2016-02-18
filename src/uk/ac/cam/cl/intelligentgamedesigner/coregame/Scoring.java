package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class Scoring {
	// Scoring values (in accordance with the spec)
	static final int MATCHED_3                = 60;	// When 3 candies are matched
	static final int MATCHED_4                = 120;	// When 4 candies are matched
	static final int MATCHED_5                = 200;	// When 5 candies are matched (include T, L and + shapes)
	static final int CANDY_ELIMINATED         = 60;	// When a candy is eliminated by some special candy
	static final int MATCHED_A_JELLY          = 1000;	// When a jelly is part of a match
	static final int DETONATE_WRAPPED_CANDY   = 540;	// When a wrapped candy is activated
	static final int DETONATE_BOMB            = 3000;	// When a bomb is activated
	static final int DETONATE_STRIPPED_CANDY = 0;
	static final int BROUGHT_INGREDIENT_DOWN  = 10000;// When an ingredient reaches the bottom
	static final int MADE_BOMB = 200;
	static final int MADE_WRAPPED_CANDY = 200;
	static final int MADE_STRIPPED_CANDY = 120;
	static final int NO_ADDITIONAL_SCORE = 0;
	static final int STRIPPED_INDIVIDUAL = 60;
	static final int WRAPPED_INDIVIDUAL = 60;
	static final int BOMB_INDIVIDUAL = 60;
	static final int WRAPPED_STRIPPED_INDIVIDUAL = 60;
	static final int ICING_CLEARED = 20; 
	// No score for liquorice.
}
