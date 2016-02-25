package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class AestheticChecker {
	
	public static double calculateFitness(DesignBoard board) {
		double fitness = calculateSymmetryScore(board);
		
		return 0.1 + ( fitness * calculateDistributionScore(board) * calculateCentralDistance(board) * calculateConnectedFitness(board) );
	}
	
	private static double calculateDistributionScore(DesignBoard board) {
		int[] counts = new int[DesignCellType.values().length];
		int cellCount = board.height * board.width;
		
		for (int x = 0; x < board.width; x++) {
    		for (int y = 0; y < board.height; y++) {
    			DesignCellType type = board.get(x, y).getDesignCellType();
    			counts[type.ordinal()]++;
    		}
    	}
		
		double[][] distributionDesired = new double[counts.length][2];
		
		distributionDesired[0][0] = 0.2;
		distributionDesired[0][1] = 0.7;
		
		distributionDesired[1][0] = 0.4;
		distributionDesired[1][1] = 1.0;
		
		distributionDesired[2][0] = 0.01;
		distributionDesired[2][1] = 0.01;
	
		distributionDesired[3][0] = 0.01;
		distributionDesired[3][1] = 0.01;
		
		double[] distributions = new double[counts.length];
		for (int i = 0; i < counts.length; i++) {
			distributions[i] = counts[i] / (double) cellCount;
		}
		
		double score = 1.0;
		
		for(int c = 0; c < distributionDesired.length; c++)
		{
			double diff = 0.0;
			
			if(distributions[c] < distributionDesired[c][0])
			{
				diff = (distributionDesired[c][0] - distributions[c]) / distributionDesired[c][0];  
			}else
			if(distributions[c] > distributionDesired[c][1])
			{
				diff = (distributions[c] - distributionDesired[c][1]) / (1.0 - distributionDesired[c][1]);  
			}
			
			score -= diff / (counts.length);
		}
		
		return score;
	}
	
	private static double calculateSymmetryScore(DesignBoard board) {
		int maxX = board.width / 2;
    	int score = 0;
    	for (int x = 0; x < maxX; x++) {
    		for (int y = 0; y < board.height; y++) {
    			DesignCellType t1 = board.get(x, y).getDesignCellType();
    			DesignCellType t2 = board.get(board.width - x - 1, y).getDesignCellType();
    			if (t1 == t2) {
    				score++;
    			}
    		}
    	}
    	return score / (double) (maxX * board.height);
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
	
}
