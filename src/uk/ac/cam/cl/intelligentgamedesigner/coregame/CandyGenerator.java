package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import sun.security.krb5.internal.crypto.Des;

import java.io.Serializable;

public abstract class CandyGenerator implements Serializable {
    
    protected Design design;
    protected GameStateProgress gameStateProgress;
    protected int previousNumberOfIngredientsRemaining;
    protected int ingredientsToDrop;

    // Some CandyGenerators do not need the GameState to throw the next candy.
    public CandyGenerator () {
    	this.ingredientsToDrop = 0;
    }
    
    // TODO: Maybe this should be Design rather than gameState
    public CandyGenerator (Design design, GameStateProgress gameStateProgress) {
    	this.design = design;
        this.gameStateProgress = gameStateProgress;
        this.previousNumberOfIngredientsRemaining = gameStateProgress.getIngredientsRemaining();

        Cell[][] board = design.getBoard();
        int numberOfInitialIngredients = 0;
        for (int x = 0; x < design.getWidth(); x++) {
            for (int y = 0; y < design.getHeight(); y++) {
                Candy candy = board[x][y].getCandy();
                if (candy != null && candy.getCandyType() == CandyType.INGREDIENT) {
                    numberOfInitialIngredients++;
                }
            }
        }

        this.ingredientsToDrop = gameStateProgress.getIngredientsRemaining() - numberOfInitialIngredients;
    }
    
    public abstract Candy generateCandy(int x);
}
