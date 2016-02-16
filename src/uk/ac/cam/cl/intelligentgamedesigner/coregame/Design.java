package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Design implements Serializable {
	private int height, width;          // The dimensions of the board.
    private Cell[][] boardLayout;       // The layout - specifying the position of special candies and blocks
    private int numberOfMovesAvailable; // The number of moves the player has available
    private int objectiveTarget;		// The target needed to clear whatever game mode
                                        // (if high score, then = the required score)
                                        // (if jelly, then ignore this)
                                        // (if ingredients, then = the number of ingredients that need to be cleared)
    private int numberOfCandyColours;   // The number of unique colours a candy can take
    private GameMode gameMode;			// The game mode

    // Initialise the Design with a default - namely a 10x10 board of empty cells
    public Design () {
    	height = 10;
    	width = 10;
    	boardLayout = new Cell[width][height];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                boardLayout[x][y] = new Cell(CellType.EMPTY, 2);
            }
        }
    	numberOfMovesAvailable = 10;
        objectiveTarget = 1;
        numberOfCandyColours = 6;
        gameMode = GameMode.HIGHSCORE;
    }

    public void setSize(int width, int height) {
    	Cell[][] newBoard = new Cell[width][height];
    	//crop/enlarge to the correct size
    	for(int x=0;x<width;x++){
    		for(int y=0;y<height;y++){
    			if(x<this.width && y<this.height){
    				newBoard[x][y] = boardLayout[x][y];
    			}else{
    				newBoard[x][y] = new Cell(CellType.EMPTY, 0);
    			}
    		}
    	}
    	this.width = width;
    	this.height = height;
    }

    public void setBoard(Cell[][] board){
    	boardLayout = board;
        this.width = board.length;
        this.height = board[0].length;

        // Currently, the default ingredient sinks are set to the bottom-most cells in each column
        for (int x = 0; x < width; x++) {
            int y = height - 1;
            while (y >= 0 && board[x][y].getCellType() == CellType.UNUSABLE) {
                y--;
            }
            if (y >= 0) board[x][y].setIngredientSink();
        }
    }
    
    public Cell[][] getBoard() {
    	return boardLayout;
    }

    public void setRules(GameMode gameMode, int numberOfMovesAvailable, int objectiveTarget, int numberOfCandyColours){
    	this.gameMode               = gameMode;
    	this.numberOfMovesAvailable = numberOfMovesAvailable;
    	this.objectiveTarget        = objectiveTarget;
        this.numberOfCandyColours   = numberOfCandyColours;
    }

    public void setGameMode (GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setNumberOfMovesAvailable (int numberOfMovesAvailable) {
        this.numberOfMovesAvailable = numberOfMovesAvailable;
    }

    public void setObjectiveTarget (int objectiveTarget) {
        this.objectiveTarget = objectiveTarget;
    }

    public void setNumberOfCandyColours (int numberOfCandyColours) {
        this.numberOfCandyColours = numberOfCandyColours;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public GameMode getMode(){
    	return gameMode;
    }

    public Cell getCell (int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return boardLayout[x][y];
        }
        return null;
    }

    public int getNumberOfMovesAvailable () {
        return numberOfMovesAvailable;
    }

    public int getObjectiveTarget () {
        return objectiveTarget;
    }

    public int getNumberOfCandyColours () {
        return numberOfCandyColours;
    }

    @Override
    public boolean equals (Object object) {
        if (!(object instanceof Design)) return false;

        Design toCompare = (Design) object;

        boolean equals = true;

        equals &= (this.height                  == toCompare.height
                && this.width                   == toCompare.width
                && this.numberOfMovesAvailable  == toCompare.numberOfMovesAvailable
                && this.objectiveTarget         == toCompare.objectiveTarget
                && this.numberOfCandyColours    == toCompare.numberOfCandyColours
                && this.gameMode                == toCompare.gameMode);

        // If parameters match, then check the board
        if (equals) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    equals &= boardLayout[x][y].equals(toCompare.boardLayout[x][y]);
                }
            }
        }

        return equals;
    }
}