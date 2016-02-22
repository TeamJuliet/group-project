package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.GameStateMetric.add;

//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialScorePlayer extends DepthPotentialPlayer {

    public DepthPotentialScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.getGameProgress().score);
    }
}
