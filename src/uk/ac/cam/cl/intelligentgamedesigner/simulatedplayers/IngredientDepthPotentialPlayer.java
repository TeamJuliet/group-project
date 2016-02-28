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

/**
 * 
 * Player that attempts to solve ingredient levels. It takes into account the
 * potential of the ingredients to be removed.
 * 
 * Note: it assumes that every column has a sink.
 *
 */
public class IngredientDepthPotentialPlayer extends DepthPotentialPlayer {
    // The multiplier value for how much less should we care about clearing a
    // blocker at the boundary.
    private static final double   BLOCKER_AT_BOUNDARY = 0.5;

    // Rounds to execute in the estimation of the difficulty for the board.
    private static final int      NUMBER_OF_ROUNDS      = 10;

    // The bonus given for removing a single ingredient.
    private static final double   INGREDIENTS_BONUS_REMOVAL   = 5.0;

    // Constant that is used to promote candy combinations or moves that remove
    // a lot of cells (but are possible to refill them).
    private static final double   HOPEFUL_BOOST              = 1.5;

    private static final double   INGREDIENT_PERCENTAGE_BOOST = 10.0;

	// Score given by each special candy on the board.
	private static final double SPECIAL_CANDY_SCORE = 1.2;

	// Score given by each colour bomb on the board.
	private static final double COLOUR_BOMB_SCORE = 2.5;
	
    // The positions of the initial blockers on the board.
    private List<Position> blockers                  = new LinkedList<Position>();

    // The design that was used to generate the current game state.
    private Design         referenceDesign           = null;

    // Keep the difficulty for removing all cells in an array, corresponding to
    // the reference design.
    private double[][]     difficultyBoard;
    
    public IngredientDepthPotentialPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        if (referenceDesign != currentState.levelDesign) {
            referenceDesign = currentState.levelDesign;
            this.blockers = GameStateAuxiliaryFunctions.getBlockerPositions(referenceDesign.getBoard());
            this.difficultyBoard = BoardDifficultyGenerator.getBoardDifficulty(referenceDesign, NUMBER_OF_ROUNDS);
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
    	return new ScalarCombinedMetric(((ScalarGameMetric) metric).score);
    }

    @Override
    protected List<Move> selectMoves(GameState gameState) {
        List<Move> ret = gameState.getValidMoves();
        Collections.shuffle(ret);
        return ret;
    }
    
    // Function that gets a difficulty score for a single blocker on the board
    // at location (x, y).
    private double getBlockerCriticality(Cell[][] board, int x, int y) {
        double multiplier = 1.0;

        // If the blocker is at the board then it is not as important, hence set
        // the multiplier to be a fraction smaller than 1.0.
        if (x == board.length - 1 || x == 0)
            multiplier = BLOCKER_AT_BOUNDARY;

        return multiplier * (2.5 - Math.exp(-0.2 * countBlockersHeight(board, x, y)));
    }

    // Function that returns the difficulty for all the blockers on the board.
    private double getBlockersDifficulty(Cell[][] board) {
        double blockerScore = 0.0;
        for (Position blockerPosition : this.blockers) {
            // Add the estimation for the single cell only if it is still a
            // blocked candy.
            if (board[blockerPosition.x][blockerPosition.y].blocksCandies())
                blockerScore += getBlockerCriticality(board, blockerPosition.x, blockerPosition.y);
        }
        return blockerScore;
    }

    // Function that evaluates the score for the ingredients on the board.
    private double getIngredientsDifficulty(Cell[][] board) {
        // The score for the ingredients difficulty.
        double score = 0.0;

        for (int x = 0; x < board.length; ++x) {

            // The difficulty for all ingredients in the column.
            // This will be used to take sum of the average completion for the
            // ingredients in this column.
            double colIngredientsDifficulty = 0.0;

            // The total difficulty of clearing all the cells up to that point.
            double colTotalDifficulty = 0.0;

            for (int y = board[0].length - 1; y >= 0; --y) {

                // If there is an ingredient at this position then add the
                // difficulty for all the cells below (and this will be averaged
                // at the end).
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y])) {
                    colIngredientsDifficulty += colTotalDifficulty;
                }

                // Add the current cell difficulty to the column total.
                colTotalDifficulty += this.difficultyBoard[x][y];
            }

            // If the total difficulty of the column was 0.0 (which should be
            // impossible, but it is added for safety of wrong generation for
            // the board difficulty), then skip the average calculation.
            if (colTotalDifficulty == 0.0)
                continue;

            // Add the progress for all ingredients in that column.
            // If the difficulty remaining for the ingredients on the column
            // were Di and the total difficulty is D we are taking the Sum(Di/D)
            // which is Sum(Di) / D.
            score += colIngredientsDifficulty / colTotalDifficulty * INGREDIENT_PERCENTAGE_BOOST;
        }
        return score;
    }

    private double getIngredientsPotential(Cell[][] board) {
        double score = 0.0;
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[0].length; ++y) {
                if (GameStateAuxiliaryFunctions.hasIngredient(board[x][y])) {
                    score += BoardDifficultyGenerator.getMotionPotential(board, x, y) / 3.0;
                }
            }
        }
        return score;
    }

    // Function that returns how many cells is the blocker actually affecting.
    private int countBlockersHeight(Cell[][] board, int x, int y) {
        // The blocker should include itself.
        int count = 1;

        for (int j = y + 1; j < board.length; ++j) {
            // Only count the cells that are valid inside the board layout.
            if (!board[x][y].getCellType().equals(CellType.UNUSABLE))
                ++count;
        }

        return count;
    }

    // Function that estimates the importance of special candies in the board.
    private double getSpecialCandyScore(Cell[][] board) {
        int combinableCandies = BoardDifficultyGenerator.getCombinableCandies(board);
        // Simple evaluation for the combinable candies giving a signigicant
        // boost.
        double combinableCandiesScore;
        switch (combinableCandies) {
        case 0:
            combinableCandiesScore = 5.0;
            break;
        case 1:
            combinableCandiesScore = 3.5;
            break;
        default:
            combinableCandiesScore = 0.0;
        }

        double candiesScore = 10.0 - getSpecialCandiesScore(board,
				COLOUR_BOMB_SCORE, SPECIAL_CANDY_SCORE);
        if (candiesScore < 0.0)
            candiesScore = 0.0;
        return combinableCandiesScore + candiesScore;
    }

    // Function that returns how much should reaching the end of the game boost
    // the player making moves for reaching the target directly.
    private static double targetWeight(int movesRemaining) {
        return 0.9 * Math.exp(-0.2 * movesRemaining);
    }

    // Returns the score for hopeful cells.
    private double hopefulCellsScore(Cell[][] cellBoard) {
        return 20.0 - countHopeful(cellBoard) / 2.0;
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

            // Term that promotes the removal of ingredients.
            final double remainingIngredients = INGREDIENTS_BONUS_REMOVAL
                    * gameState.getGameProgress().ingredientsRemaining;

            score = (1.0 + targetAlpha) * (getIngredientsDifficulty(board) + remainingIngredients)
                    + (1.0 - targetAlpha) * (getBlockersDifficulty(board) + getSpecialCandyScore(board)
                            + HOPEFUL_BOOST * hopefulCellsScore(board) + getIngredientsPotential(board));
        }
        return new ScalarGameMetric(score);
    }


}
