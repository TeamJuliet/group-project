package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmoveableCandyGenerator;

public class SimGS extends GameState {

    public SimGS(GameState level){
        super(level, new UnmoveableCandyGenerator());
    }
    
    public void swapCandies(Move move){
        super.swapCandies(move);
    }
}
