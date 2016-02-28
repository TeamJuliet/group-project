package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.canBeRemoved;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countHopeful;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMotionPotential;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getProbabilityOfHopefulCells;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getSpecialCandiesScore;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

/**
 * 
 * Player that removes jelly based on the jellies remaining on the board, the
 * potential to remove this jellies in the next moves, the potential to make
 * combinable or special candies and the potential to remove blockers.
 *
 */
public class JellyRemoverPlayerLuna extends DepthPotentialPlayer {

	// The number of rounds for which the target player will be run.
	private static final int NUMBER_OF_ROUNDS = 15;

	// Constant that reduces score for jellies that are removable.
	private static final double IS_REMOVABLE = 0.5;

	// The weight given to the potential of motion rather than the potential of
	// surrounding moves.
	private static final double ALPHA_LIKELIHOOD = 0.8;

	// Score reduced for blockers at the boundary of the board.
	private static final double BLOCKER_AT_BOUNDARY = 0.5;

	// Score given by each special candy on the board.
	private static final double SPECIAL_CANDY_SCORE = 1.2;

	// Score given by each colour bomb on the board.
	private static final double COLOUR_BOMB_SCORE = 2.5;

	// The difficulty of positions (also stored for efficiency).
	private HashMap<Position, Double> difficultyOfFixedPositions = new HashMap<Position, Double>();

	// The positions of jellies and blockers (stored for efficiency).
	private List<Position> jellies = new LinkedList<Position>(),
			blockers = new LinkedList<Position>();

	// The design for the last game state.
	private Design levelDesign = null;

	/**
	 * Construct player by providing the arguments for the super class.
	 * 
	 * @param numOfStatesAhead
	 *            Number of states that will be looked ahead.
	 * @param numOfStatesInPool
	 *            Number of states that will be kept in the pool.
	 */
	public JellyRemoverPlayerLuna(int numOfStatesAhead, int numOfStatesInPool) {
		super(numOfStatesAhead, numOfStatesInPool);
	}

	@Override
	public Move calculateBestMove(GameState currentState)
			throws NoMovesFoundException {
		if (this.levelDesign != currentState.levelDesign) {
			this.levelDesign = currentState.levelDesign;
			fillDifficultyOfFixedPositions(this.levelDesign);
		}
		return super.calculateBestMove(currentState);
	}

	@Override
	public GameStateMetric getGameStateMetric(GameState gameState) {
		double score = 0.0;
		if (!gameState.isGameWon()) {
			Cell[][] board = gameState.getBoard();
			// Accelerates jellies detonation when the number of moves
			// approaches 0 or the number
			// of jellies approaches zero.
			final double targetAlpha = targetWeight(gameState.getGameProgress().movesRemaining);
			score = (2.0 + targetAlpha)
					* getJelliesDifficulty(board)
					+ (1.0 - targetAlpha)
					* (getBlockersDifficulty(board) + getCandyScore(board) + hopefulCellsScore(board));
		}
		return new ScalarGameMetric(score);
	}

	@Override
	public GameStatePotential getGameStatePotential(GameState gameState) {
		// Doesn't use gameStatePotential
		return null;
	};

	@Override
	public GameStateCombinedMetric getCombinedMetric(GameStateMetric metric,
			GameStatePotential potential) {
		return new ScalarCombinedMetric(((ScalarGameMetric) metric).score);
	}

	@Override
	protected List<Move> selectMoves(GameState gameState) {
		List<Move> ret = gameState.getValidMoves();
		// If there is a combinable candy then just make it (and moves are 3 and
		// above).
		List<Move> tmp = new LinkedList<Move>();
		if (gameState.getGameProgress().movesRemaining > 2) {
			for (Move move : ret) {
				if (GameStateAuxiliaryFunctions
						.hasSpecialOrColourBomb(gameState.getCell(move.p1.x,
								move.p1.y))
						&& GameStateAuxiliaryFunctions
								.hasSpecialOrColourBomb(gameState.getCell(
										move.p2.x, move.p2.y)))
					tmp.add(move);
			}
		}
		if (!tmp.isEmpty())
			return tmp;
		Collections.shuffle(ret);
		return ret;
	}

	// Function that calculates the difficulty for the jellies on the board.
	private double getJelliesDifficulty(Cell[][] board) {
		double jellyScore = 0.0;
		for (Position jellyPosition : this.jellies) {
			int x = jellyPosition.x, y = jellyPosition.y;
			Cell cell = board[x][y];

			if (cell.getJellyLevel() > 0) {
				// The difficulty for the position that was precomputed.
				final double positionDifficulty = this.difficultyOfFixedPositions
						.get(jellyPosition);

				double multiplier;
				if (canBeRemoved(board, x, y)) {
					// If the cell can be moved on exactly the next move then
					// give something in the range of 0.5 (smaller score so that
					// it is evaluated as a better position).
					multiplier = IS_REMOVABLE;
				} else {
					// Calculate the likelihood of the jelly being cleared on
					// the next move (based on the cells that are currently
					// empty, but are going to be filled).
					final double likelihood = getProbabilityOfHopefulCells(
							board, x, y,
							this.levelDesign.getNumberOfCandyColours());

					// Get the potential based on the moves that are going to be
					// made above and below that cell.
					final double potential = getMotionPotential(board, x, y);

					// The multiplier between 0.5 and 1.0 that gives the
					// probability of clearing the jelly.
					multiplier = 1.0 - ((1.0 - ALPHA_LIKELIHOOD) * potential
							/ 3.0 + ALPHA_LIKELIHOOD * likelihood) / 2.0;
				}

				// Increment the jelly score based on the jelly level of the
				// cell, the multiplier and the difficulty of the position.
				// Note: that the position difficulty normalizes the metric in
				// terms of the average moves needed.
				jellyScore += cell.getJellyLevel() * multiplier
						* positionDifficulty;
			}
		}
		return jellyScore;
	}

	// Function that returns the difficulty for all the blockers on the board.
	private double getBlockersDifficulty(Cell[][] board) {
		double score = 0.0;
		for (Position blockerPosition : this.blockers) {
			if (board[blockerPosition.x][blockerPosition.y].getCellType()
					.isBlocker())
				score += getBlockerCriticality(board, blockerPosition.x,
						blockerPosition.y);
		}
		return score;
	}

	// Function that returns the blocker difficulty for a single cell.
	private double getBlockerCriticality(Cell[][] board, int x, int y) {
		double multiplier;
		if (x == board.length - 1 || x == 0)
			multiplier = BLOCKER_AT_BOUNDARY;
		else
			multiplier = 1.0;
		return multiplier
				* (2.5 - Math.exp(-0.2 * countBlockersHeight(board, x, y)));
	}

	// Function that counts the height of a blocker cell at (x, y). Note: this
	// cell might not actually be a blocker.
	private static int countBlockersHeight(Cell[][] board, int x, int y) {
		int count = 0;
		for (int j = y + 1; j < board.length; ++j) {
			if (!board[x][y].getCellType().equals(CellType.UNUSABLE)) {
				++count;
			}
		}
		return count;
	}

	// Function that returns the score for the candies on the board.
	private static double getCandyScore(Cell[][] board) {
		int combinableCandies = BoardDifficultyGenerator
				.getCombinableCandies(board);
		double combinableCandiesScore;
		// The evaluation of combinable candies on the board.
		switch (combinableCandies) {
		case 0:
			combinableCandiesScore = 30.0;
			break;
		case 1:
			combinableCandiesScore = 15.0;
			break;
		default:
			combinableCandiesScore = 0.0;
		}

		double candiesScore = 10.0 - getSpecialCandiesScore(board,
				COLOUR_BOMB_SCORE, SPECIAL_CANDY_SCORE);
		if (candiesScore < 0.0)
			candiesScore = 0.0;
		return combinableCandiesScore + candiesScore;
	}

	// Function that is called for acceleration of the target coefficient
	// towards the end of the game.
	private static double targetWeight(int movesRemaining) {
		return Math.exp(-0.2 * movesRemaining);
	}

	// Calculates the score of the cells that are emptied and will be filled in
	// the next round.
	private static double hopefulCellsScore(Cell[][] cellBoard) {
		int numOfHopeful = countHopeful(cellBoard);
		return 20.0 - ((double) numOfHopeful) / 2.0;
	}

	// Function that precomputes the difficulty of the fixed positions. (Note:
	// that this is an intensive procedure and that is why it is run only once
	// for the board design).
	private void fillDifficultyOfFixedPositions(Design design) {
		this.levelDesign = design;
		Cell[][] cellBoard = design.getBoard();
		for (int x = 0; x < cellBoard.length; ++x) {
			for (int y = 0; y < cellBoard[0].length; ++y) {
				Position currentPosition = new Position(x, y);
				if (cellBoard[x][y].getJellyLevel() > 0) {
					this.difficultyOfFixedPositions.put(currentPosition,
							BoardDifficultyGenerator.getCellDifficulty(design,
									x, y, NUMBER_OF_ROUNDS));
					this.jellies.add(currentPosition);
				}
				if (cellBoard[x][y].getCellType().isBlocker()) {
					this.blockers.add(currentPosition);
				}
			}
		}
	}

}
