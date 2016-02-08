package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.Random;

public abstract class ArrayLevelRepresentation extends LevelRepresentation {
    protected RandomBoard<DesignCellType> board;
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
        board = new RandomBoard<>(maxWidth, maxHeight, random, DesignCellType.values());
    }
    
    @Override
    public ArrayLevelRepresentation clone() {
    	ArrayLevelRepresentation clone = (ArrayLevelRepresentation) super.clone();
    	
    	// Copy the board.
    	clone.board = new RandomBoard<>(board);
    	
    	// Copy the list of parameters.
    	int length = parameters.size();
    	clone.parameters = new ArrayList<>(length);
    	for (int i = 0; i < length; i++) {
    		clone.parameters.add(parameters.get(i).copy());
    	}
    	
    	return clone;
    }

    public void mutate() {
        if (random.nextDouble() < 0.75) {
            // Mutate the board (75% probability).
        	board.mutate();
        } else {
            // Mutate a parameter (25% probability).
            Parameter p = parameters.get(random.nextInt(parameters.size()));
            p.generateNewValue();
        }
    }
    
	@Override
    public void crossoverWith(LevelRepresentation levelRepresentation) {
		ArrayLevelRepresentation l = (ArrayLevelRepresentation) levelRepresentation;
		
		board.crossoverWith(l.board);
		
		for (int i = 0; i < parameters.size(); i++) {
			if (random.nextDouble() > 0.5) { // Swap each parameter with 50% probability.
				Parameter p = parameters.get(i);
				parameters.set(i, l.parameters.get(i));
				l.parameters.set(i, p);
			}
		}
    }
    
    /**
     * Creates a Design instance that includes the basic layout of the board,
     * but excludes the parameters specific to a particular game type.
     */
    @Override
	public Design getDesign() {
        Design design = new Design();

        design.setSize(board.width, board.height);

        Cell[][] designBoard = new Cell[board.width][board.height];

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                switch (board.get(x, y)) {
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
    
    @Override
    public double getAestheticFitness() {
    	return AestheticChecker.calculateFitness(board);
    }
    
    public void printBoard() {
    	String[] r = {"X", " ", "I", "L"};
    	for (int y = 0; y < board.height; y++) {
    		for (int x = 0; x < board.width; x++) {
    			int t = board.get(x, y).ordinal();
    			System.out.print(r[t] + ' ');
    		}
    		System.out.println();
    	}
    }
    
}
