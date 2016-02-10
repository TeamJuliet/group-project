package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Candy implements Cloneable, Serializable {
    final private CandyType   candyType;
    private CandyColour colour;

    private boolean           detonated            = false;
    private int               detonationsRemaining = 0;

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
