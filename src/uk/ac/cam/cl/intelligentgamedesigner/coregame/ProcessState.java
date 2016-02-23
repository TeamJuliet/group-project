package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that has the possible states of the GameState state diagram in between generations of the board.
 *
 */
public enum ProcessState {
    MATCH_AND_REPLACE,
    SHUFFLE,
    DETONATE_PENDING,
    BRING_DOWN_CANDIES,
    PASSING_INGREDIENTS,
    FILL_BOARD,
    AWAITING_MOVE
}
