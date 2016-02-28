package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

/**
 * 
 * A class of players that is based on the a * search. The give for each state a
 * metric and a potential and tries to find looking ahead several moves in a
 * restricted pool.
 *
 */
public abstract class DepthPotentialPlayer extends SimulatedPlayerBase {
	/**
	 * The number of states that the Player should look ahead at each move.
	 * Note: when this is -1, it generates all possible moves.
	 */
	protected final int numOfStatesAhead;

	/**
	 * The number of states in the pool that will be chosen to be explored next.
	 * Note: again, when this is -1, it discovers all possible moves.
	 */
	protected final int numOfStatesInPool;

	// The pool of game states with their respective metric that are currently
	// being considered.
	private PriorityQueue<GameStateWithCombinedMetric> pool;

	// The pool of game states with their respective metric that have been
	// completed or have no more moves.
	private PriorityQueue<GameStateWithCombinedMetric> results;

	/**
	 * Function that evaluates the current game state based on the knowledge at
	 * that particular state.
	 * 
	 * @param gameState
	 *            The game state to be evaluated.
	 * @return The metric for that state of the game.
	 */
	abstract GameStateMetric getGameStateMetric(GameState gameState);

	/**
	 * Function that evaluates the current game state based on its potential of
	 * making progress towards the goal. (e.g. if there is a large number of
	 * cells containing jellies refreshed, etc).
	 * 
	 * @param gameState
	 *            The game state that will be evaluated.
	 * @return The potential for that game state.
	 */
	GameStatePotential getGameStatePotential(GameState gameState) {
		// Return the highest increase in score of all possible matches
		GameState original = new GameState(gameState,
				new UnmovableCandyGenerator());
		GameStateMetric bestMetric = null;
		List<Move> moves = original.getValidMoves();
		for (Move move : moves) {
			GameState tmp = new GameState(original);
			try {
				tmp.makeFullMove(move);
			} catch (InvalidMoveException e) {
				// Just eat the exception since we don't care if wrong move is
				// suggested
				printInvalidSuggestionError(tmp, move);
				continue;
			}

			GameStateMetric nextMetric = getGameStateMetric(tmp);
			if (nextMetric.compareTo(bestMetric) == -1)
				bestMetric = nextMetric;
		}
		if (bestMetric == null)
			return new GameStatePotential(Integer.MAX_VALUE);
		return new GameStatePotential(bestMetric);
	}

	/**
	 * Function that is used for combining the metric and the potential for that
	 * game state.
	 * 
	 * @param metric
	 *            The metric for a game state.
	 * @param potential
	 *            The potential for a game state.
	 * @return The Combined metric for the two.
	 */
	GameStateCombinedMetric getCombinedMetric(GameStateMetric metric,
			GameStatePotential potential) {
		return new GameStateCombinedMetric(metric, potential,
				(metric.metric + potential.potential) / 2);
	}

	/**
	 * Function that filters the moves on the board that will be checked next.
	 * 
	 * @param gameState
	 *            Game state where the moves will be fetched from.
	 * @return A list of moves that the player will choose from.
	 */
	protected List<Move> selectMoves(GameState gameState) {
		// Shuffle the moves so that they are not biased to the left and upper
		// most part of the board.
		List<Move> ret = gameState.getValidMoves();
		Collections.shuffle(ret);
		return ret;
	}

	/**
	 * Function that generates combined metric for game state that would occur
	 * if the move was made.
	 * 
	 * @param state
	 *            GameStateWithCombinedMetric to be used for evaluation.
	 * @param move
	 *            Move to be evaluated.
	 * @return GameStateWithCombinedMetric instance containing evaluation
	 *         results
	 */

	private GameStateWithCombinedMetric generateCombinedMetric(
			GameStateWithCombinedMetric state, Move move) {
		GameState nextState;
		try {
			nextState = simulateNextMove(state.gameState, move);
		} catch (InvalidMoveException e) {
			System.err.println("Some of the moves generated are not possible.");
			System.out.println(state.gameState);
			System.out.println(state.gameState.getValidMoves());
			e.printStackTrace();
			return null;
		}
		Move moveRecorded = state.originalMove == null ? move
				: state.originalMove;
		return new GameStateWithCombinedMetric(nextState,
				getCombinedMetric(getGameStateMetric(nextState),
						getGameStatePotential(nextState)), moveRecorded);
	}

	// Function that calculates the next depth for the game states given.
	private void nextDepth() {
		int upperLimit = numOfStatesInPool == -1 ? pool.size()
				: numOfStatesInPool;
		int elementsProcessed = 0;
		PriorityQueue<GameStateWithCombinedMetric> nextPool = new PriorityQueue<GameStateWithCombinedMetric>(
				numOfStatesInPool);
		while (elementsProcessed < upperLimit && !pool.isEmpty()) {
			GameStateWithCombinedMetric current = pool.poll();
			List<Move> moves = selectMoves(current.gameState);
			if (moves.isEmpty() || current.gameState.isGameOver()) {
				results.add(current);
				continue;
			}
			for (Move move : moves) {
				nextPool.add(generateCombinedMetric(current, move));
			}

		}
		pool = nextPool;
	}

	/**
	 * Function that returns the best move for the game state given
	 */
	public Move calculateBestMove(GameState currentState)
			throws NoMovesFoundException {
		List<Move> moves = currentState.getValidMoves();
		if (moves.size() == 0)
			throw new NoMovesFoundException(currentState);
		pool = new PriorityQueue<GameStateWithCombinedMetric>();
		results = new PriorityQueue<GameStateWithCombinedMetric>();
		pool.add(new GameStateWithCombinedMetric(currentState,
				new GameStateCombinedMetric(), null));

		int stages = 0;
		while (stages < numOfStatesAhead
				|| (numOfStatesAhead == -1 && !pool.isEmpty())) {
			nextDepth();
			stages++;
		}

		// Copy all states in the results pool for evaluation.
		while (!pool.isEmpty()) {
			results.add(pool.poll());
		}

		// Return the move with the minimum score.
		Move moveMake = results.peek().originalMove;
		results.clear();
		return moveMake;
	}

	/**
	 * Construct a depth potential player by specifying the number of states
	 * that should be looked ahead and the states that should be kept in the
	 * pool.
	 * 
	 * @param numOfStatesAhead
	 *            Number of steps lookahead.
	 * @param numOfStatesInPool
	 *            Number of steps that will be kept in the pool.
	 */
	public DepthPotentialPlayer(int numOfStatesAhead, int numOfStatesInPool) {
		this.numOfStatesAhead = numOfStatesAhead;
		this.numOfStatesInPool = numOfStatesInPool;
	}

	/**
	 * The default constructor for the player.
	 */
	public DepthPotentialPlayer() {
		this(2, 15);
	}

	/**
	 * Function that is used to simulate the next move on the game state without
	 * affecting the given game state.
	 * 
	 * @param gameState
	 *            The game state where the move should be made.
	 * @param move
	 *            The move that has to be tested.
	 * @return The state of the next board after applying the move.
	 * @throws InvalidMoveException
	 *             If the move given is not valid.
	 */
	protected GameState simulateNextMove(GameState gameState, Move move)
			throws InvalidMoveException {
		GameState nextState = new GameState(gameState,
				new UnmovableCandyGenerator());
		nextState.makeFullMove(move);
		return nextState;
	}

	/**
	 * Function that is used to print the error for choosing a move that does
	 * not exist on the given board.
	 * 
	 * @param level
	 *            The game state where the invalid move was given.
	 * @param move
	 *            The move that was given.
	 */
	protected void printInvalidSuggestionError(GameState level, Move move) {
		System.err.format(
				"WARNING! Invalid move suggested in %s (%d,%d) evaluation:\n"
						+ level + "\n" + move + ".\n", this.getClass()
						.getSimpleName(), this.numOfStatesAhead,
				this.numOfStatesInPool);
		System.err.println(level.getValidMoves());
	}
}
