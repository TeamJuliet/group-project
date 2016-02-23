package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that contains many constants used in the game. This class should not
 * contain any constants concerning scoring.
 *
 */
public class GameConstants {
	// The radius a wrapped candy should have when it is single detonating.
	public static final int SINGLE_WRAPPED_RADIUS = 1;
	// The radius of the two wrapped candies when they are detonated together.
	// (One should have a radius of 0, because it should still appear on the
	// board).
	public static final int DOUBLE_WRAPPED_RADIUS_A = 0;
	public static final int DOUBLE_WRAPPED_RADIUS_B = 3;

	// The moves remaining for the game are 100.
	public static final int MOVES_REMAINING_100 = 100;
	// No jellies will be used in the game.
	public static final int NO_JELLIES = 0;
	// No ingredients will be used in the game.
	public static final int NO_INGREDIENTS = 0;
	// No jelly level for this cell.
	public static final int NO_JELLY_LEVEL = 0;
}
