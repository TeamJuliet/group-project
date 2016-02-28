package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * Class that is used to provide an immutable view on a GameStateProgress.
 */
public class GameStateProgressView implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * The current score in the game.
     */
    public final int score;
    
    /**
     * The number of jellies remaining to clear.
     */
	public final int jelliesRemaining;
	
	/**
	 * The number of ingredients remaining to pass through the sinks.
	 */
	public final int ingredientsRemaining;
	
	/**
	 * The moves remaining in the game.
	 */
	public final int movesRemaining;
	
	/**
	 * Construct a progress view of the game from the progress.
	 * @param progress
	 */
	public GameStateProgressView(GameStateProgress progress) {
		this.score = progress.getScore();
		this.jelliesRemaining = progress.getJelliesRemaining();
		this.movesRemaining = progress.getMovesRemaining();
		this.ingredientsRemaining = progress.getIngredientsRemaining();
	}
	
    @Override
    public String toString() {
        return new String("Score: " + this.score + "\n" + "Ingredients: " + this.ingredientsRemaining + "\n"
                + "Moves: " + this.movesRemaining + "\n" + "Jellies: " + this.jelliesRemaining + "\n");
    }
}
