package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Design implements Serializable {
	private int height, width;          // The dimensions of the board.
    private Cell[][] boardLayout;       // The layout - specifying the position of special candies and blocks
    private int numberOfMoves;          // The number of moves the player has available
    private int objectiveTarget;		// The target needed to clear whatever game mode
    	//(if high score, then = the required score)
    	//(if jelly, then ignore this)
    	//(if ingredients, then = the number of ingredients that need to be cleared)
    private GameMode gameMode;			// The game mode

    //initialise with no values. specified elsewhere

    public Design(){ //initialise with default values. specified elsewhere
    	height = 10;
    	width = 10;
    	boardLayout = new Cell[width][height];
    	numberOfMoves = 10;
    	gameMode = GameMode.HIGHSCORE;
    	objectiveTarget = 1;
    }
    
    public void setSize(int width, int height){
    	this.width = width;
    	this.height = height;
    }

    public void setBoard(Cell[][] board){
    	boardLayout = board;
    }

    public void setRules(GameMode gameMode, int numberOfMoves, int objectiveTarget){
    	this.gameMode = gameMode;
    	this.numberOfMoves = numberOfMoves;
    	this.objectiveTarget = objectiveTarget;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }

    public Cell getCell (int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return boardLayout[x][y];
        }
        return null;
    }
}