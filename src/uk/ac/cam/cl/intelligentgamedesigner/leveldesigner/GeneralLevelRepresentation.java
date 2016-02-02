package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.Random;

public abstract class GeneralLevelRepresentation extends LevelRepresentation {

	protected Random random;
    protected DesignCellType[][] board;
    protected static int maxWidth = 10;
    protected static int maxHeight = 10;
    protected static int cellModulo = 4;
    protected ArrayList<Integer> parameters;

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

        // Number of moves is initialised in the range: 10-30 (inclusive)
        parameters.add(random.nextInt(21) + 10);

        // Number of candy colours is initialised in the range: 4-6 (inclusive)
        parameters.add(random.nextInt(3) + 4);

        // TODO: The above initialisations are just guesses at the moment. We may need to refine them

        // Initialise the board
        this.board = new DesignCellType[GeneralLevelRepresentation.maxWidth][GeneralLevelRepresentation.maxHeight];
        for (int x = 0; x < GeneralLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < GeneralLevelRepresentation.maxHeight; y++) {
                board[x][y] = random.nextInt(GeneralLevelRepresentation.cellModulo);
            }
        }
    }
    
    /** Creates a Design instance that includes the basic layout of the board, but excludes the parameters specific to
     *  particular game type.
     * 
     */
    protected Design getBaseDesign()
    {
    	Design design = new Design();
        
    	design.setSize(board.getWidth(),board.getHeight());
    
    	Cell[][] designBoard = new Cell[board.getWidth()][board.getHeight()];
    
    	for(int i = 0; i < designBoard.getWidth(); i++)
    	{
    		for(int j = 0; j < designBoard.getHeight(); j++)
        	{
    			switch(board[i][j])
    			{
    			case DesignCellType::UNUSABLE:
    				designBoard[i][j] = new Cell(CellType::UNUSABLE);
    				break;
    			case DesignCellType::EMPTY:
    				designBoard[i][j] = new Cell(CellType::EMPTY);
    				break;
    			case DesignCellType::ICING:
    				designBoard[i][j] = new Cell(CellType::ICING);
    				break;
    			case DesignCellType::LIQUORICE:
    				designBoard[i][j] = new Cell(CellType::LIQUORICE);
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
