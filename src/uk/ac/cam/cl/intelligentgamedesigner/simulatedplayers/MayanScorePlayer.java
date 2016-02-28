package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countHopeful;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getSpecialCandiesScore;

import java.util.Collections;
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
 * Score player that uses several considerations in the metric apart from score
 * distance, including the difficulty of removing certain jellies, the
 * probability of removing blockers in close rounds, encourages the formation of
 * candies and the generation of combinable candies.
 *
 */
public class MayanScorePlayer extends DepthPotentialPlayer {
	// The blockers at the boundary are given a fraction of the score for normal
	// blockers.
	private static final double BLOCKER_AT_BOUNDARY_CONSTANT = 0.5;

	// A smoothing on the score. (In the future maybe this could be dynamic).
	private static final double SCORE_SMOOTHING = 0.005;

	// The coefficient for the empty cells on the board that will be filled.
	private static final double HOPEFUL_BOOST = 1.5;

	// The score that will be counted for each combinable candies on the board.
	private static final double COMBINABLE_CANDY_SCORE = 18.0;

	// The score that will be counted for each special candy.
	private static final double SPECIAL_CANDY_SCORE = 1.2;

	// The score that will be counted for each colour bomb on the board.
	private static final double COLOUR_BOMB_SCORE = 2.5;

	// Precompute the jelly and blockers positions.
	private List<Position> jellies = new LinkedList<Position>(),
			blockers = new LinkedList<Position>();

	// The design with which the latest game was created, so that we do not
	// loose time in computing the same things.
	private Design referenceDesign = null;

	/**
	 * Construct the Mayan player specifying the number of states that it should
	 * look ahead and the number of states it could have in pool.
	 * 
	 * Note: the higher the parameters the better the player is going to
	 * perform, but in the expense of speed.
	 * 
	 * @param numOfStatesAhead
	 * @param numOfStatesInPool
	 */
	public MayanScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
		super(numOfStatesAhead, numOfStatesInPool);
	}

	@Override
	public GameStateMetric getGameStateMetric(GameState gameState) {
		double score = 0.0;
		// If the game is over then the score will be just zero (meaning that we
		// have reached the target.
		if (!gameState.isGameOver()) {
			Cell[][] board = gameState.getBoard();
			// Accelerates jellies detonation when the number of moves
			// approaches 0 or the number
			// of jellies approaches zero.
			final double targetAlpha = targetWeight(gameState.getGameProgress().movesRemaining);

			// The normalised distance to the target score.
			final double scoreDistance = (gameState.levelDesign
					.getObjectiveTarget() - gameState.getGameProgress().score)
					* SCORE_SMOOTHING;

			score = (2.0 + targetAlpha)
			// Term that describes the target related features (that
			// should be accelerated towards the end).
					* (scoreDistance) + (1.0 - targetAlpha)
					// Term that is measuring features that will help in the
					// future.
					* (getBlockersDifficulty(board) + 1.5
							* getCandyScore(board) + HOPEFUL_BOOST
							* hopefulCellsScore(board));
		}
		return new ScalarGameMetric(score);
	}

	@Override
	public Move calculateBestMove(GameState currentState)
			throws NoMovesFoundException {
		// Check if the reference level design has changed.
		if (referenceDesign != currentState.levelDesign) {
			referenceDesign = currentState.levelDesign;
			// If it has then we have to perform the precomputation.
			recordJelliesAndBlockers(referenceDesign);
		}
		return super.calculateBestMove(currentState);
	}

	@Override
	public GameStatePotential getGameStatePotential(GameState gameState) {
		// There is no potential calculated for this player.
		return null;
	}

	@Override
	public GameStateCombinedMetric getCombinedMetric(GameStateMetric metric,
			GameStatePotential potential) {
		return new ScalarCombinedMetric(((ScalarGameMetric) metric).score);
	}

	// Function that records the jellies and blockers on the board.
	private void recordJelliesAndBlockers(Design design) {
		Cell[][] cellBoard = design.getBoard();
		for (int x = 0; x < cellBoard.length; ++x) {
			for (int y = 0; y < cellBoard[0].length; ++y) {
				Position currentPosition = new Position(x, y);
				if (cellBoard[x][y].getJellyLevel() > 0) {
					this.jellies.add(currentPosition);
				}
				if (cellBoard[x][y].getCellType().isBlocker()) {
					this.blockers.add(currentPosition);
				}
			}
		}
	}

	// Function that returns the difficulty of the board based on the blocker
	// blocker positioned at (x, y). Note that there is no check that this is
	// actually a blocker.
	private double getBlockerCriticality(Cell[][] board, int x, int y) {
		double multiplier;
		if (x == board.length - 1 || x == 0)
			multiplier = BLOCKER_AT_BOUNDARY_CONSTANT;
		else
			multiplier = 1.0;
		return multiplier
				* (2.5 - Math.exp(-0.2 * countBlockersHeight(board, x, y)));
	}

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

	// Auxiliary functions that counts how many cells bellow it does a blocker
	// block.
	private int countBlockersHeight(Cell[][] board, int x, int y) {
		int count = 0;
		for (int j = y + 1; j < board.length; ++j) {
			if (!board[x][y].getCellType().equals(CellType.UNUSABLE)) {
				++count;
			}
		}
		return count;
	}

	// Auxiliary function that gives a score to the board based on the candies
	// that appear there.
	private double getCandyScore(Cell[][] board) {
		int combinableCandies = BoardDifficultyGenerator
				.getCombinableCandies(board);
		double combinableCandiesScore;
		// For the first combinable candies we reduce the score by 6.0
		switch (combinableCandies) {
		case 0:
			combinableCandiesScore = COMBINABLE_CANDY_SCORE * 3;
			break;
		case 1:
			combinableCandiesScore = COMBINABLE_CANDY_SCORE * 2;
			break;
		case 2:
			combinableCandiesScore = 6.0;
			break;

		// If there are two or more than two combinable candies then add no
		// score (meaning that this is the best scenario).
		default:
			combinableCandiesScore = 0.0;
		}

		// Calculate a score based on the formed candies.
		double candiesScore = 15.0 - getSpecialCandiesScore(board,
				COLOUR_BOMB_SCORE, SPECIAL_CANDY_SCORE);

		// That should rarely be the case, but for safety that should be
		// positive.
		if (candiesScore < 0.0)
			candiesScore = 0.0;
		return combinableCandiesScore + candiesScore;
	}

	// Function that gives the appropriate weight towards the end of the game.
	private static double targetWeight(int movesRemaining) {
		return 0.5 * Math.exp(-0.2 * movesRemaining);
	}

	// Function that evaluates the hopeful cells on the board.
	private double hopefulCellsScore(Cell[][] cellBoard) {
		return 10.0 - countHopeful(cellBoard) / 2.0;
	}

	/**
	 * If there is any move that forms a combinable candy then it should be
	 * performed.
	 */
	@Override
	protected List<Move> selectMoves(GameState gameState) {
		List<Move> ret = gameState.getValidMoves();
		List<Move> tmp = new LinkedList<Move>();
		for (Move move : ret) {
			if (GameStateAuxiliaryFunctions.hasSpecialOrColourBomb(gameState
					.getCell(move.p1.x, move.p1.y))
					&& GameStateAuxiliaryFunctions
							.hasSpecialOrColourBomb(gameState.getCell(
									move.p2.x, move.p2.y)))
				tmp.add(move);
		}
		if (!tmp.isEmpty())
			return tmp;
		Collections.shuffle(ret);
		return ret;
	}

}
