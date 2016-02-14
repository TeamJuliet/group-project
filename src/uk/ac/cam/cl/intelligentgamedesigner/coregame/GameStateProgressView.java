package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Class that is used to provide an immutable view on a GameStateProgress.
 */
public class GameStateProgressView {
	public final int score;
	public final int jelliesRemaining;
	public final int ingredientsRemaining;
	public final int movesRemaining;
	
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
