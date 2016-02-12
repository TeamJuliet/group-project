package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmoveableCandyGenerator;


//Simple Evaluation: Metric = current score, Potential = best score after one move
public class DepthPotentialScorePlayer extends DepthPotentialPlayer {
    
    public DepthPotentialScorePlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    private void printInvalidMoveError(Move move) {
        System.err.format("WARNING! DepthPotentialScorePlayer with settings (%d,%d)has suggested an invalidMove " + move + ".", numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(gameState.getScore());
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
            
            int increase = tmp.getScore() - original.getScore();
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
