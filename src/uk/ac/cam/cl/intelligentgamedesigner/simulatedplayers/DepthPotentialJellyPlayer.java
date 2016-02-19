package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getJellyNumber;

public class DepthPotentialJellyPlayer extends DepthPotentialPlayer {

    public DepthPotentialJellyPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        super(numOfStatesAhead, numOfStatesInPool);
    }

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        // TODO: look into balancing this
        int jellyNumber = getJellyNumber(gameState);
        int metric;
        switch (jellyNumber) {
        case 0:
            metric = 1024;
            break;
        case 1:
            metric = 512;
            break;
        case 2:
            metric = 256;
            break;
        case 3:
            metric = 128;
            break;
        case 4:
            metric = 64;
            break;
        default:
            metric = 32 - jellyNumber / 8;
            break;
        }
        return new GameStateMetric(metric);
    }
}
