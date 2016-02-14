package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Candy implements Cloneable, Serializable {
    final private CandyType   candyType;
    private CandyColour colour;

    private boolean           detonated            = false;
    // Remaining times that the special candy has to detonate.
    // (Mainly used for the wrapped candy).
    private int               detonationsRemaining = 0;
    
    // This is used to distinguish between wrapped-wrapped detonations (where one
    // has radius 0 and the other has radius 3) and single wrapped detonations (where
    // the wrapped candy has radius 1).
    private int wrappedDetonationRadius = 1;

    // made public
    public Candy(CandyColour colour, CandyType candyType) {
        this.colour = colour;
        this.candyType = candyType;
    }

    public Candy(Candy original) {
        this.colour = original.colour;
        this.candyType = original.candyType;
        this.detonated = original.detonated;
        this.detonationsRemaining = original.detonationsRemaining;
    }

    public CandyColour getColour() {
        return colour;
    }

    public CandyType getCandyType() {
        return candyType;
    }

    @Override
    public boolean equals(Object toCompare) {
        Candy candyToCompare = (Candy) toCompare;

        return (this.colour == candyToCompare.colour && this.candyType == candyToCompare.candyType
                && this.detonated == candyToCompare.detonated
                && this.detonationsRemaining == candyToCompare.detonationsRemaining);
    }

    @Override
    public Object clone() {
        Candy clone = new Candy(this.colour, this.candyType);
        clone.detonated = this.detonated;
        clone.detonationsRemaining = this.detonationsRemaining;

        return clone;
    }

    public boolean isDetonated() {
        return detonated;
    }

    public void setDetonated() {
        detonated = true;
        detonationsRemaining = candyType.getDetonations();
    }
    
    public void setWrappedRadius(int radius) {
    	this.wrappedDetonationRadius = radius;
    }
    
    public int getWrappedRadius() {
    	return wrappedDetonationRadius;
    }

    public void setColour (CandyColour colour) {
        this.colour = colour;
    }

    public int getDetonationsRemaining() {
        return detonationsRemaining;
    }

    public void decreaseDetonations() {
        detonationsRemaining--;
    }
}
