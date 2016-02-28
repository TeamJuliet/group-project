package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * Class that contains the information for a candy stored at a certain location
 * on the board. This includes the candy type and the candy colour (if it is
 * applicable).
 * 
 * Note: this class will always have a candy type.
 * 
 * Also, for the special candies extra information is stored for whether the
 * candy has been detonated and how many detonations remaining. In the case of
 * the wrapped candy a special radius is also specified in order to distinguish
 * between wrapped - wrapped detonation (where one wrapped candy should have
 * radius 3 and the other radius 0) while in the normal case it is just radius
 * 1.
 *
 */
public class Candy implements Cloneable, Serializable {
	// The type of the candy contained in the cell.
	final private CandyType candyType;

	// The candy colour of the candy in the cell.
	// Note: This should be null for cases of color bomb, ingredient, unmovable
	// candy or when there is no Candy in the cell.
	private CandyColour colour;

	// Specifies whether the current candy is detonated (if applicable for that
	// candy).
	private boolean detonated = false;

	// Remaining times that the special candy has to detonate.
	// (Mainly used for the wrapped candy).
	private int detonationsRemaining = 0;

	// This is used to distinguish between wrapped-wrapped detonations (where
	// one has radius 0 and the other has radius 3) and single wrapped
	// detonations (where the wrapped candy has radius 1).
	private int wrappedDetonationRadius = 1;

	/**
	 * Construct a candy by providing its colour and its type.
	 * 
	 * @param colour
	 *            The colour of the candy.
	 * @param candyType
	 *            The type of the candy.
	 */
	public Candy(CandyColour colour, CandyType candyType) {
		this.colour = colour;
		this.candyType = candyType;
		if (this.candyType == null)
			System.err.println("Error: The candy type should not be null.");
	}

	/**
	 * Copy Constructor.
	 * 
	 * @param original
	 *            The candy to be copied.
	 */
	public Candy(Candy original) {
		this.colour = original.colour;
		this.candyType = original.candyType;
		this.detonated = original.detonated;
		this.detonationsRemaining = original.detonationsRemaining;
		this.wrappedDetonationRadius = original.wrappedDetonationRadius;
	}

	/**
	 * Function that returns the colour of the candy. Note: This could be null
	 * if the candy type does not specify one.
	 * 
	 * @return the candy colour of the candy.
	 */
	public CandyColour getColour() {
		return colour;
	}

	/**
	 * Function that returns the candy type of the candy or null if it is not
	 * set.
	 * 
	 * @return
	 */
	public CandyType getCandyType() {
		return candyType;
	}

	/**
	 * Function that returns whether the candy has been detonated (in case it is
	 * a special candy).
	 * 
	 * @return Whether the candy has been detonated.
	 */
	public boolean isDetonated() {
		return this.detonated;
	}

	/**
	 * Function that sets the candy to detonated. If the candy does not need any
	 * detonations then the remaining detonations will be set to 0.
	 */
	public void setDetonated() {
		this.detonated = true;
		this.detonationsRemaining = candyType.getDetonations();
	}

	/**
	 * Function that sets the detonation radius for the wrapped candy. Read the
	 * class description for further information.
	 * 
	 * @param radius
	 *            Radius of the wrapped candy that is being detonated. That
	 *            should either be 3, 1 or 0 (which are included in the
	 *            GameConstants as DOUBLE_WRAPPED_RADIUS_B,
	 *            SINGLE_WRAPPED_RADIUS and DOUBLE_WRAPPED_RADIUS_A
	 *            respectively).
	 */
	public void setWrappedRadius(int radius) {
		this.wrappedDetonationRadius = radius;
	}

	/**
	 * Function that returns the wrapped radius for the wrapped candy being
	 * detonated. Read the class description for further information.
	 * 
	 * @return radius of the wrapped candy that is being detonated (0 if it has
	 *         not yet been detonated).
	 */
	public int getWrappedRadius() {
		return this.wrappedDetonationRadius;
	}

	/**
	 * Sets a new colour to the candy. Note: there is no special check to
	 * whether the candy type requires a candy colour or that a candy type that
	 * requires a candy colour was set to null.
	 * 
	 * @param colour
	 *            The new candy colour for the candy.
	 */
	public void setColour(CandyColour colour) {
		this.colour = colour;
		if (this.colour == null && this.candyType.needsColour()) {
			System.err
					.println("Error: This candy does need a colour, but is not set one.");
		} else if (this.colour != null && !this.candyType.needsColour()) {
			System.err
					.println("Error: This candy does not need a colour, but is set one.");
		}
	}

	/**
	 * 
	 * @return detonations remaining for this candy, provided that it is a
	 *         special candy (and that it has been detonated).
	 */
	public int getDetonationsRemaining() {
		return this.detonationsRemaining;
	}

	/**
	 * Function that decreases the detonations remaining for a single candy,
	 * when these are greater than 0.
	 */
	public void decreaseDetonations() {
		if (this.detonationsRemaining > 0)
			detonationsRemaining--;
	}

	@Override
	public boolean equals(Object toCompare) {
		if (toCompare == null)
			return false;
		Candy candyToCompare = (Candy) toCompare;

		return (this.colour == candyToCompare.colour
				&& this.candyType == candyToCompare.candyType
				&& this.detonated == candyToCompare.detonated
				&& this.detonationsRemaining == candyToCompare.detonationsRemaining && this.wrappedDetonationRadius == candyToCompare.wrappedDetonationRadius);
	}

	@Override
	public Object clone() {
		return new Candy(this);
	}
}
