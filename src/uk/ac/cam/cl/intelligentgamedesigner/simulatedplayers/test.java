package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.userinterface.LevelManager;

public class test {

    public static void main(String[] args) {
        LevelManager manager = new LevelManager();
        GameState testLvl = new GameState(manager.getLevel(1));
        Move suggestedMove = null;
        int i = 0;
        while(i < 10){
            try {
                suggestedMove = SimulatedPlayerManager.calculateBestMove(testLvl, 3);
            } catch (NoMovesFoundException e) {
                suggestedMove = null;
            }
            System.out.println(i);
            System.out.println(testLvl.getValidMoves());
            System.out.println(suggestedMove);
            System.out.println(testLvl);
            i++;
            try{
                testLvl.makeFullMove(suggestedMove);
            } catch (InvalidMoveException e){
                e.printStackTrace();
            }
            System.out.println("\n-----\n");
        } 
    }

}
