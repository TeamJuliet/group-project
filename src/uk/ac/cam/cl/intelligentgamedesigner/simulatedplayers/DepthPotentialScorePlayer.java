package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialScorePlayer extends DepthPotentialPlayer {

    public DepthPotentialScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.levelDesign.getObjectiveTarget() - gameState.getGameProgress().score);
    }
}
