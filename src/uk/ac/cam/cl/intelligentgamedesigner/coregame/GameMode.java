package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * The game target mode: 
 *      - HIGHSCORE: the player should reach the score specified as the target. 
 *      - JELLY: the player should clear the number of jellies specified. 
 *      - INGREDIENTS: the number of ingredients specified should be passed through the respective sinks.
 *
 */
public enum GameMode {
    /**
     * The player should reach the target score.
     */
    HIGHSCORE,
    /**
     * The player should clear all the jellies from the board.
     */
    JELLY,
    /**
     * The player should pass the number of ingredients specified.
     */
    INGREDIENTS
}
