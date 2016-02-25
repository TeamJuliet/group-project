package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;

import java.util.List;

public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

    public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    public GameStateMetric getGameStateMetric(GameState gameState) {
        return new GameStateMetric(getJellyNumber(gameState));
    }

    @Override
    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        Move move = super.calculateBestMove(currentState);
        GameState copy = new GameState(currentState);
        try {
            copy.makeFullMove(move);
        } catch (InvalidMoveException e) {
            move = currentState.getValidMoves().get(0);
        }
        if (getGameStateMetric(copy) == getGameStateMetric(currentState)) {
            List<Move> moves = currentState.getValidMoves();
            double mostSpecials = 0;
            for (Move m : moves) {
                double specials = numOfSpecialsOnMove(currentState, m);
                if (specials > mostSpecials) {
                    mostSpecials = specials;
                    move = m;
                }
            }
        }
        return move;
    }
}
