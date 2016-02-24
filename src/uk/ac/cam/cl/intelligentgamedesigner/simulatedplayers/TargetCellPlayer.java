package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.canBeRemoved;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countMovesThatAffectCell;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMotionPotential;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMovesDelta;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getProbabilityOfHopefulCells;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

/**
 * 
 * Player that is designed using the principle of the depth potential player and
 * focuses to clear the jelly from the cell located at the position that this
 * player had been initialized for.
 *
 */
public class TargetCellPlayer extends DepthPotentialPlayer {
    // The coordinates of the cell that has to be removed.
    private final int           x, y;

    private static final double ZERO_DISTANCE = 0.0,
                                        BASE_DISTANCE = 0.1;

    /**
     * Construct the player by providing the target position that the player
     * should aim to clear.
     * 
     * @param x
     *            The x coordinate for the cell to clear.
     * @param y
     *            The y coordinate for the cell to clear.
     */
    public TargetCellPlayer(int numOfStatesAhead, int numOfStatesInPool, int x, int y) {
        super(numOfStatesAhead, numOfStatesInPool);
        this.x = x;
        this.y = y;
    }

    /**
     * The metric for a state is used to distinguish between the game states
     * where the specific position has been cleared and the rest.
     */
    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        ScalarGameMetric metric;

        // If the target has been reached then the distance should be 0.
        if (gameState.getCell(x, y).getJellyLevel() == 0) {
            metric = new ScalarGameMetric(ZERO_DISTANCE);
        }
        // Otherwise a standard base distance value is inserted to distinguish
        // from
        // ZERO_DISTANCE.
        else {
            metric = new ScalarGameMetric(BASE_DISTANCE);
        }

        return metric;
    }

    /**
     * The potential metric gives a zero distance for the states in which the
     * target can be removed in the next move. For the other cases, it takes
     * into account the probability that there will be a match formed containing
     * the target cell or a score about how much the board around the taregt
     * cell is changing.
     */
    @Override
    public GameStatePotential getGameStatePotential(GameState gameState) {
        ScalarGamePotential metric;

        // If the target has been reached or the target can be reached in the
        // next move then the distance should be 0.
        if (gameState.getCell(x, y).getJellyLevel() == 0 || canBeRemoved(gameState.getBoard(), x, y)) {
            metric = new ScalarGamePotential(ZERO_DISTANCE);
        }
        // Otherwise we get a distance that is greater than the base distance.
        else {
            // The factor that there are moves that affect the target cell
            // position (i.e. they are in a region bellow the target cell)
            double movesDelta = getMovesDelta(countMovesThatAffectCell(gameState, x, y));

            // Find the probability that there will be a match formed using the
            // knowledge about the candies that are going to be filled.
            double replacementPotential = 1.0 - getProbabilityOfHopefulCells(gameState.getBoard(), x, y,
                    gameState.levelDesign.getNumberOfCandyColours());

            // The potential that the region above or below is likely to change
            // it the next few moves.
            double motionPotential = getMotionPotential(gameState.getBoard(), x, y);

            metric = new ScalarGamePotential(movesDelta + replacementPotential + motionPotential);
        }

        return metric;
    }

    /**
     * The combined metric for this player is given as the sum of the metric and
     * the potential.
     */
    @Override
    public GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        return new ScalarCombinedMetric(
                ((ScalarGameMetric) metric).score + ((ScalarGamePotential) potential).score);
    }

}
