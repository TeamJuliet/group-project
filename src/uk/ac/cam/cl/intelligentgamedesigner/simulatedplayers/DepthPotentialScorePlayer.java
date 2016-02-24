package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialScorePlayer extends DepthPotentialPlayer {

    public DepthPotentialScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.levelDesign.getObjectiveTarget() - gameState.getGameProgress().score);
    }

    @Override
    GameStatePotential getGameStatePotential(GameState gameState) {
        // Return the highest increase in score of all possible matches
        GameState original = new GameState(gameState, new UnmovableCandyGenerator());
        int highestIncrease = 0;
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

            int increase = tmp.getGameProgress().score - original.getGameProgress().score;
            if (increase > highestIncrease)
                highestIncrease = increase;
        }
        return new GameStatePotential(highestIncrease);
    }

    @Override
    protected GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        // Value metric and potential equally for now and find arithmetic mean
        return new GameStateCombinedMetric(metric, potential, (metric.metric + potential.potential) / 2);
    }

    @Override
    protected List<Move> selectMoves(GameState gameState) {
        // TODO: look more into filtering moves
        return gameState.getValidMoves();
    }

}
