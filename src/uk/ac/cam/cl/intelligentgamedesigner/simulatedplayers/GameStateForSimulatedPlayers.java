package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class GameStateForSimulatedPlayers extends GameState {
    public GameStateForSimulatedPlayers(Design design){
        super(design);
    }
    
    public void swapCandies(Move move){
        super.swapCandies(move);
    }
}
