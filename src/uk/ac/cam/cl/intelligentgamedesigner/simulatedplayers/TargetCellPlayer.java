package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.canBeRemoved;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countMovesThatAffectCell;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMovesDelta;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getProbabilityOfHopefulCells;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.motionPotential;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

/**
 * 
 * Player that is designed using the principle of the depth potential player and
 * focuses to clear the jelly from the cell located at the position that this
 * player had been initialized for.
 *
 */
public class TargetCellPlayer extends DepthPotentialPlayer {
    private int x, y;

    /**
     * Construct the player by providing the target position that the player
     * should aim to clear.
     * 
     * @param x The x coordinate for the cell to clear.
     * @param y The y coordinate for the cell to clear.
     */
    public TargetCellPlayer(int x, int y) {
        super(2, 10);
        this.x = x;
        this.y = y;
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        TargetGameMetric metric;

        if (gameState.getCell(x, y).getJellyLevel() == 0) {
            metric = new TargetGameMetric(0.0);
        } else if (canBeRemoved(gameState.getBoard(), x, y)) {
            metric = new TargetGameMetric(0.1);
        } else {
            MovesAffectingCell affectingMoves = countMovesThatAffectCell(gameState, x, y);
            double movesDelta = getMovesDelta(affectingMoves);
            metric = new TargetGameMetric(movesDelta + 0.1 + 1.0
                    - getProbabilityOfHopefulCells(gameState.getBoard(), x, y,
                            gameState.getLevelDesign().getNumberOfCandyColours())
                    + motionPotential(gameState.getBoard(), x, y));
        }

        return (GameStateMetric) metric;
    }

    @Override
    GameStatePotential getGameStatePotential(GameState gameState) {
        // There is no potential function used for this player.
        return null;
    }

    @Override
    protected GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        // Just consider using the targetGameMetric since a potential metric is
        // not used.
        TargetGameMetric targetMetric = (TargetGameMetric) metric;
        return new TargetCombinedGameMetric(targetMetric);
    }

    @Override
    protected List<Move> selectMoves(GameState gameState) {
        return gameState.getValidMoves();
    }

}
