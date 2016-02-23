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
    HIGHSCORE,
    JELLY,
    INGREDIENTS
}
