package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;


public class Specification {
       
    private double targetDifficulty;
    private GameMode gameMode;
    private LevelDesignerAccuracy accuracy;

    public Specification(double targetDifficulty, GameMode gameMode) {
        this (targetDifficulty, gameMode, LevelDesignerAccuracy.MEDIUM);
    }

    public Specification(double targetDifficulty, GameMode gameMode, LevelDesignerAccuracy accuracy) {
    	if (targetDifficulty < 0.0 || targetDifficulty > 1.0) throw new IllegalArgumentException();
    	
    	this.targetDifficulty = targetDifficulty;
    	this.gameMode = gameMode;
        this.accuracy = accuracy;
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
}