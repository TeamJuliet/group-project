package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmoveableCandyGenerator;

public class ScorePlayerBeta implements SimulatedPlayerBase {
    GameState level;

    public ScorePlayerBeta(GameState level) {
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

    private void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerBeta has suggested an invalidMove " + move + ".");
    }

    public static Move calculateBestMove(GameState currentState) {
        GameState original = new GameState(currentState, new UnmoveableCandyGenerator());
        List<Move> validMoves = original.getValidMoves();
        int largestMatch = 0;
        Move bestMove = null;
        for (Move candidateMove : validMoves) {
            SimGS tmp = (SimGS) original;
            List<MatchAnalysis> matches = tmp.getMatchAnalysis(candidateMove);
            int numberOfCellsMathed = 0;
            for (MatchAnalysis match : matches) {
                numberOfCellsMathed += match.positionsMatched.size();
            }
            if (numberOfCellsMathed > largestMatch) {
                largestMatch = numberOfCellsMathed;
                bestMove = candidateMove;
            }
        }
        return bestMove;
    }
}
