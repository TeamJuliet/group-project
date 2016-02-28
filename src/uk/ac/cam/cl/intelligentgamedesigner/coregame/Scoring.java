package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that contains constants and functions related to scoring. All constants
 * should be defined here.
 *
 */
public class Scoring {

    /**
     * When 3 candies are matched
     */
    public static final int MATCHED_3                   = 60;
    
    /**
     * When 4 candies are matched
     */
    public static final int MATCHED_4                   = 120;
    
    /**
     * When 5 candies are matched not in the same row or column (i.e. short T,
     * half-short half long T, L, and + shapes)
     */
    public static final int MATCHED_5                   = 200;
    
    /**
     * When a jelly is part of a match
     */
    public static final int MATCHED_A_JELLY             = 1000;
    
    /**
     * When a wrapped candy is activated (note: this should occur twice).
     */
    public static final int DETONATE_WRAPPED_CANDY      = 540;
    
    /**
     * When a bomb is matched.
     */
    public static final int DETONATE_BOMB               = 3000;
    
    /**
     * When a stripped candy has been detonated.
     */
    public static final int DETONATE_STRIPPED_CANDY     = 0;
    
    /**
     *  When an ingredient reaches the sink.
     */
    public static final int BROUGHT_INGREDIENT_DOWN     = 10000;
    
    /**
     *  When a bomb has been formed.
     */
    public static final int MADE_BOMB                   = 200;
    
    /**
     *  When a wrapped candy has been formed.
     */
    public static final int MADE_WRAPPED_CANDY          = 200;
    
    /**
     *  When a stripped candy has been formed.
     */
    public static final int MADE_STRIPPED_CANDY         = 120;
    
    /**
     *  When detonating a candy with single stripped detonation.
     */
    public static final int STRIPPED_INDIVIDUAL         = 60;
    
    /**
     * When detonating a candy with single wrapped detonation.
     */
    public static final int WRAPPED_INDIVIDUAL          = 60;
    
    /**
     * When detonating a candy with single bomb.
     */
    public static final int BOMB_INDIVIDUAL             = 60;
    
    /**
     *  When detonated using a wrapped stripped combination.
     */
    public static final int WRAPPED_STRIPPED_INDIVIDUAL = 60;
    
    /**
     *  When icing has been cleared.
     */
    public static final int ICING_CLEARED               = 20;

    /**
     *  Constant to indicate that no additional score should be counted.
     */
    public static final int NO_ADDITIONAL_SCORE         = 0;
}
