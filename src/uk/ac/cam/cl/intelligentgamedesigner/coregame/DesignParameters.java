package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class DesignParameters implements Serializable {

    private int numberOfCandyColours;
    private GameMode gameMode;
    private int numberOfIngredients;        // If the game mode is INGREDIENTS, the generator needs to know how
                                            // many ingredients to generate over the course of the level

    public DesignParameters () {
        this(6, GameMode.HIGHSCORE, 0);
    }

    public DesignParameters (int numberOfCandyColours, GameMode gameMode, int numberOfIngredients) {
        this.numberOfCandyColours = numberOfCandyColours;
        this.gameMode = gameMode;
        this.numberOfIngredients = numberOfIngredients;
    }

    public int getNumberOfCandyColours () {
        return numberOfCandyColours;
    }

    public GameMode getGameMode () {
        return gameMode;
    }

    public int getNumberOfIngredients () {
        return numberOfIngredients;
    }
}
