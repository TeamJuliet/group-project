package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class AestheticChecker {
	
	public static double calculateFitness(DesignBoard board) {
		double fitness = calculateSymmetryScore(board);

		return 0.1 + ( fitness * calculateCentralDistance(board) * calculateConnectedFitness(board) );
	}
	
	private static double calculateSymmetryScore(DesignBoard board) {
		
    	int vertScore = 0;
    	for (int x = 0; x < (board.width / 2); x++) {
    		for (int y = 0; y < board.height; y++) {
    			DesignCellType t1 = board.get(x, y).getDesignCellType();
    			DesignCellType t2 = board.get(board.width - x - 1, y).getDesignCellType();
    			if (t1 == t2) {
    				vertScore++;
    			}
    		}
    	}
    	
    	double vert = 1.05 * Math.pow(vertScore / (double) ((board.width * board.height) / 2),2);
    	
    	/*int diagScore = 0;
    	for(int j = 0; j < board.height; j++)
    	{
	    	for(int i = 0; i < j; i++) {
	    		DesignCellType t1 = board.get(i, j).getDesignCellType();
    			DesignCellType t2 = board.get(j, i).getDesignCellType();
    			if (t1 == t2) {
    				diagScore++;
    			}
	    	}
    	}
    	
    	double diag = Math.pow(diagScore / (double) ((board.width * board.height) / 2),2);
    	
    	int invertDiagScore = 0;
    	for(int j = 0; j < board.height; j++)
    	{
	    	for(int i = 0; i < board.height - (j + 1); i++) {
	    		DesignCellType t1 = board.get(i, j).getDesignCellType();
    			DesignCellType t2 = board.get(board.height - (j + 1), board.width - (i + 1)).getDesignCellType();
    			if (t1 == t2) {
    				invertDiagScore++;
    			}
	    	}
    	}
    	
    	
    	double invertDiag = Math.pow(invertDiagScore / (double) ((board.width * board.height) / 2),2);
    	
    	if(invertDiag > 0.3 && diag > 0.3) {
    		return invertDiag * diag;
    	}
    	
    	return Math.max(Math.max(vert, diag),invertDiag);*/
    	
    	return vert;
	}
	
	private static double calculateCentralDistance(DesignBoard board)
	{
		int x_position = 0;
		int y_position = 0;
		
		int emptyCells = 0;
		
		for(int i = 0; i < board.height; i++)
		{
			for(int j = 0; j < board.width; j++)
			{
				if(board.get(i, j).getDesignCellType() == DesignCellType.EMPTY)
				{
					x_position += i;
					y_position += j;
					emptyCells++;
				}
			}
		}
		
		if (emptyCells == 0) {
			return 0.0;
		}
		
		return Math.pow((Math.sqrt(8.0) - Math.sqrt(Math.pow(Math.floor((double)board.width / 2.0) - (x_position / emptyCells), 2.0) + Math.pow(Math.floor((double)board.height / 2.0) - (y_position / emptyCells), 2.0))) / Math.sqrt(8.0), 2.0);
	}
	
	
	private static double calculateConnectedFitness(DesignBoard board)
	{
		int meh = 0;
		
		for(int i = 1; i < board.height - 1; i++)
		{
			for(int j = 1; j < board.width - 1; j++)
			{
				DesignCellType t = board.get(i, j).getDesignCellType(); 
				
				if(t != board.get(i, j + 1).getDesignCellType())
				{
					meh++;
				}
							
				if(t != board.get(i + 1, j).getDesignCellType())
				{
					meh++;
				}
							
				if(t != board.get(i, j - 1).getDesignCellType())
				{
					meh++;
				}
							
				if(t != board.get(i - 1, j).getDesignCellType())
				{
					meh++;
				}
			}
		}
		
		return 1.0 - ((double)meh / (4 * board.width * board.height));
	}

	public static double calculateJellyFitness(DesignBoard board) {
		return calculateJellyNonIsolationFitness(board) * calculateJellySymmetryFitness(board);
	}

	public static double calculateJellySymmetryFitness(DesignBoard board) {
		int maxX = board.width / 2;
    	int score = 0;
    	for (int x = 0; x < maxX; x++) {
    		for (int y = 0; y < board.height; y++) {
    			int j1 = board.get(x, y).getJellyLevel();
    			int j2 = board.get(board.width - x - 1, y).getJellyLevel();
    			if (j1 == j2) {
    				score++;
    			}
    		}
    	}
    	return score / (double) (maxX * board.height);
	}

	public static double calculateJellyNonIsolationFitness (DesignBoard board) {
		int totalJelly = 0;
		int totalIsolatedJelly = 0;
		for (int x = 1; x < board.width - 1; x++) {
			for (int y = 1; y < board.height - 1; y++) {
				if (board.get(x, y).getJellyLevel() > 0) {
					totalJelly++;

					if (board.get(x - 1, y).getJellyLevel() == 0
							&& board.get(x, y - 1).getJellyLevel() == 0
							&& board.get(x + 1, y).getJellyLevel() == 0
							&& board.get(x, y + 1).getJellyLevel() == 0) totalIsolatedJelly++;
				}
			}
		}
		double proportionIsolated = totalIsolatedJelly / (double) totalJelly;

		return Math.exp(-proportionIsolated);
	}
	
}
