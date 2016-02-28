package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.RuleBasedAuxiliaryFunctions.isJellyBetter;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

/**
 * 
 * This is a simple rule based player for clearing jelly levels. The order with
 * which it evaluates moves is by considering moves that are of combinable
 * candies, moves that remove jellies, moves that remove blockers, moves that
 * remove specials and then an element of randomness.
 *
 */
public class RuleBasedJellyPlayer extends SimulatedPlayerBase {

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        List<Move> moves = currentState.getValidMoves();
        if (moves.isEmpty())
            throw new NoMovesFoundException(currentState);

        List<MatchAnalysis> bestMatches = null;
        Move bestMove = moves.get(0);

        for (Move move : moves) {
            List<MatchAnalysis> matchAnalyses = currentState.getMatchAnalysis(move);
            // If there is a move combining two special candies then do it.
            if (matchAnalyses.isEmpty())
                return move;

            if (isJellyBetter(bestMatches, matchAnalyses)) {
                bestMatches = matchAnalyses;
                bestMove = move;
            }
        }

        return bestMove;
    }

}
