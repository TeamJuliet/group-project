package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

/**
 * 
 * Class that contains the basic functions that all simulated players should
 * implement. Every simulated player should extend this.
 *
 */
public abstract class SimulatedPlayerBase {

	/**
	 * Function that plays the game until the game is over.
	 * 
	 * @param game
	 *            The game that the player should play.
	 * @throws NoMovesFoundException
	 *             In case it reaches a state where there is no move found it
	 *             throws this exception.
	 */
	public void solve(GameState game) throws NoMovesFoundException {
		while (!game.isGameOver()) {
			try {
				game.makeFullMove(calculateBestMove(game));
			} catch (InvalidMoveException e) {
				this.printInvalidMoveError(e.invalidMove);
				try { // TODO: this is horrible, fix it
					game.makeFullMove(game.getValidMoves().get(0));
				} catch (InvalidMoveException exception) {
					return;
				}
			}
		}
	}

	/**
	 * Function that returns the move suggestion of the player for the
	 * currentState passed.
	 * 
	 * @param currentState
	 *            The state for which the player will return the suggested move.
	 * @return The suggested move.
	 * @throws NoMovesFoundException
	 *             Exception thrown if there are no possible moves on the
	 *             current state.
	 */
	public abstract Move calculateBestMove(GameState currentState)
			throws NoMovesFoundException;

	/**
	 * Error message when moves are not found.
	 */
	// TODO: Should this be protected.
	public void noMovesFound() {
		System.err.format("WARNING! %s didn't return a valid move!\n", this
				.getClass().getSimpleName());
	}

	// TODO: Do we need this?
	private void printInvalidMoveError(Move move) {
	}
}
