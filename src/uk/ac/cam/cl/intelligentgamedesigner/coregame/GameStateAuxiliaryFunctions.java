package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Class that contains functions that are related to the functionality of the
 * GameState.
 *
 */
public class GameStateAuxiliaryFunctions {

	// Declarations for the direction that should be followed when given in
	// boolean format.
	public static final boolean VERTICAL = true;
	public static final boolean HORIZONTAL = false;

	// Constant candy that refers to a colour bomb. Should only be used for
	// counting or should be called with new Candy(COLOR_BOMB).
	public static final Candy COLOUR_BOMB = new Candy(null, CandyType.BOMB);

	/**
	 * Function that is used to determine what special candy will be formed by a
	 * certain pattern.
	 * 
	 * @param analysis
	 *            the single tile analysis of a cell
	 * @return the candyType that should be formed by this match.
	 */
	public static CandyType getSpecialCandyFormed(SingleTileAnalysis analysis) {
		if (analysis.getLengthX() <= 3 && analysis.getLengthY() < 3
				|| analysis.getLengthX() < 3 && analysis.getLengthY() <= 3)
			return null;
		else if (analysis.getLengthX() < 3 && analysis.getLengthY() == 4)
			return CandyType.VERTICALLY_STRIPPED;
		else if (analysis.getLengthY() < 3 && analysis.getLengthX() == 4)
			return CandyType.HORIZONTALLY_STRIPPED;
		else if (analysis.getLengthX() == 5 || analysis.getLengthY() == 5)
			return CandyType.BOMB;
		else
			return CandyType.WRAPPED;
	}

	/**
	 * Function that is used to replace all striped and wrapped candies with
	 * normal candies of the same colour.
	 *
	 * @param board
	 *            The board to replace the special candies on
	 */
	public static void removeSpecialCandies(Cell[][] board) {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				// Ignore cells without a candy
				if (!board[x][y].hasCandy())
					continue;

				Candy candy = board[x][y].getCandy();
				CandyType candyType = candy.getCandyType();
				if (candyType == CandyType.VERTICALLY_STRIPPED
						|| candyType == CandyType.HORIZONTALLY_STRIPPED
						|| candyType == CandyType.WRAPPED) {
					board[x][y].setCandy(new Candy(candy.getColour(),
							CandyType.NORMAL));
				}
			}
		}
	}

	/**
	 * Function that safely checks if the cell has the same colour as the candy
	 * colour given.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @param colour
	 *            The colour to be checked for equality.
	 * @return Whether the cell has a candy and this has the colour specified.
	 */
	public static boolean sameColourWithCell(Cell cell, CandyColour colour) {
		if (cell.getCandy() == null || cell.getCandy().getColour() == null)
			return false;
		return cell.getCandy().getColour().equals(colour);
	}

	/**
	 * Function to get the single tile analysis for the position on a board.
	 * 
	 * @param pos
	 *            The position of the cell to be checked for matches. (Note: the
	 *            position should actually be in the board).
	 * @param board
	 *            The board on which this should be checked.
	 * @return The Single Tile Analysis for the cell specified.
	 */
	public static SingleTileAnalysis analyzeTile(Position pos, Cell[][] board) {
		int x = pos.x, y = pos.y;
		CandyColour cellColour = board[x][y].getCandy().getColour();
		int startX = x - 1, endX = x + 1, startY = y - 1, endY = y + 1;
		while (startX >= 0 && sameColourWithCell(board[startX][y], cellColour))
			--startX;
		while (endX < board.length
				&& sameColourWithCell(board[endX][y], cellColour))
			++endX;
		while (startY >= 0 && sameColourWithCell(board[x][startY], cellColour))
			--startY;
		while (endY < board[0].length
				&& sameColourWithCell(board[x][endY], cellColour))
			++endY;
		return new SingleTileAnalysis(startX + 1, endX - 1, startY + 1,
				endY - 1);
	}

	/**
	 * Check if any of the two positions is in the horizontal range.
	 * 
	 * @param move
	 *            The move for which the two positions will be checked.
	 * @param startX
	 *            The start of the horizontal interval (inclusive).
	 * @param endX
	 *            The end of the horizontal interval (inclusive).
	 * @return Whether on the two positions are in the horizontal range.
	 */
	public static boolean moveInHorizontalRange(Move move, int startX, int endX) {
		return inRange(move.p1.x, startX, endX)
				|| inRange(move.p2.x, startX, endX);
	}

	/**
	 * Check if any of the two positions is in the vertical range.
	 * 
	 * @param move
	 *            The move for which the two positions will be checked.
	 * @param startX
	 *            The start of the vertical interval (inclusive).
	 * @param endX
	 *            The end of the vertical interval (inclusive).
	 * @return Whether on the two positions are in the vertical range.
	 */
	public static boolean moveInVerticalRange(Move move, int startY, int endY) {
		return inRange(move.p1.y, startY, endY)
				|| inRange(move.p2.y, startY, endY);
	}

	/**
	 * Function to check if x is in range [a, b]
	 * 
	 * @param x
	 *            The number to be checked.
	 * @param a
	 *            The start of the interval (inclusive).
	 * @param b
	 *            The end of the interval (inclusive).
	 * @return whether x is in the interval [a, b].
	 */
	public static boolean inRange(int x, int a, int b) {
		return a <= x && x <= b;
	}

	/**
	 * Function that checks whether the cell has an ingredient.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains an ingredient.
	 */
	public static boolean hasIngredient(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType().equals(CandyType.INGREDIENT);
	}

	/**
	 * Function that checks whether the position contains a special candy.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains a special candy.
	 */
	public static boolean hasSpecial(Cell cell) {
		return cell.hasCandy() && cell.getCandy().getCandyType().isSpecial();
	}

	/**
	 * Function that checks whether the position contains a colour bomb.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains a colour bomb.
	 */
	public static boolean hasColourBomb(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType().equals(CandyType.BOMB);
	}

	/**
	 * Function that checks whether the position contains a vertically stripped.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains a vertically stripped candy.
	 */
	public static boolean hasVerticallyStripped(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType()
						.equals(CandyType.VERTICALLY_STRIPPED);
	}

	/**
	 * Function that checks whether the position contains a special candy or a
	 * colour bomb.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains a special candy or a bomb.
	 */
	public static boolean hasSpecialOrColourBomb(Cell cell) {
		return hasSpecial(cell) || hasColourBomb(cell);
	}

	/**
	 * Function that checks whether the position contains a horizontally
	 * stripped.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return Whether that cell contains a horizontally stripped candy.
	 */
	public static boolean hasHorizontallyStripped(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType()
						.equals(CandyType.HORIZONTALLY_STRIPPED);
	}

	/**
	 * Function that checks whether that cell contains a wrapped candy.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return whether the cell contains a wrapped candy.
	 */
	public static boolean hasWrapped(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType().equals(CandyType.WRAPPED);
	}

	/**
	 * Function that checks whether a cell contains a horizontally or a
	 * vertically stripped candy.
	 * 
	 * @param cell
	 *            The cell to be checked.
	 * @return whether the cell has a stripped candy.
	 */
	public static boolean hasStripped(Cell cell) {
		return hasVerticallyStripped(cell) || hasHorizontallyStripped(cell);
	}

	/**
	 * Function that checks if a cell has a normal candy.
	 * 
	 * @param cell
	 *            The cell to be checked (assumed not null).
	 * @return Whether the given cell has a normal candy.
	 */
	public static boolean hasNormal(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType().equals(CandyType.NORMAL);
	}

	/**
	 * Function that returns whether the cell at position forms a match.
	 * 
	 * @param board
	 *            The board in which the cell is located.
	 * @param position
	 *            The position of the cell on the board.
	 * @return Whether the cell forms a match.
	 */
	public static boolean cellFormsMatch(Cell[][] board, Position position) {
		SingleTileAnalysis analysis = analyzeTile(position, board);
		return analysis.getLengthX() > 2 || analysis.getLengthY() > 2;
	}

	/**
	 * Function that creates the match analysis for a single cell at the
	 * specified position on the board. If the cell does not form a match then
	 * it will return null.
	 * 
	 * @param board
	 *            The board on which the cell is located.
	 * @param pos
	 *            The position of the cell on the board.
	 * @return The MatchAnalysis for that cell on the board.
	 */
	public static MatchAnalysis getSingleMatchAnalysis(Cell[][] board,
			Position pos) {
		if (!cellFormsMatch(board, pos))
			return null;
		SingleTileAnalysis analysis = analyzeTile(pos, board);
		
		List<Position> positions = new LinkedList<Position>();
		List<CandyType> specials = new LinkedList<CandyType>();
		
		int jelliesRemoved = 0;
		int blockersRemoved = 0;
		
		if (analysis.getLengthX() > 2) {
			for (int x = analysis.startX; x <= analysis.endX; ++x) {
				if (x == pos.x)
					continue;
				Position currentPosition = new Position(x, pos.y);
				positions.add(currentPosition);
				if (hasSpecialOrColourBomb(board[x][pos.y]))
					specials.add(board[x][pos.y].getCandy().getCandyType());
				if (board[x][pos.y].getJellyLevel() > 0)
					++jelliesRemoved;
				if (board[x][pos.y].getCellType().blocksCandies())
                    ++blockersRemoved;
			}
		}
		
		if (analysis.getLengthY() > 2) {
			for (int y = analysis.startY; y <= analysis.endY; ++y) {
				if (y == pos.y)
					continue;
				Position currentPosition = new Position(pos.x, y);
				positions.add(currentPosition);
				if (hasSpecialOrColourBomb(board[pos.x][y]))
					specials.add(board[pos.x][y].getCandy().getCandyType());
				if (board[pos.x][y].getJellyLevel() > 0)
					++jelliesRemoved;
				if (board[pos.x][y].getCellType().blocksCandies())
                    ++blockersRemoved;
			}
		}
		
		return new MatchAnalysis(positions, specials,
				getSpecialCandyFormed(analysis), jelliesRemoved, blockersRemoved);
	}

	/**
	 * Function that checks if there is a detonated candy on the board.
	 * 
	 * @param board
	 *            The board to be checked.
	 * @return Whether there is a detonated candy on the board.
	 */
	public static boolean hasDetonated(Cell[][] board) {
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board[0].length; ++j) {
				if (board[i][j].hasCandy()
						&& board[i][j].getCandy().isDetonated())
					return true;
			}
		}
		return false;
	}

	/**
	 * Get the total jelly level that exists on the board..
	 * 
	 * @param state
	 *            The game state to be checked.
	 * @return The total level of jellies on the board.
	 */
	public static int getJellyNumber(GameState state) {
		Cell[][] board = state.getBoard();
		int totalJellies = 0;
		for (Cell[] row : board) {
			for (Cell cell : row) {
				totalJellies += cell.getJellyLevel();
			}
		}
		return totalJellies;
	}

	/**
	 * Get the number of icing cells on the board of the game state.
	 * 
	 * @param state
	 *            The game state to be checked.
	 * @return The number of icing cells on the board.
	 */
	public static int getIcingNumber(GameState state) {
		Cell[][] board = state.getBoard();
		int totalIcing = 0;
		for (Cell[] row : board) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.ICING)
					totalIcing++;
			}
		}
		return totalIcing;
	}

	/**
	 * Get the number of liquorice cells on the board of the game state.
	 * 
	 * @param state
	 *            The game state to be checked.
	 * @return The number of liquorice cells on the board.
	 */
	public static int getLiquoriceNumber(GameState state) {
		Cell[][] board = state.getBoard();
		int totalLiquorice = 0;
		for (Cell[] row : board) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.LIQUORICE)
					totalLiquorice++;
			}
		}
		return totalLiquorice;
	}

	/**
	 * Get the number of ingredient candies on the board of the game state.
	 * 
	 * @param board
	 *            The board to count the ingredients.
	 * @return The number of ingredient candies on the board.
	 */
	public static int getIngredientsNumber(Cell[][] board) {
		int totalIngredients = 0;
		for (Cell[] row : board) {
			for (Cell cell : row) {
				if (hasIngredient(cell))
					totalIngredients++;
			}
		}
		return totalIngredients;
	}

	/**
	 * Function that deeply copies a cell board.
	 * 
	 * @param board
	 *            The board to be copied.
	 * @return The copy of the board.
	 */
	public static Cell[][] copyBoard(Cell[][] board) {
		Cell[][] copy = new Cell[board.length][board[0].length];
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board[0].length; ++j) {
				copy[i][j] = new Cell(board[i][j]);
			}
		}
		return copy;
	}

	/**
	 * Function that returns the blockers positions on the board.
	 * 
	 * @param board
	 *            The board that will be scanned.
	 * @return The list of positions containing blockers.
	 */
	public static List<Position> getBlockerPositions(Cell[][] board) {
		List<Position> blockers = new LinkedList<Position>();
		for (int x = 0; x < board.length; ++x) {
			for (int y = 0; y < board[0].length; ++y) {
				if (board[x][y].getCellType().blocksCandies()) {
					blockers.add(new Position(x, y));
				}
			}
		}
		return blockers;
	}

	/**
	 * Function that returns a human readable string representation of the
	 * board.
	 * 
	 * @param board
	 * @return string representation of the board.
	 */
	public static String boardToString(Cell[][] board) {
		String result = "";

		// We need to transpose the board so string can be constructed easily.
		Cell[][] tmp = new Cell[board.length][board[0].length];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				tmp[y][x] = board[x][y];
			}
		}

		result += "  ";
		for (int i = 0; i < board.length; i++) {
			result += " " + i + "  ";
		}
		result += "\n";
		int rowNum = 0;
		for (Cell[] row : tmp) {
			result += rowNum + " ";
			rowNum++;
			for (Cell cell : row) {
				result += cell.toString() + " ";
			}
			result += "\n";
		}
		return result;
	}
}
