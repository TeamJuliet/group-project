package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Abstract class that is used to generate new candies for the positions to be filled.
 * Note: that the function setDesignAndGameProgress has to been called before being called.
 *
 */
public abstract class CandyGenerator implements Serializable {
    
    protected Design design;
    protected GameStateProgress gameStateProgress;
    protected int previousNumberOfIngredientsRemaining;
    protected int ingredientsToDrop;

    // Some CandyGenerators do not need the GameState to throw the next candy.
    public CandyGenerator () {
    	this.ingredientsToDrop = 0;
    }
    
    public void setDesignAndGameProgress (Design design, GameStateProgress gameStateProgress) {
    	this.design = design;
        this.gameStateProgress = gameStateProgress;
        if (this.design == null) return;
        if (this.gameStateProgress == null) 
        	System.err.println("Candy Generator Error: Game state progress is null");
        this.previousNumberOfIngredientsRemaining = gameStateProgress.getIngredientsRemaining();

        int numberOfInitialIngredients = GameStateAuxiliaryFunctions.getIngredientsNumber(design.getBoard());

        this.ingredientsToDrop = gameStateProgress.getIngredientsRemaining() - numberOfInitialIngredients;
    }
    
    public abstract Candy generateCandy(int x);
}
