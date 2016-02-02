package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

import java.util.ArrayList;
import java.util.Random;

public abstract class GeneralLevelRepresentation extends LevelRepresentation {

	protected Random random;
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
    

    public GeneralLevelRepresentation(Random random) {
    	super(random);

        parameters = new ArrayList<>();

        // Number of moves is initialised in the range: 10-40 (inclusive)
        parameters.add(new Parameter(random, 10, 40));

        // Number of candy colours is initialised in the range: 4-6 (inclusive)
        parameters.add(new Parameter(random, 4, 6));

        // TODO: The above initialisations are just guesses at the moment. We may need to refine them

        // Initialise the board
        this.board = new DesignCellType[GeneralLevelRepresentation.maxWidth][GeneralLevelRepresentation.maxHeight];
        for (int x = 0; x < GeneralLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < GeneralLevelRepresentation.maxHeight; y++) {
                board[x][y] = DesignCellType.values()[random.nextInt(GeneralLevelRepresentation.cellModulo)];
            }
        }
    }

    public void mutate() {
        if (random.nextDouble() < 0.8) {
            // Mutate the board (80% probability).

            int x = random.nextInt(maxWidth);
            int y = random.nextInt(maxHeight);

            // Make sure the value actually changes.
            int newValue = random.nextInt(cellModulo - 1);
            if (newValue >= board[x][y].ordinal()) {
                newValue++;
            }
            board[x][y] = DesignCellType.values()[newValue];
        } else {
            // Mutate a parameter (20% probability).
            Parameter p = parameters.get(random.nextInt(parameters.size()));
            p.generateNewValue();
        }
    }


    /** Creates a Design instance that includes the basic layout of the board, but excludes the parameters specific to
     *  particular game type.
     * 
     */
    protected Design getBaseDesign()
    {
    	Design design = new Design();
        
    	design.setSize(maxWidth, maxHeight);
    
    	Cell[][] designBoard = new Cell[maxWidth][maxHeight];
    
    	for(int x = 0; x < maxWidth; x++)
    	{
    		for(int y = 0; y < maxHeight; y++)
        	{
    			switch(board[x][y])
    			{
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
}
