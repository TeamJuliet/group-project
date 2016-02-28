package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

/**
 * 
 * Basic player, find the move that makes the longest match and make it.
 *
 */
public class ScorePlayerBeta extends SimulatedPlayerBase {

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        GameState original = new GameState(currentState, new UnmovableCandyGenerator());
        List<Move> validMoves = original.getValidMoves();
        
        int largestMatch = 0;
        if (validMoves.isEmpty())
            throw new NoMovesFoundException(currentState);
        
        Move bestMove = null;
        for (Move candidateMove : validMoves) {
            // TODO: This is unnecessary since match analysis does not alter the board.
            GameState tmp = new GameState(original);
            List<MatchAnalysis> matches = tmp.getMatchAnalysis(candidateMove);
            
            // Count the number of positions that were directly removed.
            int numberOfCellsMathed = 0;
            for (MatchAnalysis match : matches) {
                numberOfCellsMathed += match.positionsMatched.size();
            }
            
            // Check to update maximum match.
            if (numberOfCellsMathed > largestMatch) {
                largestMatch = numberOfCellsMathed;
                bestMove = candidateMove;
            }
        }
        return bestMove;
    }
}
