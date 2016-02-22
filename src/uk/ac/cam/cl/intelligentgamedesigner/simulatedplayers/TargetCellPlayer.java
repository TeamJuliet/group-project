package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.canBeRemoved;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.countMovesThatAffectCell;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getMovesDelta;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.getProbabilityOfHopefulCells;
import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.BoardDifficultyGenerator.motionPotential;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class TargetCellPlayer extends DepthPotentialPlayer {
    private int x, y;

    TargetCellPlayer(int x, int y) {
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
            metric = new TargetGameMetric(movesDelta + 0.1 + 1.0 - 
                    getProbabilityOfHopefulCells(gameState.getBoard(), x, y, gameState.getLevelDesign().getNumberOfCandyColours())
                    + motionPotential(gameState.getBoard(), x, y)
            );
        }
        
        return (GameStateMetric) metric;
    }

    @Override
    GameStatePotential getGameStatePotential(GameState gameState) {
        // There is no potential function used for this player.
        return null;
    }

    @Override
    GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        // Just consider using the targetGameMetric since a potential metric is not used.
        TargetGameMetric targetMetric = (TargetGameMetric) metric;
        return new TargetCombinedGameMetric(targetMetric);
    }

    @Override
    List<Move> selectMoves(GameState gameState) {
        return gameState.getValidMoves();
    }

}
