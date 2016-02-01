package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class ScorePlayerBeta implements SimulatedPlayerBase {
    GameState level;

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
        System.err.println("WARNING! ScorePlayerBeta has suggested an invalidMove " + move + ".");
    }

    @Override
    public Move calculateBestMove(GameState currentState) {
        List<Move> validMoves = level.getValidMoves();
        int largestMatch = 0;
        Move bestMove = null;
        for (Move candidateMove : validMoves) {
            GameStateForSimulatedPlayers tmp = (GameStateForSimulatedPlayers) level;
            // perhaps we don't need to copy in this case, we can just swap back
            // at the end
            // but we will definetly need a rollback or copy in more
            // sophisticated AI
            tmp.swapCandies(candidateMove);
            int numberOfCellsMathed = 0; // TODO:check how many cells are
                                         // matched
            if (numberOfCellsMathed > largestMatch) {
                largestMatch = numberOfCellsMathed;
                bestMove = candidateMove;
            }
        }
        return bestMove;
    }

}
