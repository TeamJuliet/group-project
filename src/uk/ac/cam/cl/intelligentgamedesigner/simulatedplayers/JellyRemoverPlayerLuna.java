package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.canBeRemoved;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countHopeful;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getProbabilityOfHopefulCells;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getSpecialCandiesScore;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMotionPotential;

import java.util.Collections;
import java.util.HashMap;
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

public class JellyRemoverPlayerLuna extends DepthPotentialPlayer {

    private final int                 fixedNumberOfRounds        = 20;
    private final double              isRemovableConstant        = 0.5;
    private final double              alphaLikelihood            = 0.8;
    private final double              blockerAtBoundaryConstant  = 0.5;

    private HashMap<Position, Double> difficultyOfFixedPositions = new HashMap<Position, Double>();
    private List<Position>            jellies                    = new LinkedList<Position>(),
                                      blockers                   = new LinkedList<Position>();
    private Design                    levelDesign                = null;

    private void fillDifficultyOfFixedPositions(Design design) {
        this.levelDesign = design;
        Cell[][] cellBoard = design.getBoard();
        for (int x = 0; x < cellBoard.length; ++x) {
            for (int y = 0; y < cellBoard[0].length; ++y) {
                Position currentPosition = new Position(x, y);
                if (cellBoard[x][y].getJellyLevel() > 0) {
                    this.difficultyOfFixedPositions.put(currentPosition,
                            BoardDifficultyGenerator.getCellDifficulty(design, x, y, fixedNumberOfRounds));
                    this.jellies.add(currentPosition);
                }
                if (cellBoard[x][y].getCellType().blocksCandies()) {
                    this.blockers.add(currentPosition);
                }
            }
        }
    }

    public JellyRemoverPlayerLuna(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }
    
    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if(this.levelDesign != currentState.levelDesign){
            this.levelDesign = currentState.levelDesign;
            DebugFilter.println("Design was replaced by LunaJellyRemoverPlayer", DebugFilterKey.SIMULATED_PLAYERS);
            fillDifficultyOfFixedPositions(this.levelDesign);
            DebugFilter.println("Filtering has ended", DebugFilterKey.SIMULATED_PLAYERS);
        }
        return super.calculateBestMove(currentState);
    }

    public static Cell getCell(Cell[][] board, Position pos) {
        return BoardDifficultyGenerator.getCell(board, pos.x, pos.y);
    }

    private double getJelliesDifficulty(Cell[][] board) {
        double jellyScore = 0.0;
        for (Position jellyPosition : this.jellies) {
            Cell cell = getCell(board, jellyPosition);
            int x = jellyPosition.x, y = jellyPosition.y;
            if (cell.getJellyLevel() > 0) {
                double positionDifficulty = this.difficultyOfFixedPositions.get(jellyPosition);
                double multiplier;
                if (canBeRemoved(board, x, y)) {
                    multiplier = isRemovableConstant;
                } else {
                    double likelihood = getProbabilityOfHopefulCells(board, x, y,
                            this.levelDesign.getNumberOfCandyColours());
                    double potential = getMotionPotential(board, x, y);
                    multiplier = 1.0 - ((1.0 - alphaLikelihood) * potential / 3.0 + alphaLikelihood * likelihood) / 4.0;
                }
                // System.err.println("jelly multiplier is " + multiplier);
                // System.err.println("jelly score is : " + cell.getJellyLevel() * multiplier * positionDifficulty);
                jellyScore += cell.getJellyLevel() * multiplier * positionDifficulty;
            }
        }
        return jellyScore;
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
            combinableCandiesScore = 30.0;
            break;
        case 1:
            combinableCandiesScore = 15.0;
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
        return Math.exp(-0.2 * movesRemaining);
    }

    private double hopefulCellsScore(Cell[][] cellBoard) {
        return 10.0 - countHopeful(cellBoard) / 2.0;
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        double score = 0.0;
        if (!gameState.isGameWon()) {
            Cell[][] board = gameState.getBoard();
            // Accelerates jellies detonation when the number of moves
            // approaches 0 or the number
            // of jellies approaches zero.
            final double targetAlpha = Math.max(targetWeight(gameState.getGameProgress().movesRemaining),
                    targetWeight(gameState.getGameProgress().jelliesRemaining));
            // System.out.println(getJelliesDifficulty(board));
            // System.out.println(getBlockersDifficulty(board));
            score = (2.0 + targetAlpha) * getJelliesDifficulty(board) + (1.0 - targetAlpha)
                    * (getBlockersDifficulty(board) + 0.5 * getCandyScore(board) + 0.5 * hopefulCellsScore(board));
            // System.err.println(score);
        }
        return new ScalarGameMetric(score);
    }
    
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
