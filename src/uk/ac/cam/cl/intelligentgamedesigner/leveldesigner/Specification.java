package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;


public class Specification {
       
    private double targetDifficulty;
    private GameMode gameMode;
    private LevelDesignerAccuracy accuracy;
    
    //extra parameters
    private int //integer bounds for designed levels
    	max_moves, 
    	min_moves,
    	max_candies,
    	min_candies;
    private double //arbitrary value from 0-1 of how many of each should be in the design
    	jelly_density,
    	icing_density,
    	lock_density;
    	
    //the new constructor, with the added specifications
    public Specification(double targetDifficulty,
                         GameMode gameMode,
                         LevelDesignerAccuracy accuracy,
                         int max_moves,
                         int min_moves,
                         int max_candies,
                         int min_candies,
                         double jelly_density,
                         double icing_density,
                         double lock_density) {
    	this.targetDifficulty = targetDifficulty;
    	this.gameMode = gameMode;
        this.accuracy = accuracy;
        this.max_moves = max_moves; 
        this.min_moves = min_moves;
        this.max_candies = max_candies;
        this.min_candies = min_candies;
        this.jelly_density = jelly_density;
        this.icing_density = icing_density;
        this.lock_density = lock_density;
    	if (targetDifficulty < 0.0 || targetDifficulty > 1.0) throw new IllegalArgumentException();
    }

    public double getTargetDifficulty () {
        return targetDifficulty;
    }

    public GameMode getGameMode () {
        return gameMode;
    }

    public LevelDesignerAccuracy getAccuracy () {
        return accuracy;
    }
    
    //the added getters
    public int getMaxMoves(){
    	return max_moves;
    }
    public int getMinMoves(){
    	return min_moves;
    }
    public int getMaxCandies(){
    	return max_candies;
    }
    public int getMinCandies(){
    	return min_candies;
    }
    public double getDesiredJelly(){
    	return jelly_density;
    }
    public double getDesiredLiquorice(){
    	return lock_density;
    }
    public double getDesiredIcing(){
    	return icing_density;
    }
}