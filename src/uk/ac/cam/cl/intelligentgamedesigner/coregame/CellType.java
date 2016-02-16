package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public enum CellType {
    UNUSABLE,
    EMPTY,
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
