package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.Random;

public abstract class ArrayLevelRepresentation extends LevelRepresentation {
    protected DesignBoard board;
    protected static int maxWidth = 9;
    protected static int maxHeight = 9;
    protected int numberOfCandyColours;

    public ArrayLevelRepresentation(Random random, int numberOfCandyColours) {
    	super(random);

        this.numberOfCandyColours = numberOfCandyColours;

        // Initialise the board
        board = new DesignBoard(maxWidth, maxHeight, random);
    }
    
    @Override
    public ArrayLevelRepresentation clone() {
    	ArrayLevelRepresentation clone = (ArrayLevelRepresentation) super.clone();
    	
    	// Copy the board.
    	clone.board = new DesignBoard(board);

        // Copy the number of candy colours
        clone.numberOfCandyColours = this.numberOfCandyColours;
    	
    	return clone;
    }

    public void mutate() {
        board.mutateCellType();
    }
    
	@Override
    public void crossoverWith(LevelRepresentation levelRepresentation) {
		ArrayLevelRepresentation l = (ArrayLevelRepresentation) levelRepresentation;
		board.crossoverWith(l.board);
    }
    
    /**
     * Creates a Design instance that includes the basic layout of the board,
     * but excludes the parameters specific to a particular game type.
     */
    @Override
	public Design getDesign() {
        Design design = new Design();

        design.resizeBoard(board.width, board.height);

        Cell[][] designBoard = new Cell[board.width][board.height];

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                switch (board.get(x, y).getDesignCellType()) {
                    case UNUSABLE:
                        designBoard[x][y] = new Cell(CellType.UNUSABLE, board.get(x, y).getJellyLevel());
                        break;
                    case EMPTY:
                        designBoard[x][y] = new Cell(CellType.EMPTY, board.get(x, y).getJellyLevel());
                        break;
                    case ICING:
                        designBoard[x][y] = new Cell(CellType.ICING, board.get(x, y).getJellyLevel());
                        break;
                    case LIQUORICE:
                        designBoard[x][y] = new Cell(CellType.LIQUORICE, board.get(x, y).getJellyLevel());
                        break;
                }
            }
        }

        design.setBoard(designBoard);

        // Set the general parameters
        design.setNumberOfCandyColours(this.numberOfCandyColours);

        return design;
    }
    
    @Override
    public double getAestheticFitness() {
    	return AestheticChecker.calculateFitness(board);
    }

    @Override
    public double getConstraintFitness() {
        return ConstraintChecker.calculateFitness(board);
    }


    @Override
    public String representationToString() {
        String result = "";
        String[] r = {"X", " ", "I", "L"};
        for (int y = 0; y < board.height; y++) {
            for (int x = 0; x < board.width; x++) {
                int t = board.get(x, y).getDesignCellType().ordinal();
                result += (r[t] + ' ');
            }
            result += "\n";
        }

        return result;
    }

    @Override
    public void printRepresentation () {
        System.out.println(representationToString());
    }
    
}
