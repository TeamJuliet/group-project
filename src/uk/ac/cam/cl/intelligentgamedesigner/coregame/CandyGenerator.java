package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public abstract class CandyGenerator implements Cloneable, Serializable {
    
    protected GameState gameState;
    protected int ingredientsToDrop;

    // Some CandyGenerators do not need the GameState to throw the next candy.
    public CandyGenerator () {
    	this.ingredientsToDrop = 0;
    }
    
    // TODO: Maybe this should be Design rather than gameState
    public CandyGenerator (GameState gameState) {
    	this.gameState = gameState;
        this.ingredientsToDrop = gameState.getGameProgress().ingredientsRemaining;
    }
    
    public abstract Candy generateCandy(int x);
}
