package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public enum CellType {
	// Specifies that the cell is not part of the board. This should be used to form complicated shapes other than rectangular boards.
    UNUSABLE,
    // Specifies a cell that is empty, either because there is a blocker above it, or because it has not yet been refilled.
    EMPTY,
    // Specifies a blocker cell that contains icing. This can only be removed if a neighbouring candy is matched.
    ICING,
    NORMAL,
    LIQUORICE,
    DONT_CARE;
    
    public boolean needsCandy() {
    	return this.equals(NORMAL);
    }
    
    public boolean blocksCandies() {
        return this.equals(ICING) || this.equals(LIQUORICE);
    }
}
