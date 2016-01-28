package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class Cell {
    private CellType cellType;
    private Candy    candy;
    private int jellyLevel = 0;
    
    public Cell(CellType cellType) {
        this.cellType = cellType;
        this.candy = null;
    }

    public Cell(CellType cellType, Candy candy) {
        this.cellType = cellType;
        this.candy = candy;
    }
    
    public void setJellyLevel(int jellyLevel) {
    	this.jellyLevel = jellyLevel;
    }

    public void setCandy(Candy candy) {
        this.candy = candy;
        if (candy != null) cellType = CellType.NORMAL;
    }

    public Candy getCandy() {
        return candy;
    }

    public boolean hasCandy() {
    	return candy != null;
    }
    
    public void removeCandy() {
        cellType = CellType.EMPTY;
        candy = null;
    }
    
    // Upgrades candy to the type specified.
    public void changeCandyType(CandyType candyType) {
    	if (candyType == CandyType.BOMB) {
    		candy = new Candy(null, CandyType.BOMB);
    	} else {
    		candy = new Candy(candy.getColour(), candyType);
    	}
    }
    
    // Function that returns whether it is possible to move the contents of the block.
    public boolean isMoveable() {
    	return cellType == CellType.NORMAL;
    }
    
    public CellType getCellType() {
        return cellType;
    }
}
