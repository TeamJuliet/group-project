package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

public class SimulatedPlayersHelpers {
    public static GameState simulateNextMove(GameState gameState, Move move) throws InvalidMoveException {
        GameState nextState = new GameState(gameState, new UnmovableCandyGenerator());
        nextState.makeFullMove(move);
        return nextState;
    }
}
