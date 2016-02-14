package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public abstract class CandyGenerator implements Serializable {
    
    protected GameState gameState;
    protected int ingredientsToDrop;

    // Some CandyGenerators do not need the GameState to throw the next candy.
    public CandyGenerator () {
    	this.ingredientsToDrop = 0;
    }
    
    // TODO: Maybe this should be Design rather than gameState
    public CandyGenerator (GameState gameState) {
    	this.gameState = gameState;

        Cell[][] board = gameState.getLevelDesign().getBoard();
        int numberOfInitialIngredients = 0;
        for (int x = 0; x < gameState.width; x++) {
            for (int y = 0; y < gameState.height; y++) {
                Candy candy = board[x][y].getCandy();
                if (candy != null && candy.getCandyType() == CandyType.INGREDIENT) {
                    numberOfInitialIngredients++;
                }
            }
        }

        this.ingredientsToDrop = gameState.getGameProgress().ingredientsRemaining - numberOfInitialIngredients;
    }
    
    public abstract Candy generateCandy(int x);
}
