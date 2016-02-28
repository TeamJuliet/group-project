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

    public ArrayLevelRepresentation(LevelRepresentationParameters parameters) {
    	super(parameters);

        // Initialise the board
        board = new DesignBoard(maxWidth, maxHeight, parameters);
    }

    /**
     * This method clones the current representation.
     *
     * @return The clone
     */
    @Override
    public ArrayLevelRepresentation clone() {
    	ArrayLevelRepresentation clone = (ArrayLevelRepresentation) super.clone();
    	
    	// Copy the board.
    	clone.board = new DesignBoard(board);

        // Copy the parameters
        clone.parameters = new LevelRepresentationParameters(parameters.random,
                parameters.numberOfCandyColours,
                parameters.targetIcingDensity,
                parameters.targetLiquoriceDensity,
                parameters.targetJellyDensity);
    	
    	return clone;
    }

    /**
     * This performs mutation on the representation.
     */
    public void mutate() {
        board.mutateCellType();
    }

    /**
     * Performs crossover in place using the given LevelRepresentation and this.
     * Note that levelRepresenation should be an instance of the same class as this.
     *
     * @param levelRepresentation the level representation to perform crossover with.
     */
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
        design.setNumberOfCandyColours(parameters.numberOfCandyColours);

        return design;
    }

    /**
     * This returns the aesthetic fitness of the board.
     *
     * @return  The fitness
     */
    @Override
    public double getAestheticFitness() {
    	return AestheticChecker.calculateFitness(board);
    }

    /**
     * This returns the constraint fitness of the board.
     *
     * @return The fitness
     */
    @Override
    public double getConstraintFitness() {
        return ConstraintChecker.calculateFitness(board);
    }

    /**
     * This is used for debugging the representation.
     *
     * @return The string representation
     */
    @Override
    public String toString () {
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
    
}
