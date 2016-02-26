package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class RuleBasedIngredientsPlayer extends SimulatedPlayerBase {
    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        List<Move> moves = currentState.getValidMoves();
        if (moves.isEmpty()) throw new NoMovesFoundException(currentState);
        
        int[][] ingredientsCount = RuleBasedAuxiliaryFunctions.precomputeIngredientsCount(currentState.getBoard());
        int[][] inverseIngredientsCount = RuleBasedAuxiliaryFunctions.precomputeInverseIngredientsCount(currentState.getBoard());
        
        List<MatchAnalysis> bestMatches = null;
        Move bestMove = moves.get(0);
        
        for (Move move : moves) {
            List<MatchAnalysis> matchAnalyses = currentState.getMatchAnalysis(move);
            // If there is a move combining two special candies then do it. 
            if (matchAnalyses.isEmpty()) return move;
            
            if (RuleBasedAuxiliaryFunctions.isIngredientsBetter(bestMatches, matchAnalyses, ingredientsCount, inverseIngredientsCount)) {
                bestMatches = matchAnalyses;
                bestMove = move;
            }
        }
        
        return bestMove;
    }

}
