package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class AestheticChecker {
	
	public static double calculateFitness(RandomBoard<DesignCellType> board) {
		double fitness = calculateSymmetryScore(board);
		
		return fitness * calculateDistributionScore(board);
	}
	
	private static double calculateDistributionScore(RandomBoard<DesignCellType> board) {
		int[] counts = new int[DesignCellType.values().length];
		int usableCount = 0;
		
		for (int x = 0; x < board.width; x++) {
    		for (int y = 0; y < board.height; y++) {
    			DesignCellType type = board.get(x, y);
    			counts[type.ordinal()]++;
    			if (type != DesignCellType.UNUSABLE) {
    				usableCount++;
    			}
    		}
    	}
		
		double[] distributions = new double[counts.length];
		for (int i = 0; i < counts.length; i++) {
			distributions[i] = counts[i] / (double) usableCount;
		}
		
		double score = 1.0;
		
		// Check icing distribution.
		double icingPercentage = distributions[DesignCellType.ICING.ordinal()];
		if (icingPercentage > 0.2) {
			score -= 0.1;
		}
		
		// Check icing distribution.
		double liquoricePercentage = distributions[DesignCellType.LIQUORICE.ordinal()];
		if (liquoricePercentage > 0.2) {
			score -= 0.1;
		}
		
		return score;
	}
	
	private static double calculateSymmetryScore(RandomBoard<DesignCellType> board) {
		int maxX = board.width / 2;
    	int score = 0;
    	for (int x = 0; x < maxX; x++) {
    		for (int y = 0; y < board.height; y++) {
    			if (board.get(x, y) == board.get(board.width - x - 1, y)) {
    				score++;
    			}
    		}
    	}
    	
    	double perfectScore = maxX * board.height;
    	return score / perfectScore;
	}
	
}
