package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Class that represents a single cell (tile) on the board.
 *
 */
public class Cell implements Cloneable, Serializable {

	// The maximum jelly level that the board supports.
	public static final int maxJellyLevel = 5;

	// Indicates whether the cell will pass ingredients through.
	private boolean isIngredientSink;

	// The type of the cell.
	// Note: this should be non-null.
	private CellType cellType;

	// The candy that is contained in the cell.
	// Note: if the CellType of this cell does not require a candy then this
	// should be null.
	private Candy candy;

	// The level of jelly that has not been removed from the cell.
	private int jellyLevel = 0;

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

	/**
	 * Creates the candy by specifying.
	 * 
	 * @param cellType
	 *            The type of the cell. (Should be non null).
	 * @param candy
	 *            The candy that will be in the cell (This can be null, but
	 *            should agree to the cell type).
	 * @param jellyLevel
	 *            The level of jelly that will be initially on the cell and has
	 *            to be removed.
	 * @param isIngredientSink
	 *            Indicates whether the cell will be set to be an ingredient
	 *            sink.
	 */
	public Cell(CellType cellType, Candy candy, int jellyLevel,
			boolean isIngredientSink) {
		this.cellType = cellType;
		this.candy = candy;
		this.jellyLevel = jellyLevel;
		this.isIngredientSink = isIngredientSink;
	}

	public Cell(Cell original) {
		this.cellType = original.cellType;
		this.candy = original.candy == null ? null : new Candy(original.candy);
		this.jellyLevel = original.jellyLevel;
		this.isIngredientSink = original.isIngredientSink;
	}

	/**
	 * Sets the jelly level for the cell. Note: This should be used only when
	 * constructing the Design.
	 * 
	 * @param jellyLevel
	 *            the number of jellies that should be placed on the cell.
	 */
	public void setJellyLevel(int jellyLevel) {
		this.jellyLevel = jellyLevel;
		if (this.jellyLevel > maxJellyLevel)
			System.err
					.println("Error: Cell is set to jelly level above maximum.");
	}

	/**
	 * Sets the candy to the cell and changes the cell type appropriately.
	 * 
	 * @param candy
	 *            the candy to be set in the cell.
	 */
	public void setCandy(Candy candy) {
		this.candy = candy;
		if (cellType.equals(CellType.EMPTY) && candy != null)
			cellType = CellType.NORMAL;
	}

	/**
	 * Function that returns the candy in the cell.
	 * 
	 * @return the candy that is stored in the cell (if there is one).
	 */
	public Candy getCandy() {
		return candy;
	}

	/**
	 * Function that returns whether the current cell can be filled. Note: This
	 * is not the only condition for the cell to be filled, since the cell
	 * should also not have a blocker above it in the board.
	 * 
	 * @return
	 */
	public boolean isFillable() {
		return (this.cellType.equals(CellType.EMPTY))
				|| (this.cellType.equals(CellType.LIQUORICE) && !this
						.hasCandy());
	}

	/**
	 * Function that returns whether the cell has a candy.
	 * 
	 * @return if the candy is not null.
	 */
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
	public boolean isMovable() {
		return cellType == CellType.NORMAL
				&& !(hasCandy() && candy.getCandyType().equals(
						CandyType.UNMOVABLE));
	}

	/**
	 * Function that sets this cell to be an ingredient sink.
	 */
	public void setIngredientSink() {
		this.isIngredientSink = true;
	}

	/**
	 * Function that checks if this cell is an ingredient sink, meaning that it
	 * will consume the ingredients that stay on that cell at a given instance.
	 * 
	 * @return true if this is an ingredient sink.
	 */
	public boolean isIngredientSink() {
		return isIngredientSink;
	}

	/**
	 * Function that returns the CellType of the cell.
	 * 
	 * @return the CellType of the cell, which is guaranteed to be non-null.
	 */
	public CellType getCellType() {
		return cellType;
	}

	// added a setter for customisation purposes
	public void setCellType(CellType cellType) {
		this.cellType = cellType;
		if (!cellType.equals(CellType.NORMAL)
				&& !cellType.equals(CellType.LIQUORICE))
			this.candy = null;
	}

	/**
	 * Function that returns the current jelly level of the cell.
	 * 
	 * @return the current jelly level for the cell.
	 */
	public int getJellyLevel() {
		return this.jellyLevel;
	}

	/**
	 * Function that removes the jelly from the cell and returns whether any
	 * jelly was actually removed (i.e. there was jelly to be removed).
	 * 
	 * @return whether jelly was removed.
	 */
	public boolean removeJellyLayer() {
		boolean removedJelly = false;
		if (jellyLevel > 0) {
			--jellyLevel;
			removedJelly = true;
		}
		return removedJelly;
	}

	/**
	 * Function that indicates whether the cell is allowed to drop its candy to
	 * the levels below.
	 * 
	 * @return whether the cell can drop candy to the cells below.
	 */
	public boolean canDropCandy() {
		// This happens if the cell has a candy and it is not a liquorice.
		return hasCandy() && (!cellType.equals(CellType.LIQUORICE));
	}

	/**
	 * Function that checks whether this cell blacks the candies from falling.
	 * This is done when the cell type itself is a blocker or it is stripped
	 * candy that has been detonated, meaning that it has to clear the same row
	 * where it was detonated.
	 * 
	 * @return whether the cell will allow candies to be dropped through it.
	 */
	public boolean blocksCandies() {
		return cellType.blocksCandies()
				|| (hasCandy() && candy.isDetonated() && candy.getCandyType()
						.isStripped());
	}

	@Override
	public boolean equals(Object toCompare) {
		Cell cellToCompare = (Cell) toCompare;

		if (this.candy == null) {
			return (cellToCompare.candy == null
					&& this.cellType == cellToCompare.cellType
					&& this.jellyLevel == cellToCompare.jellyLevel && this.isIngredientSink == cellToCompare.isIngredientSink);
		}

		return (this.candy.equals(cellToCompare.candy)
				&& this.cellType == cellToCompare.cellType
				&& this.jellyLevel == cellToCompare.jellyLevel && this.isIngredientSink == cellToCompare.isIngredientSink);
	}

	@Override
	public Object clone() {
		return new Cell(this);
	}

	@Override
	public String toString() {
		String result = "";
		switch (cellType) {
		case UNUSABLE:
			return "   ";
		case EMPTY:
			return "EEE";
		case ICING:
			return "ICE";
		case NORMAL:
			result += "N";
			break;
		case LIQUORICE:
			result += "L";
			break;
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
		case UNMOVABLE:
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
