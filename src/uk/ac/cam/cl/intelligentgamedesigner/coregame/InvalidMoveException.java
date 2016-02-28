package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Exception that will be thrown when the move attempted is invalid.
 *
 */
public class InvalidMoveException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The invalid move that was made.
	 */
	public final Move invalidMove;
  
	/**
	 * 
	 * @param move The move that was invalid.
	 */
    public InvalidMoveException(Move move) {
    	super("You made an invalid move: " + move);
        this.invalidMove = move;
    }
}
