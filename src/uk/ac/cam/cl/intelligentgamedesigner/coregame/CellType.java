package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * CellType determines the type of a cell.
 *
 */
public enum CellType {
    /**
     * Specifies that the cell is not part of the board. This should be used to
     * form complicated shapes other than rectangular boards.
     */
    UNUSABLE,

    /**
     * Specifies a cell that is empty, either because there is a blocker above
     * it, or because it has not yet been refilled.
     */
    EMPTY,

    /**
     * Specifies a blocker cell that contains icing. This can only be removed if
     * a neighbouring candy is matched.
     */
    ICING,

    /**
     * A cell that contains a Candy and has nothing special.
     */
    NORMAL,

    /**
     * A cell that contains a Candy, but will not allow it to move in order to
     * form matches. This is converted to a normal cell when the candy is
     * matched.
     */
    LIQUORICE,

    /**
     * Used for testing, in order to indicate that we do not care about this
     * cell in the unit test.
     */
    DONT_CARE;

    /**
     * Function that specifies whether this cell type requires a candy.
     * 
     * @return
     */
    public boolean needsCandy() {
        return this.equals(NORMAL) || this.equals(LIQUORICE);
    }

    /**
     * Function that returns whether this cell type is a blocker, meaning that
     * it does not let ingredients get through.
     * 
     * @return whether the cell type is a blocker.
     */
    public boolean isBlocker() {
        return this.equals(ICING) || this.equals(LIQUORICE);
    }
}
