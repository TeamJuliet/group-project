package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;


public class Specification {
       
    private double targetDifficulty;
    private GameMode gameMode;
       
    public Specification(double targetDifficulty, GameMode gameMode) {
    	if (targetDifficulty < 0.0 || targetDifficulty > 1.0) throw new IllegalArgumentException();
    	
    	this.targetDifficulty = targetDifficulty;
    	this.gameMode = gameMode;
    }

    public double getTargetDifficulty () {
        return targetDifficulty;
    }

    public GameMode getGameMode () {
        return gameMode;
    }
}