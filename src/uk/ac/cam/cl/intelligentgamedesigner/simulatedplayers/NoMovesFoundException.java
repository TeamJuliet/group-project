package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

/**
 * 
 * Exception raised when no moves where found in a simulated player for a given
 * GameState.
 *
 */
public class NoMovesFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * The GameState where no moves where found.
     */
    public final GameState    state;

    public NoMovesFoundException(GameState state) {
        super("No Moves where found in : \n" + state);
        this.state = new GameState(state);
    }
}
