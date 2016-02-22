package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.*;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;


public class BoardDifficultyGenerator {
    
    // TODO: Add a check for whether the blocks are movable.
    public static int getCombinableCandies(Cell[][] cellBoard) {
        int combinableCandies = 0;
        for (int x = 0; x < cellBoard.length; ++x) {
            for (int y = 0; y < cellBoard[0].length; ++y) {
                if (y != cellBoard[0].length - 1 && hasSpecial(cellBoard[x][y]) && hasSpecial(cellBoard[x][y+1]))
                    ++combinableCandies;
                if (x != cellBoard.length - 1 && hasSpecial(cellBoard[x][y]) && hasSpecial(cellBoard[x+1][y]))
                    ++combinableCandies;
            }
        }
        return combinableCandies;
    }
    
    public static double getSpecialCandiesScore(Cell[][] cellBoard) {
        double count = 0;
        for (Cell[] row : cellBoard) {
            for (Cell cell : row) {
                if (hasBomb(cell)) count += 2.5;
                else if (hasSpecial(cell)) count += 1.0;
            }
        }
        return count;
    }
    
    public static int countHopeful(Cell[][] cellBoard) {
        int hopefulCount = 0;
        for (int x = 0; x < cellBoard.length; ++x) {
            for (int y = 0; y < cellBoard[0].length; ++y) {
                if (cellBoard[x][y].blocksCandies()) break;
                if (hasUnmovable(cellBoard[x][y])) ++hopefulCount;
            }
        }
        return hopefulCount;
    }
    
    public static int numOfRounds(GameState game, int x, int y) {
        TargetCellPlayer player = new TargetCellPlayer(x, y);
        int rounds = 0;
        while (!game.isGameOver() && game.getCell(x, y).getJellyLevel() != 0) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            } catch (InvalidMoveException e) {
                e.printStackTrace();
            }
            rounds++;
        }
        return rounds;
    }
    
    public static double getCellDifficulty(Design levelDesign, int x, int y, int numRounds) {
        Design design = new Design(levelDesign);
        design.setGameMode(GameMode.HIGHSCORE);
        design.setObjectiveTarget(2000000);
        design.setNumberOfMovesAvailable(numRounds);
        design.getBoard()[x][y].setJellyLevel(1);
        int difficulty = 0;
        for (int i = 0; i < numRounds; ++i) {
            GameState auxGame = new GameState(design);
            int num = numOfRounds(auxGame, x, y);
            difficulty += num;
        }
        return difficulty / (double) numRounds;
    }
    
    public static double[][] getBoardDifficulty(Design design) {
        double[][] difficultyBoard = new double[design.getWidth()][design.getHeight()];
        for (int i = 0; i < design.getWidth(); ++i) {
            for (int j = 0; j < design.getHeight(); ++j) {
                difficultyBoard[i][j] = getCellDifficulty(design, i, j, 20);
                System.out.print(difficultyBoard[i][j] + " ");
            }
            System.out.println();
        }
        return difficultyBoard;
    }
    
    
    
    public static void main(String[] args) {
        getBoardDifficulty(DepthPotentialScorePlayerTest.getSampleDesign());
        // getBoardDifficulty(DisplayTester.getSampleDesign());
    }
    
    public static boolean inBoard(Cell[][] cells, int x, int y) {
        return x >= 0 && y >= 0 && cells.length > y && cells.length > x;
    }

    public static Cell getCell(Cell[][] cellBoard, int x, int y) {
        if (!inBoard(cellBoard, x, y))
            return null;
        return cellBoard[x][y];
    }

    public static boolean haveSameColor(Cell[][] cellBoard, int x1, int y1, int x2, int y2) {
        Cell cell1 = getCell(cellBoard, x1, y1);
        Cell cell2 = getCell(cellBoard, x2, y2);
        if (cell1 == null || cell2 == null)
            return false;
        return cell1.hasCandy() && cell1.getCandy().getColour() != null && cell2.hasCandy()
                && cell2.getCandy().getColour() != null
                && cell1.getCandy().getColour().equals(cell2.getCandy().getColour())
                && !cell1.getCandy().getCandyType().equals(CandyType.UNMOVEABLE);
    }

    public static boolean haveSameColor(Cell[][] board, int x1, int y1, int x2, int y2, int x3, int y3) {
        return haveSameColor(board, x1, y1, x2, y2) && haveSameColor(board, x1, y1, x3, y3);
    }

    public static boolean hasUnmovable(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.UNMOVEABLE);
    }

    public static boolean canBeRemoved(Cell[][] cells, int x, int y) {
        // Case 1:
        // NC TX NC
        // NX NC NX
        if (haveSameColor(cells, x + 1, y, x - 1, y, x, y + 1) || haveSameColor(cells, x + 1, y, x - 1, y, x, y - 1))
            return true;
        // Case 2:
        // NC NX
        // TX NC
        // NC NX
        if (haveSameColor(cells, x, y + 1, x, y - 1, x + 1, y) || haveSameColor(cells, x, y + 1, x, y - 1, x - 1, y))
            return true;
        
        if (haveSameColor(cells, x, y + 1, x, y + 2, x + 1, y) || haveSameColor(cells, x, y + 1, x, y + 2, x - 1, y))
            return true;
        
        if (haveSameColor(cells, x, y - 1, x, y - 2, x + 1, y) || haveSameColor(cells, x, y - 1, x, y - 2, x - 1, y))
            return true;
        
        if (haveSameColor(cells, x - 1, y, x - 2, y, x, y + 1) || haveSameColor(cells, x - 1, y, x - 2, y, x, y - 1))
            return true;
        
        if (haveSameColor(cells, x + 1, y, x + 2, y, x, y + 1) || haveSameColor(cells, x + 1, y, x + 2, y, x, y - 1))
            return true;
        
        int xStart = x, xEnd = x;
        if (haveSameColor(cells, x, y, x-1, y)) xStart = x - 1;
        if (haveSameColor(cells, x, y, x+1, y)) xEnd = x + 1;
        if (xEnd != xStart && (haveSameColor(cells, x, y, xStart - 1, y+1) || haveSameColor(cells, x, y, xStart - 1, y-1)
                || haveSameColor(cells, x, y, xEnd + 1, y+1) || haveSameColor(cells, x, y, xEnd - 1, y+1)))
                return true;
        
        int yStart = y, yEnd = y;
        if (haveSameColor(cells, x, y, x, y - 1)) yStart = y - 1;
        if (haveSameColor(cells, x, y, x, y + 1)) yEnd = y + 1;
        if (yEnd != yStart && (haveSameColor(cells, x, y, x + 1, yEnd + 1) || haveSameColor(cells, x, y, x - 1, yEnd - 1)
                || haveSameColor(cells, x, y, x + 1, yStart - 1) || haveSameColor(cells, x, y, x - 1, yStart + 1)))
                return true;
        
        return false;
    }
    
    // Will this cell be filled in the next round?
    public static boolean isFillable(Cell[][] board, int x, int y) {
        if (!inBoard(board, x, y)) return false;
        int curY = y;
        while( curY >= 0 ) {
            // There is a cell above it that blocks candies.
            if (board[x][curY].blocksCandies()) return false;
            --curY;
        }
        return true;
    }
    
    public static boolean isHopeful(Cell[][] board, int x, int y) {
        if (!inBoard(board, x, y)) return false; 
        return hasUnmovable(board[x][y]) && isFillable(board, x, y);
    }
    
    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }
    
    public static int getVerticalHopeful(Cell[][] board, int x, int y) {
        return toInt(isHopeful(board, x, y)) + toInt(isHopeful(board, x, y + 1)) + toInt(isHopeful(board, x, y - 1)); 
    }
    
    public static int getHorizontalHopeful(Cell[][] board, int x, int y) {
        return toInt(isHopeful(board, x - 1, y)) + toInt(isHopeful(board, x + 1, y)) + toInt(isHopeful(board, x, y)); 
    }
    
    public static double getProbabilityOfHopefulCells(Cell[][] board, int x, int y, int differentColors) {
        int hHopeful = getHorizontalHopeful(board, x, y);
        int vHopeful = getVerticalHopeful(board, x, y);
        double inv = 1.0 / differentColors;
        if (hHopeful >= 3) {
            if (vHopeful >= 3)
                return inv * inv * inv * (vHopeful + hHopeful - 6 + 2);
            return inv * inv * inv * (hHopeful - 3 + 1);
        } else if (vHopeful >= 3) {
            return inv * inv * inv * (hHopeful - 3 + 1);
        }
        int count = 0;
        if (haveSameColor(board, x - 2, y, x - 1, y)) count += getVerticalHopeful(board, x, y);
        if (haveSameColor(board, x + 2, y, x + 1, y)) count += getVerticalHopeful(board, x, y);
        if (haveSameColor(board, x, y + 1, x, y + 2)) count += getHorizontalHopeful(board, x, y);
        
        if (haveSameColor(board, x - 1, y, x, y)) count += getVerticalHopeful(board, x + 1, y) + getVerticalHopeful(board, x - 2, y);
        if (haveSameColor(board, x + 1, y, x, y)) count += getVerticalHopeful(board, x + 2, y) + getVerticalHopeful(board, x - 1, y);
        if (haveSameColor(board, x, y, x, y + 1)) count += getHorizontalHopeful(board, x, y - 1);
        
        if (haveSameColor(board, x - 2, y, x, y)) count += getVerticalHopeful(board, x - 1, y);
        if (haveSameColor(board, x + 2, y, x, y)) count += getVerticalHopeful(board, x + 1, y);
        double base = (differentColors - 1) / differentColors;
        
        return 1 - Math.pow(base, count);
    }
     
    public static MovesAffectingCell countMovesThatAffectCell(GameState gameState, int x, int y) {
        List<Move> moves = gameState.getValidMoves();
        int countBelow = 0, countAbove = 0;
        for (Move move : moves) {
            List<MatchAnalysis> analyses = gameState.getMatchAnalysis(move);
            for (MatchAnalysis  analysis : analyses) {
                List<Position> affected = analysis.positionsMatched;
                for (Position pos : affected) {
                    if (pos.x >= x - 1 && pos.x <= x + 1) {
                        if (pos.y < y)
                            ++countBelow;
                        else 
                            ++countAbove;
                    }
                }
            }
        }
        return new MovesAffectingCell(countBelow, countAbove);
    }

    public static double getMovesDelta(MovesAffectingCell movesDelta) {
        if (movesDelta.below == 0) return 2.0;
        if (movesDelta.below >= 3) return 0.0;
        if (movesDelta.below == 2) return 1.0;
        /* if (movesDelta == 1) */
        return 0.5;
    }
    
    // Function that returns estimate between 0 and 3.0
    public static double motionPotential(Cell[][] board, int x, int y) {
        int cellsToBeFilled = 0;
        int cellsWithSpecials = 0;

        final int smoothingFactorSpecials = 10;
        final int smoothingFactorFillable = 5;
        for (int i = x - 1; i <= x + 1; ++i) {
            for (int j = y - 1; j >= 0; --j) {
                if (inBoard(board, i, j) && GameStateAuxiliaryFunctions.hasSpecial(board[i][j])) {
                    ++cellsWithSpecials;
                }
                else if (isHopeful(board, i, j)) {
                    ++cellsToBeFilled;
                }
            }
        }
        // System.out.println(cellsToBeFilled);
        double contributorCellsWithSpecial = cellsWithSpecials / ((double) smoothingFactorSpecials + Math.pow(cellsWithSpecials - 1, 3));
        double contributorCellsToBeFilled = smoothingFactorFillable / (cellsToBeFilled + (double) smoothingFactorFillable);
        // System.out.println(contributorCellsToBeFilled + " " + cellsToBeFilled);
        return contributorCellsWithSpecial + 2.0 * contributorCellsToBeFilled;
    }
        
}
