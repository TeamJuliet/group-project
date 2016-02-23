package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Class that describe the type of the candy. This includes the normal candies,
 * the special ones (stripped or wrapped), the colour bomb, the ingredient and
 * the Unmovable type (which is used for exploration purposes).
 *
 */
public enum CandyType {
    NORMAL, // Simple candy without any detonation effects.
    VERTICALLY_STRIPPED, // Horizontally stripped special candy.
    HORIZONTALLY_STRIPPED, // Horizontally stripped special candy.
    WRAPPED, // Wrapped special candy.
    BOMB, // Does not have a Candy Color
    INGREDIENT, // Does not have a Candy Color
    UNMOVEABLE, // Does not have a Candy Color and it is used to get next
                // partially known states.
    ;

    /**
     * Function that checks if the candy type is special (i.e. has color and
     * property).
     * 
     * @return whether this is a stripped candy or a wrapped candy.
     */
    public boolean isSpecial() {
        return this == HORIZONTALLY_STRIPPED || this == VERTICALLY_STRIPPED || this == WRAPPED;
    }

    /**
     * Function that checks if the current candy is stripped.
     * 
     * @return true if the candy is vertically stripped or horizontally
     *         stripped.
     */
    public boolean isStripped() {
        return this == VERTICALLY_STRIPPED || this == HORIZONTALLY_STRIPPED;
    }

    /**
     * Function that returns whether this candy type requires a candy.
     * 
     * @return whether this type needs a candy.
     */
    public boolean needsColour() {
        return this == NORMAL || isSpecial();
    }

    /**
     * Function that returns the number a certain type of candy detonates.
     * 
     * @return the number of detonations that it should perform before being
     *         removed.
     */
    public int getDetonations() {
        switch (this) {
        case VERTICALLY_STRIPPED:
            return 1;
        case HORIZONTALLY_STRIPPED:
            return 1;
        case WRAPPED:
            return 2;
        default:
            return 0;

        }
    }
}
