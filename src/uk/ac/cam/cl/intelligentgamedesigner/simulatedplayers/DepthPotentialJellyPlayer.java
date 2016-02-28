package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;

/**
 * As the aim is to clear all jellies we can use the number of jellies on the
 * board as distance.
 *
 */
public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

	public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
		super(numOfStatesAhead, numOfStatesInPool);
	}

	@Override
	public GameStateMetric getGameStateMetric(GameState gameState) {
		return new GameStateMetric(getJellyNumber(gameState));
	}
}
