package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmoveableCandyGenerator;


//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialPlayerAlpha extends DepthPotentialPlayer {
    
    public DepthPotentialPlayerAlpha(GameState level, int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
        this.level = level;
    }
    
    @Override
    public void solve() throws NoMovesFoundException {
        while (level.getGameProgress().movesRemaining > 0) {
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
        System.err.println("WARNING! DepthPotentialPlayerAlpha has suggested an invalidMove " + move + ".");
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.getGameProgress().score);
    }

    @Override
    GameStatePotential getGameStatePotential(GameState gameState) {
        //Return the highest increase in score of all possible matches
        GameState original = new GameState(gameState, new UnmoveableCandyGenerator());
        int highestIncrease = 0;
        List<Move> moves = original.getValidMoves();
        for(Move move: moves){
            GameState tmp = original;
            try{
                tmp.makeMove(move);
            } catch (InvalidMoveException e){
                //Just eat the exception since we don't care if wrong move is suggested
                System.err.println("Invalid move suggested in DepthPotentialPlayerAlpha evaluation");
                continue;
            }
            
            int increase = tmp.getGameProgress().score - original.getGameProgress().score;
            if(increase > highestIncrease) highestIncrease = increase;
        }
        return new GameStatePotential(highestIncrease);
    }

    @Override
    GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential) {
        //Value metric and potential equally for now and find arithmetic mean
        return new GameStateCombinedMetric(metric, potential, (metric.score + potential.potential)/2);
    }

    @Override
    List<Move> selectMoves(GameState gameState) {
        //TODO: look more into filtering moves
        return gameState.getValidMoves();
    }

}
