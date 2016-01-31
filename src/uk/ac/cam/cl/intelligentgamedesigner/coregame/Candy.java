package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class Candy {
    final private CandyColour colour;
    final private CandyType candyType;
    
    private boolean detonated = false;
    private int detonationsRemaining = 0;
    
    //made public
    public Candy (CandyColour colour, CandyType candyType) {
        this.colour = colour;
        this.candyType = candyType;
    }
    
    public CandyColour getColour () {
        return colour;
    }
    
    public CandyType getCandyType() {
    	return candyType;
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
