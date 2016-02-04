package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class ScorePlayerGamma implements SimulatedPlayerBase {
    GameState level;

    public ScorePlayerGamma(GameState level){
        this.level = level;
    }
    
    @Override
    public void solve() {
        while (level.getMovesRemaining() > 0) {
            Move bestMove = calculateBestMove(level);
            try {
                level.makeMove(bestMove);
            } catch (InvalidMoveException e) {
                printInvalidMoveError(e.invalidMove);
                try { // TODO: this is horrible, fix it
                    level.makeMove(level.getValidMoves().get(0));
                } catch (InvalidMoveException exception) {
                    return;
                }
            }
        }
    }

    @Override
    public void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerGamma has suggested an invalidMove " + move + ".");
    }

    @Override
    public Move calculateBestMove(GameState currentState) {
        /* Simple look ahead AI
         * Consider each move
         * Consider best possible move after one
         * Pick best combination of two moves
         */
        Move bestMove = null;
        int largestMatch = 0;
        List<Move> validMoves = level.getValidMoves();
        for(Move candidateMove: validMoves){
            SimGS tmp = (SimGS) level;
        }
        return bestMove;
    }
}
