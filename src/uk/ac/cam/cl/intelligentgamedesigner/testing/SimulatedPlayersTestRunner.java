package uk.ac.cam.cl.intelligentgamedesigner.testing;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScorePlayerAlpha;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.LevelManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatedPlayersTestRunner {

    /*
    INSERT UNIT TESTS FOR THE SIMULATED PLAYERS BELOW BY DOING THE FOLLOWING:
    ----------------------------------------------------------------------

    1. Write the test in a public void method.
    2. Annotate the method with @Test

    I've added an example below.
     */

    @Test
    public void exampleTest () {
        //TODO: make this work
        LevelManager manager = new LevelManager();
        GameState testLvl = new GameState(manager.getLevel(1));
        Move suggestedMove = null;
        try{
            ScorePlayerAlpha.calculateBestMove(testLvl);
        } catch (NoMovesFoundException e){
            return;
        }
        boolean checkIfValid = testLvl.getValidMoves().contains(suggestedMove);
        assertTrue(checkIfValid);
    }

    public int one () {
        return 1;
    }
}
