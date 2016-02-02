package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.Random;

public abstract class GeneralLevelRepresentation extends LevelRepresentation {

	protected Random random;
    protected int[][] board;
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
        this.board = new int[GeneralLevelRepresentation.maxWidth][GeneralLevelRepresentation.maxHeight];
        for (int x = 0; x < GeneralLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < GeneralLevelRepresentation.maxHeight; y++) {
                board[x][y] = random.nextInt(GeneralLevelRepresentation.cellModulo);
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
    		if (newValue >= board[x][y]) {
    			newValue++;
    		}
    		board[x][y] = newValue;
    	} else {
    		// Mutate a parameter (20% probability).
    		Parameter p = parameters.get(random.nextInt(parameters.size()));
    		p.generateNewValue();
    	}
    }
}
