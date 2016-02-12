package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.*;

public class SimulatedPlayerManager {
    public static Move calculateBestMove(GameState level, int ability) throws NoMovesFoundException{
        SimulatedPlayerBase player = null;
        switch (ability) {
        case 2:
            player = new ScorePlayerBeta();
            break;
        case 3:
            player = new ScorePlayerGamma();
            break;
        case 4:
            player = new DepthPotentialScorePlayer(4,4);
            break;
        default:
            player = new ScorePlayerAlpha();
            break;
        }
        
        return player.calculateBestMove(level);
        
        //TODO: make function with possible difficulties, make function that uses simulatedPlayers
        
    }
}
