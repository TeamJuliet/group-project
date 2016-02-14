package uk.ac.cam.cl.intelligentgamedesigner.testing;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.LevelManager;

import static org.junit.Assert.assertTrue;

public class SimulatedPlayersTestRunner {
    @Test
    public void exampleTest() {
        LevelManager manager = new LevelManager();
        GameState testLvl = new GameState(manager.getLevel(1));
        Move suggestedMove;
        try {
            suggestedMove = SimulatedPlayerManager.calculateBestMove(testLvl,1);
        } catch (NoMovesFoundException e) {
            suggestedMove = null;
        }
        boolean checkIfValid = testLvl.getValidMoves().contains(suggestedMove);
        assertTrue(checkIfValid);
    }
}
