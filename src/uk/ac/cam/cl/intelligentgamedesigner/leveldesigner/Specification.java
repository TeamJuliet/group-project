package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;


public class Specification {
       
    private double difficulty;
    private GameMode gameMode;
       
    public Specification(double difficulty, GameMode gameMode) {
    	this.difficulty = difficulty;
    	this.gameMode = gameMode;

        if(difficulty > 0.0 || difficulty < 1.0) throw new IllegalArgumentException();
    }

    public double getDifficulty () {
        return difficulty;
    }

    public GameMode getGameMode () {
        return gameMode;
    }
}