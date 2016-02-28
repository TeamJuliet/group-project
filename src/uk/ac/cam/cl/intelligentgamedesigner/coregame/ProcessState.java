package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that has the possible states of the GameState state diagram in between generations of the board.
 *
 */
public enum ProcessState {
	/**
	 * Stage where matches occur and the cells that matched are emptied.
	 */
    MATCH_AND_REPLACE,
    
    /**
     * The board is shuffled if there are no moves to be made, until there are.
     */
    SHUFFLE,
    /**
     * Detonate the special candies that were triggered in the first stage.
     */
    DETONATE_PENDING,
    
    /**
     * Bring down the candies to the fillable positions on the board.
     */
    BRING_DOWN_CANDIES,
    
    /**
     * Pass the ingredients that can be passed through the ingredient sinks.
     */
    PASSING_INGREDIENTS,
    
    /**
     * Fill the empty cells on the board that are not blocked by blocker candies.
     */
    FILL_BOARD,
    
    /**
     * The game state is awaiting a move.
     */
    AWAITING_MOVE,
    
    /**
     * Two candies are swapped. This is used for animation purposes.
     */
    MAKING_SWAP
}
