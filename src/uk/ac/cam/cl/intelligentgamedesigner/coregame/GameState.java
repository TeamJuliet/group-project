package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devankuleindiren on 21/01/2016.
 */

public class GameState {
    Cell[][]       board;
    int            width;
    int            height;
    int            movesRemaining;
    int            score;
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
    	
    	refreshBoard();
    	fillBoard();
    }
    
    private void refreshBoard() {
    	fillBoard();
//    	while(false) {
//    		fillBoard();
//    	}
    	
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

    Cell getCell(int x, int y) {
        return board[x][y];
    }

    void makeMove(Move move) throws InvalidMoveException {

    }

    public List<Match> getMatches() {
        List<Match> matches = new ArrayList();

        // since we check downwards and rightwards we don't need to check last
        // two rows/columns as min length is 3;
        for (int x = 0; x < width - 2; x++) {
            for (int y = 0; y < height - 2; y++) {
                int height = 1;
                int width = 1;

                CandyColour colour = getCell(x, y).getCandy().getColour();

                while (getCell(x + 1 + width, y).getCandy()
                        .getColour() == colour) {
                    width++;
                }
                while (getCell(x, y + 1 + height).getCandy()
                        .getColour() == colour) {
                    height++;
                }

                // TODO: there is quite a lot of repetition in those if
                // statements parphaps this can be optimised
                if (height > 2 && width > 2) {
                    Coordinates[] cells = new Coordinates[height + width - 1];
                    for (int i = 0; i < width; i++) {
                        cells[i] = new Coordinates(x + i, y);
                    }
                    for (int j = 1; j < height; j++) { // start from one since
                                                       // we don't want to
                                                       // include original
                                                       // candy twice
                        cells[width + j] = new Coordinates(x, y + j);
                    }
                    matches.add(new Match(cells, true));
                } else if (width > 2) {
                    Coordinates[] cells = new Coordinates[height + width - 1];
                    for (int i = 0; i < width; i++) {
                        cells[i] = new Coordinates(x + i, y);
                    }
                    matches.add(new Match(cells));
                } else if (height > 2) {
                    Coordinates cells[] = new Coordinates[height];
                    for (int j = 0; j < height; j++) {
                        cells[j] = new Coordinates(x, y + j);
                    }
                    matches.add(new Match(cells));
                }

            }
        }

        return matches;
    }
}
