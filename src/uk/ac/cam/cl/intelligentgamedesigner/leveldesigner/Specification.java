package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;


public class Specification {
       
    private double difficulty;
    private GameMode gameMode;
       
    public Specification(double difficulty, GameMode gameMode) {
    	if (difficulty < 0.0 || difficulty > 1.0) throw new IllegalArgumentException();
    	
    	this.difficulty = difficulty;
    	this.gameMode = gameMode;
    }

    public double getDifficulty () {
        return difficulty;
    }

    public GameMode getGameMode () {
        return gameMode;
    }
}