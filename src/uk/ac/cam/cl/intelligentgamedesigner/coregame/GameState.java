package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Created by devankuleindiren on 21/01/2016.
 */
public class GameState {
    Cell[][] board;
    int width;
    int height;
    int movesRemaining;
    int score;
    CandyGenerator candyGenerator;

    public GameState (Design design) {
    	width = 10;
    	height = 10;
    	
    	board = new Cell[width][height];
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			board[x][y] = new Cell(CellType.EMPTY);
    		}
    	}
    	
    	candyGenerator = new CandyGenerator(null);
    	
    	fillBoard();
    }
    
    private void fillBoard() {
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			Cell cell = board[x][y];
    			if (cell.getCellType() == CellType.EMPTY) {
    				Candy candy = candyGenerator.getCandy();
    				cell.setCandy(candy);
    			}
    		}
    	}
    }

    Cell getCell (int x, int y) {
        return board[x][y];
    }

    void makeMove (Move move) throws InvalidMoveException {

    }
}
