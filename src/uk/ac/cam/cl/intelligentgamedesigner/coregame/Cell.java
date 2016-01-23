package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class Cell {
    private CellType cellType;
    private Candy candy;

    public Cell (CellType cellType) {
        this.cellType = cellType;
        this.candy = null;
    }

    public Cell (CellType cellType, Candy candy) {
        this.cellType = cellType;
        this.candy = candy;
    }
    
    public void setCandy(Candy candy) {
    	this.candy = candy;
    }
    
    public Candy getCandy() {
    	return candy;
    }

    public void removeCandy () {
        cellType = CellType.EMPTY;
    }

    public CellType getCellType () {
        return cellType;
    }
}
