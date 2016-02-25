package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;


public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

    public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(getJellyNumber(gameState));
    }
}
