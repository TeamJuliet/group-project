package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class TestHelpers {
	public static final int ZERO_SCORE = 0;
	public static final Candy RED_CANDY = new Candy(CandyColour.RED, CandyType.NORMAL);
	public static final Candy BLUE_CANDY = new Candy(CandyColour.BLUE, CandyType.NORMAL);
	public static final Candy YELLOW_CANDY = new Candy(CandyColour.YELLOW, CandyType.NORMAL);
	public static final Candy GREEN_CANDY = new Candy(CandyColour.GREEN, CandyType.NORMAL);
	public static final Candy UNMOVEABLE_CANDY = new Candy(null, CandyType.UNMOVEABLE);
	public static final Candy HORIZONTAL_RED = new Candy(CandyColour.RED, CandyType.HORIZONTALLY_STRIPPED);
	public static final Candy VERTICAL_RED = new Candy(CandyColour.RED, CandyType.VERTICALLY_STRIPPED);
	public static final Candy VERTICAL_GREEN = new Candy(CandyColour.RED, CandyType.VERTICALLY_STRIPPED);
	public static final Candy WRAPPED_RED = new Candy(CandyColour.RED, CandyType.WRAPPED);
	public static final Candy COLOR_BOMB = new Candy(null, CandyType.BOMB);
	public static final int NO_JELLIES = 0;
	public static final int NO_INGREDIENTS = 0;
	public static final int TWO_MOVES_LEFT = 2;
	public static final int THREE_MOVES_LEFT = 3;
	public static final GameStateProgress NORMAL_INITIAL_PROGRESS = 
			new GameStateProgress(ZERO_SCORE, NO_JELLIES, NO_INGREDIENTS, THREE_MOVES_LEFT); 
}
