package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

public class DepthPotentialIngredientPlayer extends DepthPotentialPlayer {

    @Override
    GameStateMetric getGameStateMetric(GameState gameState) {
        // find all ingredients on the board
        // calculate vertical distances between ingredients and the receiving
        // state below them
        // if ingredient is not above a receiving state return board height (or
        // maybe multiple of it)
        // sum together all the heights, if there are ingredients still to be
        // spawned ...
        // do this for every move
        // return the minimum sum
        return null;
    }

}
