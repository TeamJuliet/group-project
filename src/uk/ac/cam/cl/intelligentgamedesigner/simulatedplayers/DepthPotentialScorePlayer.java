package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmoveableCandyGenerator;

//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialScorePlayer extends DepthPotentialPlayer {

    public DepthPotentialScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead,numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.getGameProgress().score);
    }
}
