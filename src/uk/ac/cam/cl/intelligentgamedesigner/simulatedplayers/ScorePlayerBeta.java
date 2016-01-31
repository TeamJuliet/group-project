package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class ScorePlayerBeta implements SimulatedPlayerBase {
    GameState level;

    @Override
    public void solve() {
        /* Look through all available moves
         * make each move
         * compare resulting scores
         * pick the move with highest resulting score
         */
        while(level.getMovesRemaining() > 0){
            
        }
    }

    @Override
    public void printInvalidMoveError(Move move) {
        System.err.println("WARNING! ScorePlayerBeta has suggested an invalidMove " + move + ".");
    }

}
