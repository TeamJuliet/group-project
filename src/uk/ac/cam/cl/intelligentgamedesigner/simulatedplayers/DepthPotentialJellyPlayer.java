package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;

public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

    public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead,numOfStatesInPool);
    }
    
    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        //TODO: look into balancing this
        int jellyNumber = getJellyNumber(gameState);
        int metric;
        switch(jellyNumber){
        case 0: metric = 1024;
        case 1: metric = 512;
        case 2: metric = 256;
        case 3: metric = 128;
        case 4: metric = 64;
        default: metric = 32 -jellyNumber/8;
        }
        return new GameStateMetric(metric);
    }
}
