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
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class IngredientDepthPotentialPlayer extends DepthPotentialPlayer {
    private final double   blockerAtBoundaryConstant = 0.5;
    private final int      numOfRoundsToExecute      = 10;
    private final double   ingredientsBonusRemoval   = 5.0;
    private final double   hopefulBoost              = 1.5;
    private final double   ingredientPercentageBoost = 10.0;

    private List<Position> blockers                  = new LinkedList<Position>();

    private Design         referenceDesign           = null;

    private double[][]     difficultyBoard;

    private void recordBlockers(Design design) {
        Cell[][] cellBoard = design.getBoard();
        this.blockers.clear();
        for (int x = 0; x < cellBoard.length; ++x) {
            for (int y = 0; y < cellBoard[0].length; ++y) {
                if (cellBoard[x][y].getCellType().blocksCandies()) {
                    this.blockers.add(new Position(x, y));
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

    private double getIngredientsDifficulty(Cell[][] board) {
        double score = 0.0;
        for (int x = 0; x < board.length; ++x) {
            double difficultyAccumulated = 0.0;
            double prevDifficulty = 0.0;
            double prevPrevDifficulty = 0.0;
            double colDifficulty = 0.0;
            double colTotalDifficulty = 0.0;
            for (int y = board[0].length - 1; y >= 0; --y) {
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y])) {
                    colDifficulty += colTotalDifficulty;
                    /*
                     * score += prevPrevDifficulty * prevPrevIngredientBoost +
                     * prevDifficulty * prevIngredientBoost + 0.5 *
                     * this.difficultyBoard[x][y];
                     */
                }
                colTotalDifficulty += this.difficultyBoard[x][y];
                // prevPrevDifficulty = prevDifficulty;
                // prevDifficulty = this.difficultyBoard[x][y];
            }
            if (colTotalDifficulty == 0.0)
                continue;
            // Add the progress for all ingredients in that column.
            score += colDifficulty / colTotalDifficulty * ingredientPercentageBoost;
        }
        return score;
    }

    private double getIngredientsPotential(Cell[][] board) {
        double score = 0.0;
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[0].length; ++y) {
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y])) {
                    score += BoardDifficultyGenerator.getMotionPotential(board, x, y);
                }
            }
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
        return 0.9 * Math.exp(-0.2 * movesRemaining);
    }

    private double hopefulCellsScore(Cell[][] cellBoard) {
        return 10.0 - countHopeful(cellBoard) / 2.0;
    }

    public IngredientDepthPotentialPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        double score = 0.0;
        if (!gameState.isGameWon()) {
            Cell[][] board = gameState.getBoard();
            // Accelerates jellies detonation when the number of moves
            // approaches 0 or the number of jellies ingredients is close to
            // zero.
            final double targetAlpha = Math.max(targetWeight(gameState.getGameProgress().movesRemaining),
                    targetWeight(gameState.getGameProgress().ingredientsRemaining));
            final double remainingIngredients = ingredientsBonusRemoval
                    * gameState.getGameProgress().ingredientsRemaining;
            System.out.println(remainingIngredients);
            System.out.println(targetAlpha);
            score = (1.0 + targetAlpha) * (getIngredientsDifficulty(board) + remainingIngredients)
                    + (1.0 - targetAlpha) * (getBlockersDifficulty(board) + getCandyScore(board)
                            + hopefulBoost * hopefulCellsScore(board) + getIngredientsPotential(board));

            System.out.println(score);
        }
        return new ScalarGameMetric(score);
    }

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if (referenceDesign != currentState.levelDesign) {
            referenceDesign = currentState.levelDesign;
            recordBlockers(referenceDesign);
            this.difficultyBoard = BoardDifficultyGenerator.getBoardDifficulty(referenceDesign, numOfRoundsToExecute);
        }
        return super.calculateBestMove(currentState);
    }

    @Override
    public GameStatePotential getGameStatePotential(GameState gameState) {
        // Doesn't use gameStatePotential
        return null;
    }

    @Override
    public GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        return new ScalarCombinedMetric(metric.metric);
    }

    @Override
    protected List<Move> selectMoves(GameState gameState) {
        List<Move> ret = gameState.getValidMoves();
        Collections.shuffle(ret);
        return ret;
    }
}
