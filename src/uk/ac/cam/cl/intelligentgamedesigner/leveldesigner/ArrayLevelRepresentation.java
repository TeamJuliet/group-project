package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.Random;

public abstract class ArrayLevelRepresentation extends LevelRepresentation {
    protected DesignCellType[][] board;
    protected static int maxWidth = 10;
    protected static int maxHeight = 10;
    protected static int cellModulo = 4;
    protected ArrayList<Parameter> parameters;

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours
        (subclasses may have additional parameters in this list)

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public ArrayLevelRepresentation(Random random) {
    	super(random);

        parameters = new ArrayList<>();

        // Number of moves is initialised in the range: 10-40 (inclusive)
        parameters.add(new Parameter(random, 10, 40));

        // Number of candy colours is initialised in the range: 4-6 (inclusive)
        parameters.add(new Parameter(random, 4, 6));

        // TODO: The above initialisations are just guesses at the moment. We may need to refine them

        // Initialise the board
        this.board = new DesignCellType[ArrayLevelRepresentation.maxWidth][ArrayLevelRepresentation.maxHeight];
        for (int x = 0; x < ArrayLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < ArrayLevelRepresentation.maxHeight; y++) {
                board[x][y] = DesignCellType.values()[random.nextInt(ArrayLevelRepresentation.cellModulo)];
            }
        }
    }
    
    @Override
    public ArrayLevelRepresentation clone() {
    	ArrayLevelRepresentation clone = (ArrayLevelRepresentation) super.clone();
    	
    	// Copy the board.
    	clone.board = new DesignCellType[board.length][];
    	for (int x = 0; x < board.length; x++) {
    		clone.board[x] = board[x].clone();
    	}
    	
    	// Copy the list of parameters.
    	int length = parameters.size();
    	clone.parameters = new ArrayList<>(length);
    	for (int i = 0; i < length; i++) {
    		clone.parameters.add(parameters.get(i).copy());
    	}
    	
    	return clone;
    }
    
    /**
     * Returns a random integer between min and max inclusive, that is different from the previous value.
     * @param oldValue the previous value.
     * @param min the minimum new value.
     * @param max the maximum new value.
     * @return the random new value.
     */
    protected int getNewRandomInt(int oldValue, int min, int max) {
    	int newValue = random.nextInt(max - min) + min;
    	if (newValue >= oldValue) {
    		newValue++;
    	}
    	return newValue;
    }

    public void mutate() {
        if (random.nextDouble() < 0.75) {
            // Mutate the board (75% probability).

            int x = random.nextInt(maxWidth);
            int y = random.nextInt(maxHeight);

            int oldValue = board[x][y].ordinal();
            int newValue = getNewRandomInt(oldValue, 0, cellModulo - 1);
            board[x][y] = DesignCellType.values()[newValue];
        } else {
            // Mutate a parameter (25% probability).
            Parameter p = parameters.get(random.nextInt(parameters.size()));
            p.generateNewValue();
        }
    }

    /** Creates a Design instance that includes the basic layout of the board, but excludes the parameters specific to
     *  particular game type.
     * 
     */
    protected Design getBaseDesign() {
        Design design = new Design();

        design.setSize(maxWidth, maxHeight);

        Cell[][] designBoard = new Cell[maxWidth][maxHeight];

        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                switch (board[x][y]) {
                    case UNUSABLE:
                        designBoard[x][y] = new Cell(CellType.UNUSABLE);
                        break;
                    case EMPTY:
                        designBoard[x][y] = new Cell(CellType.EMPTY);
                        break;
                    case ICING:
                        designBoard[x][y] = new Cell(CellType.ICING);
                        break;
                    case LIQUORICE:
                        designBoard[x][y] = new Cell(CellType.LIQUORICE);
                        break;
                }
            }
        }

        design.setBoard(designBoard);

        // TODO: set Max Moves

        // TODO: set number of candy colours

        return design;
    }

    /**
     * This function takes a parent and two children, and crosses over the parent
     * boards with t
     *
     * @param parent
     * @param child1
     * @param child2
     */
    public void crossoverWith(ArrayLevelRepresentation parent,
                              ArrayLevelRepresentation child1,
                              ArrayLevelRepresentation child2) {

        boolean isVerticalSplit = (random.nextInt(2) > 0);

        int splitEnd = random.nextInt(maxWidth + 1);

        for (int x = 0; x < splitEnd; x++) {
            for (int y = 0; y < maxHeight; y++) {

                if (isVerticalSplit) {
                    child1.board[x][y] = this.board[x][y];
                    child2.board[x][y] = parent.board[x][y];
                } else {
                    child1.board[y][x] = this.board[y][x];
                    child2.board[y][x] = parent.board[y][x];
                }
            }
        }

        for (int x = splitEnd; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                if (isVerticalSplit) {
                    child1.board[x][y] = parent.board[x][y];
                    child2.board[x][y] = this.board[x][y];
                } else {
                    child1.board[y][x] = parent.board[y][x];
                    child2.board[y][x] = this.board[y][x];
                }
            }
        }
    }
    
    @Override
    public double getAestheticFitness() {
    	int maxX = maxWidth / 2;
    	int score = 0;
    	for (int x = 0; x < maxX; x++) {
    		for (int y = 0; y < maxHeight; y++) {
    			if (board[x][y] == board[maxWidth - x - 1][y]) {
    				score++;
    			}
    		}
    	}
    	
    	double perfectScore = maxX * maxHeight;
    	return score / perfectScore;
    }
    
    public void printBoard() {
    	String[] r = {"X", " ", "I", "L"};
    	for (int y = 0; y < maxHeight; y++) {
    		for (int x = 0; x < maxWidth; x++) {
    			int t = board[x][y].ordinal();
    			System.out.print(r[t] + ' ');
    		}
    		System.out.println();
    	}
    }
    
}
