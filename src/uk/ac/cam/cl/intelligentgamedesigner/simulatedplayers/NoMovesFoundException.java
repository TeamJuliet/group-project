package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

public class NoMovesFoundException extends Exception {
    GameState state;

    public NoMovesFoundException(GameState state) {
        this.state = new GameState(state);
    }
}
