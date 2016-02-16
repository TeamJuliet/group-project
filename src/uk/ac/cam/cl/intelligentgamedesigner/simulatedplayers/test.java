package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.LevelManager;

public class test {

    public static void main(String[] args) {
        LevelManager manager = new LevelManager();
        GameState testLvl = new GameState(manager.getLevel(1));
        Move suggestedMove;
        try {
            suggestedMove = SimulatedPlayerManager.calculateBestMove(testLvl, 2);
        } catch (NoMovesFoundException e) {
            suggestedMove = null;
        }
        System.out.println(suggestedMove);
    }

}
