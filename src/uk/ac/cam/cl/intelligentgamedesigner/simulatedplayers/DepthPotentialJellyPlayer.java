package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

import static uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.GameStateMetric.add;
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
            metric = 131072;
            break;
        case 1:
            metric = 65536;
            break;
        case 2:
            metric = 32768;
            break;
        case 3:
            metric = 16384;
            break;
        case 4:
            metric = 8192;
            break;
        case 5:
            metric = 4092;
            break;
        case 6:
            metric = 2048;
            break;
        case 7:
            metric = 1024;
            break;
        default:
            metric = 1024 - jellyNumber * 64;
            break;
        }
        // return new GameStateMetric(metric);

        return add(new GameStateMetric(gameState.getGameProgress().score / 10),
                add(new GameStateMetric(metric), getBlockerMetric(gameState)));
    }
}
