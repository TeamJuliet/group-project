package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Candy implements Cloneable, Serializable {
    final private CandyColour colour;
    final private CandyType candyType;
    
    private boolean detonated = false;
    private int detonationsRemaining = 0;
    
    Candy (CandyColour colour, CandyType candyType) {
        this.colour = colour;
        this.candyType = candyType;
    }
    
    public CandyColour getColour () {
        return colour;
    }
    
    public CandyType getCandyType() {
    	return candyType;
    }

    @Override
    public boolean equals (Object toCompare) {
        Candy candyToCompare = (Candy) toCompare;

        return (this.colour                 == candyToCompare.colour &&
                this.candyType              == candyToCompare.candyType &&
                this.detonated              == candyToCompare.detonated &&
                this.detonationsRemaining   == candyToCompare.detonationsRemaining);
    }

    @Override
    public Object clone () {
        Candy clone = new Candy(this.colour, this.candyType);
        clone.detonated             = this.detonated;
        clone.detonationsRemaining  = this.detonationsRemaining;

        return clone;
    }
    
    public boolean isDetonated() {
    	return detonated;
    }
    
    public void setDetonated() {
    	detonated = true;
    	detonationsRemaining = candyType.getDetonations();
    }
    
    public int getDetonationsRemaining() {
    	return detonationsRemaining;
    }
    
    public void decreaseDetonations() {
    	detonationsRemaining--;
    }
}
