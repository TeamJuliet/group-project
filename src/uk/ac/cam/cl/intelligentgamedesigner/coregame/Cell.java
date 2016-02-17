package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Cell implements Cloneable, Serializable {

    // TODO: Is there really a need for this?
    public static final int maxJellyLevel = 5;
    private boolean         isIngredientSink;
    private CellType        cellType;
    private Candy           candy;
    private int             jellyLevel;

    public Cell(CellType cellType) {
        this(cellType, null, 0, false);
    }

    public Cell(CellType cellType, Candy candy) {
        this(cellType, candy, 0, false);
    }

    public Cell(CellType cellType, int jellyLevel) {
        this(cellType, null, jellyLevel, false);
    }

    public Cell(CellType cellType, Candy candy, int jellyLevel) {
        this(cellType, candy, jellyLevel, false);
    }

    public Cell(CellType cellType, Candy candy, int jellyLevel, boolean isIngredientSink) {
        this.cellType = cellType;
        this.candy = candy;
        this.jellyLevel = jellyLevel;
        this.isIngredientSink = isIngredientSink;
    }

    public Cell(Cell original) {
        this.cellType = original.cellType;
        this.candy = original.candy;
        this.jellyLevel = original.jellyLevel;
        this.isIngredientSink = original.isIngredientSink;
    }

    public void setJellyLevel(int jellyLevel) {
        this.jellyLevel = jellyLevel;
    }

    // Sets the candy to the cell and changes its type appropriately.
    public void setCandy(Candy candy) {
        this.candy = candy;
        if (cellType.equals(CellType.EMPTY))
            cellType = CellType.NORMAL;
    }

    public Candy getCandy() {
        return candy;
    }

    // Function that returns whether the current cell can be filled.
    public boolean isFillable() {
        return (this.cellType.equals(CellType.EMPTY)) || (this.cellType.equals(CellType.LIQUORICE) && !this.hasCandy());
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

    public void setIngredientSink () {
        this.isIngredientSink = true;
    }

    public boolean isIngredientSink() {
        return isIngredientSink;
    }

    public CellType getCellType() {
        return cellType;
    }

    // added a setter for customisation purposes
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
        if (!cellType.equals(CellType.NORMAL) && !cellType.equals(CellType.LIQUORICE))
            this.candy = null;
    }

    public int getJellyLevel() {
        return jellyLevel;
    }

    public boolean removeJellyLayer() {
        boolean removedJelly = false;
        if (jellyLevel > 0) {
            --jellyLevel;
            removedJelly = true;
        }
        return removedJelly;
    }

    public boolean canDropCandy() {
        return hasCandy() && (!cellType.equals(CellType.LIQUORICE));
    }

    public boolean blocksCandies() {
        return cellType.blocksCandies() || (hasCandy() && candy.isDetonated() && candy.getCandyType().isStripped());
    }

    @Override
    public boolean equals(Object toCompare) {
        Cell cellToCompare = (Cell) toCompare;

        if (this.candy == null) {
            return (cellToCompare.candy == null && this.cellType == cellToCompare.cellType
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

    @Override
    public String toString() {
        String result = "";
        switch (cellType) {
        case UNUSABLE:
            return "XXX";
        case EMPTY:
            return "EEE";
        case ICING:
            return "ICE";
        case NORMAL:
            result += "N";
            break;
        case LIQUORICE:
            result += "L";
        case DONT_CARE:
            return "D_C";
        }

        switch (candy.getCandyType()) {
        case NORMAL:
            result += "N";
            break;
        case BOMB:
            return "BMB";
        case INGREDIENT:
            result += "I";
            break;
        case UNMOVEABLE:
            return "UNM";
        case WRAPPED:
            result += "W";
            break;
        case HORIZONTALLY_STRIPPED:
            result += "H";
            break;
        case VERTICALLY_STRIPPED:
            result += "V";
            break;
        }

        switch (candy.getColour()) {
        case BLUE:
            result += "B";
            break;
        case GREEN:
            result += "G";
            break;
        case ORANGE:
            result += "O";
            break;
        case PURPLE:
            result += "P";
            break;
        case RED:
            result += "R";
            break;
        case YELLOW:
            result += "Y";
            break;
        }

        return result;
    }
}
