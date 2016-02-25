package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;

import java.util.List;

public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

    public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(getJellyNumber(gameState));
    }

    @Override
    public GameStatePotential getGameStatePotential(GameState gameState) {
        // Return the highest increase in score of all possible matches
        GameState original = new GameState(gameState, new UnmovableCandyGenerator());
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

    @Override
    public GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        return new GameStateCombinedMetric(metric, potential, (metric.metric + potential.potential) / 2);
    }
}
