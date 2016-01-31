package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Design implements Serializable {
	private int height, width;          // The dimensions of the board.
    private Cell[][] boardLayout;       // The layout - specifying the position of special candies and blocks
    private int numberOfMoves;          // The number of moves the player has available
    private GameMode gameMode;			// The game mode
    
    public Design(){
    	height = 10;
    	width = 10;
    	boardLayout = new Cell[width][height];
    	numberOfMoves = 10;
    	gameMode = GameMode.HIGHSCORE;
    }
    public void setSize(int width, int height){
    	this.width = width;
    	this.height = height;
    }
    public void setBoard(Cell[][] board){
    	boardLayout = board;
    }
    public void setRules(GameMode gameMode, int numberOfMoves){
    	this.gameMode = gameMode;
    	this.numberOfMoves = numberOfMoves;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
}