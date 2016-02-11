package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public abstract class CandyGenerator implements Cloneable, Serializable {
    protected GameState gameState;
    protected int ingredientsToDrop;

    public CandyGenerator (GameState gameState) {
    	this.gameState = gameState;
        this.ingredientsToDrop = gameState.getIngredientsRemaining();
    }
    
    public abstract Candy generateCandy(int x);
}
