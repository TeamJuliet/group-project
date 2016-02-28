package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Class that specifies design for the level including how the initial board
 * layout looks like, the number of moves available and the objective to
 * complete the level.
 *
 */
public class Design implements Serializable {
	public static int MAX_DIMENSIONS = 9;
	// The dimensions of the board.
	private int height, width;

	// The layout - specifying the position of special or unused cells.
	private Cell[][] boardLayout;

	// The number of moves that the player has to complete the level.
	private int numberOfMovesAvailable;

	// The game mode, specifying the objective of the player.
	private GameMode gameMode;

	// The target needed to clear whatever game mode
	// (if high score, then = the required score)
	// (if jelly, then ignore this)
	// (if ingredients, then = the number of ingredients that need to be
	// cleared)
	private int objectiveTarget;

	// The number of unique colours a candy can take
	// Note this should be 6 and below.
	private int numberOfCandyColours;

	// Candy generator that is going to be used.
	// Note: this is not checked for equality.
	private Class<? extends CandyGenerator> candyGenerator;

	/**
	 * Copy constructor.
	 * 
	 * @param design
	 *            The design to copy.
	 */
	public Design(Design design) {
		this.height = design.height;
		this.width = design.width;
		// Deep copy on the board.
		this.boardLayout = GameStateAuxiliaryFunctions
				.copyBoard(design.boardLayout);
		this.numberOfMovesAvailable = design.numberOfMovesAvailable;
		this.objectiveTarget = design.objectiveTarget;
		this.numberOfCandyColours = design.numberOfCandyColours;
		this.gameMode = design.gameMode;
		this.candyGenerator = design.candyGenerator;
	}

	/**
	 * Initialise the Design with a default - namely a 10x10 board of empty
	 * cells, without any jellies, having six different candy colours, 10 moves
	 * available and the objective to reach a score of 1.
	 */
	public Design() {
		this.height = MAX_DIMENSIONS;
		this.width = MAX_DIMENSIONS;
		this.boardLayout = new Cell[width][height];
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				boardLayout[x][y] = new Cell(CellType.EMPTY,
						GameConstants.NO_JELLY_LEVEL);
			}
		}
		this.numberOfMovesAvailable = 10;
		this.objectiveTarget = 1;
		this.numberOfCandyColours = 6;
		this.gameMode = GameMode.HIGHSCORE;
		this.candyGenerator = RandomCandyGenerator.class;
	}

	/**
	 * Resize the board to the dimensions specified. As many cells from the
	 * previous board as possible, will be kept.
	 * 
	 * @param width
	 *            the width of the new board.
	 * @param height
	 *            the height of the new board.
	 */
	public void resizeBoard(int width, int height) {
		Cell[][] newBoard = new Cell[width][height];
		// crop/enlarge to the correct size
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x < this.width && y < this.height) {
					newBoard[x][y] = boardLayout[x][y];
				} else {
					newBoard[x][y] = new Cell(CellType.EMPTY, 0);
				}
			}
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Function to set the board layout for this level design. It is assumed
	 * that all cells in the board are non-null.
	 * 
	 * @param board
	 *            The board layout for the design.
	 */
	public void setBoard(Cell[][] board) {
		boardLayout = board;
		this.width = board.length;
		this.height = board[0].length;

		// Currently, the default ingredient sinks are set to the bottom-most
		// cells in each column
		for (int x = 0; x < width; x++) {
			int y = height - 1;
			while (y >= 0 && board[x][y].getCellType() == CellType.UNUSABLE) {
				y--;
			}
			if (y >= 0)
				board[x][y].setIngredientSink();
		}
	}

	/**
	 * Set the rules for this level.
	 * 
	 * @param gameMode
	 *            The objective of the game.
	 * @param numberOfMovesAvailable
	 *            The number of moves available to complete the level.
	 * @param objectiveTarget
	 *            The target depending on the game mode (see comments above).
	 * @param numberOfCandyColours
	 *            The number of distinct candy colours for the candies.
	 */
	public void setRules(GameMode gameMode, int numberOfMovesAvailable,
			int objectiveTarget, int numberOfCandyColours) {
		this.gameMode = gameMode;
		this.numberOfMovesAvailable = numberOfMovesAvailable;
		this.objectiveTarget = objectiveTarget;
		this.numberOfCandyColours = numberOfCandyColours;
	}

	/**
	 * Sets the game mode for the level. Note that you have to set the
	 * appropriate objective target if the game mode is HIGHSCORE or
	 * INGREDIENTS.
	 * 
	 * @param gameMode
	 *            the game mode to set in this level design.
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	/**
	 * Set the number of moves available to complete this level.
	 * 
	 * @param numberOfMovesAvailable
	 *            The number of moves available to complete this level.
	 */
	public void setNumberOfMovesAvailable(int numberOfMovesAvailable) {
		this.numberOfMovesAvailable = numberOfMovesAvailable;
	}

	/**
	 * Set the objective target for this level. Note: this should be set only if
	 * the the game mode is INGREDIENTS or HIGHSCORE.
	 * 
	 * @param objectiveTarget
	 *            the target for HIGHSCORE or INGREDIENTS levels.
	 */
	public void setObjectiveTarget(int objectiveTarget) {
		this.objectiveTarget = objectiveTarget;
	}

	/**
	 * Set the number of distinct candy colours that the candies on the board
	 * are allowed to take.
	 * 
	 * @param numberOfCandyColours
	 *            the number of distinct candy colours that the candies on the
	 *            board will be allowed to take.
	 */
	public void setNumberOfCandyColours(int numberOfCandyColours) {
		this.numberOfCandyColours = numberOfCandyColours;
	}

	/**
	 * Set the candy generator to the one specified.
	 * 
	 * @param candyGenerator
	 *            The candy generator that should be used in this level.
	 */
	public void setCandyGenerator(Class<? extends CandyGenerator> candyGenerator) {
		this.candyGenerator = candyGenerator;
	}

	/**
	 * Get the width of the board layout.
	 * 
	 * @return the width of the board layout.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get the height of the boar layout.
	 * 
	 * @return the height of the board layout.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Get the game mode set for the game.
	 * 
	 * @return the game mode of the level.
	 */
	public GameMode getMode() {
		return gameMode;
	}

	/**
	 * Get the candy generator specified in this design.
	 * 
	 * @return The candy generator for this design.
	 */
	public CandyGenerator getCandyGenerator() {
		try {
			return this.candyGenerator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Error: Could not instantiate the candy generator.");
			return new RandomCandyGenerator();
		}
	}

	/**
	 * Function that returns the cell at the specified location (x, y) if the
	 * cell is in the board and null otherwise.
	 * 
	 * @param x
	 *            The x coordinate of the cell.
	 * @param y
	 *            The y coordinate of the cell.
	 * @return The candy located at that position (x, y) or null if the position
	 *         is invalid.
	 */
	public Cell getCell(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return boardLayout[x][y];
		}
		return null;
	}

	/**
	 * Function that gets the number of moves available in the game.
	 * 
	 * @return
	 */
	public int getNumberOfMovesAvailable() {
		return this.numberOfMovesAvailable;
	}

	/**
	 * Gets the objective target.
	 * 
	 * @return Returns the target score if the game mode is HIGHSCORE or the
	 *         number of ingredients to pass if the game mode is INGREDIENTS.
	 */
	public int getObjectiveTarget() {
		return this.objectiveTarget;
	}

	/**
	 * Function that gets the number of different colours specified for this
	 * level design.
	 * 
	 * @return the number of different candy colours.
	 */
	public int getNumberOfCandyColours() {
		return numberOfCandyColours;
	}

	/**
	 * Function that gets the board layout.
	 * 
	 * @return the board layout of the design.
	 */
	public Cell[][] getBoard() {
		return boardLayout;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Design))
			return false;

		Design toCompare = (Design) object;

		boolean equals = true;

		equals &= (this.height == toCompare.height
				&& this.width == toCompare.width
				&& this.numberOfMovesAvailable == toCompare.numberOfMovesAvailable
				&& this.objectiveTarget == toCompare.objectiveTarget
				&& this.numberOfCandyColours == toCompare.numberOfCandyColours && this.gameMode == toCompare.gameMode);

		// If parameters match, then check the board layouts.
		if (equals) {
			for (int x = 0; x < this.width; x++) {
				for (int y = 0; y < this.height; y++) {
					equals &= boardLayout[x][y]
							.equals(toCompare.boardLayout[x][y]);
				}
			}
		}

		return equals;
	}

	/**
	 * Function for debugging Design instances.
	 *
	 * @return The string representation of the design.
     */
	@Override
	public String toString () {
		String result = "Number of moves:         " + numberOfMovesAvailable + "\n";
		result += "Number of candy colours: " + numberOfCandyColours + "\n";
		result += "Objective target:        " + objectiveTarget + "\n";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				CellType cellType = boardLayout[x][y].getCellType();

				switch (cellType) {
					case EMPTY:
						result += " ";
						break;
					case ICING:
						result += "I";
						break;
					case LIQUORICE:
						result += "L";
						break;
					case UNUSABLE:
						result += "X";
						break;
					default:
						System.err.println("Error: Design contains a non-design cell!");
						break;
				}
			}
			result += "\n";
		}
		return result;
	}
}