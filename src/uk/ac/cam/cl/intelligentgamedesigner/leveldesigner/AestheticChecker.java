package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class AestheticChecker {
	
	public static double calculateFitness(RandomBoard<DesignCellType> board) {
		double fitness = calculateSymmetryScore(board);
		
		return fitness;
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
