package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.MatchAnalysis;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

/**
 * 
 * Class that contains the functions used by the rule based players.
 *
 */
public class RuleBasedAuxiliaryFunctions {

    /**
     * Function that finds the number of ingredients above each position.
     * 
     * @param board
     *            The board to be searched.
     * @return The precomputed board.
     */
    public static int[][] precomputeIngredientsCount(Cell[][] board) {
        int[][] ingredientsCount = new int[board.length][board[0].length];
        for (int x = 0; x < board.length; ++x) {
            int ingredientsInColumn = 0;
            for (int y = 0; y < board[0].length; ++y) {
                ingredientsCount[x][y] = ingredientsInColumn;
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y]))
                    ++ingredientsInColumn;
            }
        }
        return ingredientsCount;
    }

    /**
     * Function that finds the number of ingredients below each position.
     * 
     * @param board
     *            The board to be searched.
     * @return The precomputed board.
     */
    public static int[][] precomputeInverseIngredientsCount(Cell[][] board) {
        int[][] ingredientsCount = new int[board.length][board[0].length];
        for (int x = 0; x < board.length; ++x) {
            int ingredientsInColumn = 0;
            for (int y = board[0].length - 1; y >= 0; --y) {
                ingredientsCount[x][y] = ingredientsInColumn;
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y]))
                    ++ingredientsInColumn;
            }
        }
        return ingredientsCount;
    }

    /**
     * Sums the ingredient score from the precomputed table. The pre-computed
     * table amortizes complexity to be linear.
     * 
     * @param analyses
     *            The analyses for the move.
     * @param ingredientsCount
     *            The precomputed table for the ingredients to be scored.
     * @return the sum of the individual scores.
     */
    public static int getIngredientsScore(List<MatchAnalysis> analyses, int[][] ingredientsCount) {
        int count = 0;
        for (MatchAnalysis analysis : analyses) {
            for (Position position : analysis.positionsMatched) {
                count += ingredientsCount[position.x][position.y];
            }
        }
        return count;
    }

    /**
     * Comparison made for two moves for the ingredients games. Tells if 2 is
     * better than 1.
     * 
     * @param analyses1
     *            The analysis for the first move. If null then 2 will always be
     *            better.
     * @param analyses2
     *            The analysis for the second move.
     * @return whether the first move was better for ingredients than the second
     *         move.
     */
    public static boolean isIngredientsBetter(List<MatchAnalysis> analyses1, List<MatchAnalysis> analyses2,
            int[][] ingredientsCount, int[][] inverseIngredientsCount) {
        if (analyses1 == null)
            return true;

        int ingredientsScore1 = getIngredientsScore(analyses1, ingredientsCount);
        int ingredientsScore2 = getIngredientsScore(analyses2, ingredientsCount);
        if (ingredientsScore1 == ingredientsScore2) {

            int blockersCleared1 = getBlockersCleared(analyses1);
            int blockersCleared2 = getBlockersCleared(analyses2);

            if (blockersCleared1 == blockersCleared2) {
                int scoreAnalysis1 = getAnalysesScore(analyses1);
                int scoreAnalysis2 = getAnalysesScore(analyses2);

                if (scoreAnalysis1 == scoreAnalysis2) {
                    return getIngredientsScore(analyses1, inverseIngredientsCount) < getIngredientsScore(analyses2,
                            inverseIngredientsCount);
                } else {
                    return scoreAnalysis1 < scoreAnalysis2;
                }
            } else {
                return blockersCleared1 < blockersCleared2;
            }

        } else {
            return ingredientsScore1 < ingredientsScore2;
        }
    }

    /**
     * Comparison made for two moves for the jelly games. Tells if 2 is better
     * than 1.
     * 
     * @param analyses1
     *            The analysis for the first move. If null then 2 will always be
     *            better.
     * @param analyses2
     *            The analysis for the second move.
     * @return whether the first move was better for jellies than the second
     *         move.
     */
    public static boolean isJellyBetter(List<MatchAnalysis> analyses1, List<MatchAnalysis> analyses2) {
        if (analyses1 == null)
            return true;

        int jelliesCleared1 = getJelliesCleared(analyses1);
        int jelliesCleared2 = getJelliesCleared(analyses2);
        if (jelliesCleared1 == jelliesCleared2) {

            int blockersCleared1 = getBlockersCleared(analyses1);
            int blockersCleared2 = getBlockersCleared(analyses2);

            if (blockersCleared1 == blockersCleared2) {
                return getAnalysesScore(analyses1) < getAnalysesScore(analyses2);
            } else {
                return blockersCleared1 < blockersCleared2;
            }

        } else {
            return jelliesCleared1 < jelliesCleared2;
        }
    }

    // Function that counts the jellies cleared by a move.
    private static int getJelliesCleared(List<MatchAnalysis> analyses) {
        int jelliesRemoved = 0;
        for (MatchAnalysis analysis : analyses) {
            jelliesRemoved += analysis.jelliesRemoved;
        }
        return jelliesRemoved;
    }

    // Function that gets the blocks cleared by a move.
    private static int getBlockersCleared(List<MatchAnalysis> analyses) {
        int blockersRemoved = 0;
        for (MatchAnalysis analysis : analyses) {
            blockersRemoved += analysis.blockersRemoved;
        }
        return blockersRemoved;
    }

    // Function that gives a score to the immediate candies removed.
    private static int getAnalysesScore(List<MatchAnalysis> analyses) {
        int score = 0;
        for (MatchAnalysis analysis : analyses) {
            score += analysis.containsSpecials.size();
            if (analysis.formsSpecial) {
                score += 5;
                if (analysis.formedSpecialType.equals(CandyType.BOMB))
                    score += 2;
            }
        }
        return score;
    }
}
