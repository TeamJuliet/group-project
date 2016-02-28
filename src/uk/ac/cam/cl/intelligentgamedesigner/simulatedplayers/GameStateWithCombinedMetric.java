package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

/**
 * 
 * Storage class that keeps the metric of a game state together with the game
 * state and the original move that lead to this state.
 *
 */
public class GameStateWithCombinedMetric implements
		Comparable<GameStateWithCombinedMetric> {
	/**
	 * The game state for which the metric is being evaluated.
	 */
	public final GameState gameState;

	/**
	 * The game state metric for this game state.
	 */
	public final GameStateCombinedMetric gameStateMetric;

	/**
	 * The move that lead to this state.
	 */
	public final Move originalMove;

	/**
	 * Default Constructor.
	 * 
	 * @param gameState
	 *            The game state for which the metric is given.
	 * @param gameStateMetric
	 *            The metric for the game state.
	 * @param originalMove
	 *            The move that lead here.
	 */
	public GameStateWithCombinedMetric(GameState gameState,
			GameStateCombinedMetric gameStateMetric, Move originalMove) {
		this.gameState = new GameState(gameState);
		this.gameStateMetric = gameStateMetric;
		this.originalMove = originalMove;
	}

	@Override
	public int compareTo(GameStateWithCombinedMetric o) {
		return gameStateMetric.compareTo(o.gameStateMetric);
	}

}
