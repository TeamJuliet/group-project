package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Cell implements Cloneable, Serializable {

    public static final int maxJellyLevel = 5;
    private CellType        cellType;
    private Candy           candy;
    private int             jellyLevel    = 0;

    public Cell(CellType cellType) {
        this.cellType = cellType;
        this.candy = null;
    }

    public Cell(CellType cellType, Candy candy) {
        this.cellType = cellType;
        this.candy = candy;
    }

    public Cell(CellType cellType, int jellyLevel) {
        this.cellType = cellType;
        this.jellyLevel = jellyLevel;
    }

    public Cell(CellType cellType, Candy candy, int jellyLevel) {
        this.cellType = cellType;
        this.candy = candy;
        this.jellyLevel = jellyLevel;
    }

    public Cell(Cell original) {
        this.cellType = original.cellType;
        this.candy = original.candy;
        this.jellyLevel = original.jellyLevel;
    }

    public void setJellyLevel(int jellyLevel) {
        this.jellyLevel = jellyLevel;
    }

    public void setCandy(Candy candy) {
        this.candy = candy;
        if (candy != null)
            cellType = CellType.NORMAL;
    }

    public Candy getCandy() {
        return candy;
    }
    
    public boolean isFillable() {
    	return (this.cellType.equals(CellType.EMPTY))
                || (this.cellType.equals(CellType.LIQUORICE)
                || (this.cellType.equals(CellType.UNUSABLE)));
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
        if (candyType == CandyType.BOMB || candyType == CandyType.INGREDIENT) {
            candy = new Candy(null, candyType);
        } else {
            candy = new Candy(candy.getColour(), candyType);
        }
    }

    // Function that returns whether it is possible to move the contents of the
    // block.
    public boolean isMoveable() {
    	return cellType == CellType.NORMAL && !(hasCandy() && candy.getCandyType().equals(CandyType.UNMOVEABLE));
    }

    public CellType getCellType() {
        return cellType;
    }

    // added a setter for customisation purposes
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public int getJellyLevel() {
        return jellyLevel;
    }

    @Override
    public boolean equals(Object toCompare) {
        Cell cellToCompare = (Cell) toCompare;

        if (this.candy == null) {
            return (cellToCompare.candy == null
                    && this.cellType == cellToCompare.cellType
                    && this.jellyLevel == cellToCompare.jellyLevel);
        }

        return (this.candy.equals(cellToCompare.candy) && this.cellType == cellToCompare.cellType
                && this.jellyLevel == cellToCompare.jellyLevel);
    }

    @Override
    public Object clone() {
        try {
            Cell clone = (Cell) super.clone();
            clone.candy = (Candy) this.candy.clone();
            return clone;

        } catch (CloneNotSupportedException e) {
            System.err.println("Could not clone Cell");
            return null;
        }
    }
}
