package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasColourBomb;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasSpecial;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasSpecialOrColourBomb;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.RandomCandyGenerator;

/**
 * 
 * Class that contains auxiliary functions for estimating certain difficulty
 * aspects of the board.
 *
 */
public class BoardDifficultyGenerator {

	/**
	 * Function that returns the number of combinable candies on the cellBoard.
	 * 
	 * @param cellBoard
	 *            The board on which the check will be performed.
	 * @return The number of combinable pairs on the board.
	 */
	public static int getCombinableCandies(Cell[][] cellBoard) {
		int combinableCandies = 0;
		for (int x = 0; x < cellBoard.length; ++x) {
			for (int y = 0; y < cellBoard[0].length; ++y) {
				if (y != cellBoard[0].length - 1
						&& areSpecialCombinable(cellBoard[x][y],
								cellBoard[x][y + 1]))
					++combinableCandies;
				if (x != cellBoard.length - 1
						&& areSpecialCombinable(cellBoard[x][y],
								cellBoard[x + 1][y]))
					++combinableCandies;
			}
		}
		return combinableCandies;
	}

	/**
	 * Get the potential based on the special candies on a board. Notice that
	 * this does not include the potential of combinable special candies.
	 * 
	 * @param cellBoard
	 *            The board that will be evaluated.
	 * @param colourBombScore
	 *            The score for each colour bomb.
	 * @param specialCandyScore
	 *            The score for each special candy.
	 * @return The special candies potential of the board.
	 */
	public static double getSpecialCandiesScore(Cell[][] cellBoard,
			double colourBombScore, double specialCandyScore) {
		double count = 0;
		for (Cell[] row : cellBoard) {
			for (Cell cell : row) {
				if (hasColourBomb(cell))
					count += 2.5;
				else if (hasSpecial(cell))
					count += 1.0;
			}
		}
		return count;
	}

	/**
	 * Count the hopeful candies in the board, i.e. the candies that are empty
	 * and will be replaced in the next round.
	 * 
	 * @param cellBoard
	 *            The board that will be examined.
	 * @return The number of hopedul candies.
	 */
	public static int countHopeful(Cell[][] cellBoard) {
		int hopefulCount = 0;
		for (int x = 0; x < cellBoard.length; ++x) {
			for (int y = 0; y < cellBoard[0].length; ++y) {
				if (cellBoard[x][y].blocksCandies())
					break;
				if (hasUnmovable(cellBoard[x][y]))
					++hopefulCount;
			}
		}
		return hopefulCount;
	}

	/**
	 * Function that returns the average number of moves needed to remove a
	 * candy from the specified position (x, y) given the design.
	 * 
	 * @param levelDesign
	 *            The design of the level.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @param numRounds
	 *            The number of rounds for which to run the simulator.
	 * @return The average number of moves to complete it.
	 */
	public static double getCellDifficulty(Design levelDesign, int x, int y,
			int numRounds) {
		Design design = new Design(levelDesign);
		design.setGameMode(GameMode.HIGHSCORE);
		design.setObjectiveTarget(2000000);
		design.setNumberOfMovesAvailable(numRounds);
		design.getBoard()[x][y].setJellyLevel(1);
		design.setCandyGenerator(RandomCandyGenerator.class);
		int difficulty = 0;
		for (int i = 0; i < numRounds; ++i) {
			GameState auxGame = new GameState(design);
			int num = numOfRounds(auxGame, x, y);
			difficulty += num;
		}
		return difficulty / (double) numRounds;
	}

	/**
	 * Function that returns the number of average moves to clear every cell of
	 * the board.
	 * 
	 * @param design
	 *            The design for the level to be checked.
	 * @return The difficulty array for every cell.
	 */
	public static double[][] getBoardDifficulty(Design design,
			int numberOfRoundsToExecute) {
		double[][] difficultyBoard = new double[design.getWidth()][design
				.getHeight()];
		for (int i = 0; i < design.getWidth(); ++i) {
			for (int j = 0; j < design.getHeight(); ++j) {
				difficultyBoard[i][j] = getCellDifficulty(design, i, j,
						numberOfRoundsToExecute);
			}
		}
		return difficultyBoard;
	}

	/**
	 * Function that checks if the cell dis
	 * 
	 * @param cells
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean inBoard(Cell[][] cells, int x, int y) {
		return x >= 0 && y >= 0 && cells.length > y && cells.length > x;
	}

	/**
	 * Function that checks whether the cell can be removed on the next round.
	 * 
	 * @param board
	 *            The board the cell is on.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @return whether there will be a move to remove the cell on the next
	 *         round.
	 */
	public static boolean canBeRemoved(Cell[][] board, int x, int y) {
		// Case 1:
		// NC TX NC
		// NX NC NX
		if (haveSameColor(board, x + 1, y, x - 1, y, x, y + 1)
				|| haveSameColor(board, x + 1, y, x - 1, y, x, y - 1))
			return true;
		// Case 2:
		// NC NX
		// TX NC
		// NC NX
		if (haveSameColor(board, x, y + 1, x, y - 1, x + 1, y)
				|| haveSameColor(board, x, y + 1, x, y - 1, x - 1, y))
			return true;

		if (haveSameColor(board, x, y + 1, x, y + 2, x + 1, y)
				|| haveSameColor(board, x, y + 1, x, y + 2, x - 1, y))
			return true;

		if (haveSameColor(board, x, y - 1, x, y - 2, x + 1, y)
				|| haveSameColor(board, x, y - 1, x, y - 2, x - 1, y))
			return true;

		if (haveSameColor(board, x - 1, y, x - 2, y, x, y + 1)
				|| haveSameColor(board, x - 1, y, x - 2, y, x, y - 1))
			return true;

		if (haveSameColor(board, x + 1, y, x + 2, y, x, y + 1)
				|| haveSameColor(board, x + 1, y, x + 2, y, x, y - 1))
			return true;

		int xStart = x, xEnd = x;
		if (haveSameColor(board, x, y, x - 1, y))
			xStart = x - 1;
		if (haveSameColor(board, x, y, x + 1, y))
			xEnd = x + 1;
		if (xEnd != xStart
				&& (haveSameColor(board, x, y, xStart - 1, y + 1)
						|| haveSameColor(board, x, y, xStart - 1, y - 1)
						|| haveSameColor(board, x, y, xEnd + 1, y + 1) || haveSameColor(
							board, x, y, xEnd - 1, y + 1)))
			return true;

		int yStart = y, yEnd = y;
		if (haveSameColor(board, x, y, x, y - 1))
			yStart = y - 1;
		if (haveSameColor(board, x, y, x, y + 1))
			yEnd = y + 1;
		if (yEnd != yStart
				&& (haveSameColor(board, x, y, x + 1, yEnd + 1)
						|| haveSameColor(board, x, y, x - 1, yEnd - 1)
						|| haveSameColor(board, x, y, x + 1, yStart - 1) || haveSameColor(
							board, x, y, x - 1, yStart + 1)))
			return true;

		return false;
	}

	/**
	 * Function that determines whether the cell on the board will be filled in
	 * the next round.
	 * 
	 * @param board
	 *            The board that the cell is on.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @return whether the cell will be filled in the next round.
	 */
	public static boolean isFillable(Cell[][] board, int x, int y) {
		if (!inBoard(board, x, y))
			return false;
		int curY = y;
		while (curY >= 0) {
			// There is a cell above it that blocks candies.
			if (board[x][curY].blocksCandies())
				return false;
			--curY;
		}
		return true;
	}

	/**
	 * Function that checks whether a cell will be filled in the next round.
	 * 
	 * @param board
	 *            The board where the cell is.
	 * @param x
	 *            The x coordinate of the cell.
	 * @param y
	 *            The y coordinate of the cell.
	 * @return Whether the cell is going to be filled in the next round.
	 */
	public static boolean isHopeful(Cell[][] board, int x, int y) {
		if (!inBoard(board, x, y))
			return false;
		return hasUnmovable(board[x][y]) && isFillable(board, x, y);
	}

	/**
	 * Simple function that converts boolean to int. true -> 1 and false -> 0
	 * 
	 * @param b
	 *            The boolean to be converted.
	 * @return The corresponding int.
	 */
	public static int toInt(boolean b) {
		return b ? 1 : 0;
	}

	/**
	 * Get the probability that the unfilled cells around the position (x, y)
	 * will generate a match that will include the cell at (x, y). It needs the
	 * number of possible different candy colours to calculate the probability.
	 * 
	 * @param board
	 *            The board containing the cell.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @param differentColors
	 *            The number of different colours a candy can take.
	 * @return The probability that the cell (x, y) will be cleared after the
	 *         neighbouring cells have been filled.
	 */
	public static double getProbabilityOfHopefulCells(Cell[][] board, int x,
			int y, int differentColors) {
		int hHopeful = getHorizontalHopeful(board, x, y);
		int vHopeful = getVerticalHopeful(board, x, y);
		double inv = 1.0 / differentColors;
		if (hHopeful >= 3) {
			if (vHopeful >= 3)
				return inv * inv * inv * (vHopeful + hHopeful - 6 + 2);
			return inv * inv * inv * (hHopeful - 3 + 1);
		} else if (vHopeful >= 3) {
			return inv * inv * inv * (hHopeful - 3 + 1);
		}
		int count = 0;
		if (haveSameColor(board, x - 2, y, x - 1, y))
			count += getVerticalHopeful(board, x, y);
		if (haveSameColor(board, x + 2, y, x + 1, y))
			count += getVerticalHopeful(board, x, y);
		if (haveSameColor(board, x, y + 1, x, y + 2))
			count += getHorizontalHopeful(board, x, y);

		if (haveSameColor(board, x - 1, y, x, y))
			count += getVerticalHopeful(board, x + 1, y)
					+ getVerticalHopeful(board, x - 2, y);
		if (haveSameColor(board, x + 1, y, x, y))
			count += getVerticalHopeful(board, x + 2, y)
					+ getVerticalHopeful(board, x - 1, y);
		if (haveSameColor(board, x, y, x, y + 1))
			count += getHorizontalHopeful(board, x, y - 1);

		if (haveSameColor(board, x - 2, y, x, y))
			count += getVerticalHopeful(board, x - 1, y);
		if (haveSameColor(board, x + 2, y, x, y))
			count += getVerticalHopeful(board, x + 1, y);

		double probabilityOfNotGettingDesiredCandy = (differentColors - 1)
				/ differentColors;
		return 1 - Math.pow(probabilityOfNotGettingDesiredCandy, count);
	}

	/**
	 * Function that counts the moves that affect a certain cell.
	 * 
	 * @param gameState
	 *            The game state that will be checked.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @return The moves above and bellow the cell.
	 */
	public static MovesAffectingCell countMovesThatAffectCell(
			GameState gameState, int x, int y) {
		List<Move> moves = gameState.getValidMoves();
		int countBelow = 0, countAbove = 0;
		for (Move move : moves) {
			List<MatchAnalysis> analyses = gameState.getMatchAnalysis(move);
			for (MatchAnalysis analysis : analyses) {
				List<Position> affected = analysis.positionsMatched;
				for (Position pos : affected) {
					if (pos.x >= x - 1 && pos.x <= x + 1) {
						if (pos.y < y)
							++countBelow;
						else
							++countAbove;
					}
				}
			}
		}
		return new MovesAffectingCell(countBelow, countAbove);
	}

	/**
	 * A move potential function that gives a score of how likely it is that the
	 * cell will change based on the moves above and the moves below it.
	 * 
	 * @param movesAffecting
	 *            The moves above and below the cell.
	 * @return The score based on these moves.
	 */
	public static double getMovesDelta(MovesAffectingCell movesAffecting) {
		if (movesAffecting.below == 0)
			return 2.0;
		if (movesAffecting.below >= 3)
			return 0.0;
		if (movesAffecting.below == 2)
			return 1.0;
		/* if (movesDelta == 1) */
		return 0.5;
	}

	/**
	 * Function that returns estimate between 0 and 3.0 of how possible it is
	 * for the cell at position (x, y) to be cleared based on the special
	 * candies near it and the candies to be filled.
	 * 
	 * @param board
	 *            The board where the cell is.
	 * @param x
	 *            The x-coordinate of the cell.
	 * @param y
	 *            The y-coordinate of the cell.
	 * @return the potential that this cell will be moved.
	 */
	public static double getMotionPotential(Cell[][] board, int x, int y) {
		int cellsToBeFilled = 0;
		int cellsWithSpecials = 0;

		final int smoothingFactorSpecials = 10;
		final int smoothingFactorFillable = 5;
		for (int i = x - 1; i <= x + 1; ++i) {
			for (int j = y - 1; j >= 0; --j) {
				if (inBoard(board, i, j)
						&& GameStateAuxiliaryFunctions
								.hasSpecialOrColourBomb(board[i][j])) {
					++cellsWithSpecials;
				} else if (isHopeful(board, i, j)) {
					++cellsToBeFilled;
				}
			}
		}
		double contributorCellsWithSpecial = cellsWithSpecials
				/ ((double) smoothingFactorSpecials + Math.pow(
						cellsWithSpecials - 1, 3));
		double contributorCellsToBeFilled = smoothingFactorFillable
				/ (cellsToBeFilled + (double) smoothingFactorFillable);
		// System.out.println(contributorCellsToBeFilled + " " +
		// cellsToBeFilled);
		return contributorCellsWithSpecial + 2.0 * contributorCellsToBeFilled;
	}

	/**
	 * Checks whether the two cells form a combinable special candy.
	 * 
	 * @param cell1
	 *            The first cell to be checked
	 * @param cell2
	 *            The second cell to be checked.
	 * @return Whether they form a combinable special candy.
	 */
	private static boolean areSpecialCombinable(Cell cell1, Cell cell2) {
		// If any of the cells are null then return false.
		if (cell1 == null || cell2 == null)
			return false;

		return cell1.isMovable() && cell2.isMovable()
				&& hasSpecialOrColourBomb(cell1)
				&& hasSpecialOrColourBomb(cell2);
	}

	/**
	 * Function that gets the cell at (x, y) from the given board and returns
	 * null if the coordinates are out of bounds.
	 *
	 * @param board
	 *            The board on which the cell is on.
	 * @param x
	 *            The x-coordinate for the cell.
	 * @param y
	 *            The y-coordinate for the cell.
	 * @return the cell at that location or null if there is not such.
	 */
	public static Cell getCell(Cell[][] board, int x, int y) {
		if (!inBoard(board, x, y))
			return null;
		return board[x][y];
	}

	// Function that runs a game to clear the cell at (x, y) for
	// and calculates the number of rounds that were required to
	// complete it.
	private static int numOfRounds(GameState game, int x, int y) {
		TargetCellPlayer player = new TargetCellPlayer(2, 10, x, y);
		int rounds = 0;
		while (!game.isGameOver() && game.getCell(x, y).getJellyLevel() != 0) {
			try {
				Move move = player.calculateBestMove(game);
				game.makeFullMove(move);
			} catch (NoMovesFoundException e) {
				e.printStackTrace();
			} catch (InvalidMoveException e) {
				e.printStackTrace();
			}
			rounds++;
		}
		return rounds;
	}

	// Function that checks if the cells (x1, y1) and (x2, y2) have candies of
	// the same colour.
	private static boolean haveSameColor(Cell[][] cellBoard, int x1, int y1,
			int x2, int y2) {
		Cell cell1 = getCell(cellBoard, x1, y1);
		Cell cell2 = getCell(cellBoard, x2, y2);
		if (cell1 == null || cell2 == null)
			return false;
		return cell1.hasCandy()
				&& cell1.getCandy().getColour() != null
				&& cell2.hasCandy()
				&& cell2.getCandy().getColour() != null
				&& cell1.getCandy().getColour()
						.equals(cell2.getCandy().getColour())
				&& !cell1.getCandy().getCandyType().equals(CandyType.UNMOVABLE);
	}

	// Function that checks if the cells (x1, y1), (x2, y2) and (x3, y3) on the
	// board have the same colour.
	private static boolean haveSameColor(Cell[][] board, int x1, int y1,
			int x2, int y2, int x3, int y3) {
		return haveSameColor(board, x1, y1, x2, y2)
				&& haveSameColor(board, x1, y1, x3, y3);
	}

	// Function that checks if the cell has an UnmovableCandy
	private static boolean hasUnmovable(Cell cell) {
		return cell.hasCandy()
				&& cell.getCandy().getCandyType().equals(CandyType.UNMOVABLE);
	}

	// Function that gets the number of hopeful cells in the consecutive
	// horizontal triple containing (x, y).
	private static int getVerticalHopeful(Cell[][] board, int x, int y) {
		return toInt(isHopeful(board, x, y))
				+ toInt(isHopeful(board, x, y + 1))
				+ toInt(isHopeful(board, x, y - 1));
	}

	// Function that gets the number of hopeful cells in the consecutive
	// horizontal triple containing (x, y).
	private static int getHorizontalHopeful(Cell[][] board, int x, int y) {
		return toInt(isHopeful(board, x - 1, y))
				+ toInt(isHopeful(board, x + 1, y))
				+ toInt(isHopeful(board, x, y));
	}
}
