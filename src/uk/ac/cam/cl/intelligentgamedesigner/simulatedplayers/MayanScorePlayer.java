package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countHopeful;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getSpecialCandiesScore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

public class MayanScorePlayer extends DepthPotentialPlayer {
    private final double   blockerAtBoundaryConstant = 0.5;
    private final double   scoreSmoothing            = 0.0005;
    private final double   hopefulBoost              = 1.5;

    private List<Position> jellies                   = new LinkedList<Position>(),
                           blockers                  = new LinkedList<Position>();

    private Design         referenceDesign           = null;

    private void fillDifficultyOfFixedPositions(Design design) {
        Cell[][] cellBoard = design.getBoard();
        for (int x = 0; x < cellBoard.length; ++x) {
            for (int y = 0; y < cellBoard[0].length; ++y) {
                Position currentPosition = new Position(x, y);
                if (cellBoard[x][y].getJellyLevel() > 0) {
                    // this.difficultyOfFixedPositions.put(currentPosition,
                    // BoardDifficultyGenerator.getCellDifficulty(design, x, y,
                    // fixedNumberOfRounds));
                    this.jellies.add(currentPosition);
                }
                if (cellBoard[x][y].getCellType().blocksCandies()) {
                    this.blockers.add(currentPosition);
                }
            }
        }
    }

    public static Cell getCell(Cell[][] board, Position pos) {
        return BoardDifficultyGenerator.getCell(board, pos.x, pos.y);
    }

    private double getBlockerCriticality(Cell[][] board, int x, int y) {
        double multiplier;
        if (x == board.length - 1 || x == 0)
            multiplier = blockerAtBoundaryConstant;
        else
            multiplier = 1.0;
        return multiplier * (2.5 - Math.exp(-0.2 * countBlockersHeight(board, x, y)));
    }

    private double getBlockersDifficulty(Cell[][] board) {
        double score = 0.0;
        for (Position blockerPosition : this.blockers) {
            score += getBlockerCriticality(board, blockerPosition.x, blockerPosition.y);
        }
        return score;
    }

    private int countBlockersHeight(Cell[][] board, int x, int y) {
        int count = 0;
        for (int j = y + 1; j < board.length; ++j) {
            if (!board[x][y].getCellType().equals(CellType.UNUSABLE)) {
                ++count;
            }
        }
        return count;
    }

    private double getCandyScore(Cell[][] board) {
        int combinableCandies = BoardDifficultyGenerator.getCombinableCandies(board);
        double combinableCandiesScore;
        switch (combinableCandies) {
        case 0:
            combinableCandiesScore = 3.0;
            break;
        case 1:
            combinableCandiesScore = 1.5;
            break;
        default:
            combinableCandiesScore = 0.0;
        }
        // TODO: add potential to be detonated.
        double candiesScore = 10.0 - getSpecialCandiesScore(board);
        if (candiesScore < 0.0)
            candiesScore = 0.0;
        return combinableCandiesScore + candiesScore;
    }

    private static double targetWeight(int movesRemaining) {
        return 0.5 * Math.exp(-0.2 * movesRemaining);
    }

    private double hopefulCellsScore(Cell[][] cellBoard) {
        return 10.0 - countHopeful(cellBoard) / 2.0;
    }

    MayanScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        double score = 0.0;
        if (!gameState.isGameOver()) {
            Cell[][] board = gameState.getBoard();
            // Accelerates jellies detonation when the number of moves
            // approaches 0 or the number
            // of jellies approaches zero.
            final double targetAlpha = targetWeight(gameState.getGameProgress().movesRemaining);
            System.out.println(gameState.getGameProgress().movesRemaining);
            final double scoreDistance = (gameState.levelDesign.getObjectiveTarget()
                    - gameState.getGameProgress().score) * scoreSmoothing;
            System.out.println(scoreDistance + " " + getCandyScore(board));
            score = (1.0 + targetAlpha) * (scoreDistance) + (1.0 - targetAlpha)
                    * (getBlockersDifficulty(board) + getCandyScore(board) + hopefulBoost * hopefulCellsScore(board));
            System.err.println(score);
        }
        return new ScalarGameMetric(score);
    }
    
    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if(referenceDesign != currentState.levelDesign){
            referenceDesign = currentState.levelDesign;
            fillDifficultyOfFixedPositions(referenceDesign);
            DebugFilter.println("Design was replaced by MayanScorePlayer", DebugFilterKey.SIMULATED_PLAYERS);
        }
        return super.calculateBestMove(currentState);
    };
    
    @Override
    GameStatePotential getGameStatePotential(GameState gameState) {
        //Doesn't use gameStatePotential
        return null;
    };

    @Override
    protected GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        return new ScalarCombinedMetric(metric.metric);
    }

    @Override
    protected List<Move> selectMoves(GameState gameState) {
        List<Move> ret = gameState.getValidMoves();
        Collections.shuffle(ret);
        return ret;
    }

}
