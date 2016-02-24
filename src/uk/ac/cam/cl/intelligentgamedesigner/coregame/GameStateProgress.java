package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * Class that contains the progress of the game state (score, ingredients and
 * jellies remaining).
 *
 */
public class GameStateProgress {
    private int     score;
    private int     jelliesRemaining;
    private int     ingredientsRemaining;
    private int     movesRemaining;
    private boolean didFailShuffle = false;

    public GameStateProgress(int score, int jellies, int ingredients, int moves) {
        this.score = score;
        this.jelliesRemaining = jellies;
        this.ingredientsRemaining = ingredients;
        this.movesRemaining = moves;
    }

    /**
     * Copy constructor.
     * 
     * @param progress
     *            The progress to be copied.
     */
    public GameStateProgress(GameStateProgress progress) {
        this.score = progress.score;
        this.jelliesRemaining = progress.jelliesRemaining;
        this.ingredientsRemaining = progress.ingredientsRemaining;
        this.movesRemaining = progress.movesRemaining;
        this.didFailShuffle = progress.didFailShuffle;
    }

    /**
     * Create the initial game progress for the board by providing the Design of
     * the level.
     * 
     * @param design
     *            The design for the game level.
     */
    public GameStateProgress(Design design) {
        this.movesRemaining = design.getNumberOfMovesAvailable();

        if (design.getMode() == GameMode.INGREDIENTS) {
            this.ingredientsRemaining = design.getObjectiveTarget();
        } else
            this.ingredientsRemaining = 0;

        this.jelliesRemaining = 0;
        if (design.getMode() == GameMode.JELLY) {
            for (int x = 0; x < design.getWidth(); x++) {
                for (int y = 0; y < design.getHeight(); y++) {
                    this.jelliesRemaining += design.getBoard()[x][y].getJellyLevel();
                }
            }
        }

        this.score = 0;
    }

    /**
     * Function that returns whether the game is over based on the level design
     * given or a state without valid design was reached.
     * 
     * @param design
     *            The design to specify the game mode and the game target.
     * @return Whether the game is over based on the design.
     */
    public boolean isGameOver(Design design) {
        return movesRemaining == 0 || isGameWon(design) || didFailShuffle;
    }

    /**
     * Function that returns whether the game was won based on the design given.
     * 
     * @param design
     *            The design to specify the game mode and the game target.
     * @return Whether the game was won given the design of the level.
     */
    public boolean isGameWon(Design design) {
        GameMode gameMode = design.getMode();
        return (gameMode.equals(GameMode.JELLY) && jelliesRemaining == 0)
                || (gameMode.equals(GameMode.HIGHSCORE) && score >= design.getObjectiveTarget())
                || (gameMode.equals(GameMode.INGREDIENTS) && ingredientsRemaining == 0);
    }

    /**
     * Function that returns whether the shuffling phase failed.
     * 
     * @return Whether the shuffling phase failed.
     */
    public boolean didFailShuffle() {
        return didFailShuffle;
    }

    /**
     * Function that returns the score that has been reached.
     * 
     * @return The score currently reached.
     */
    public int getScore() {
        return score;
    }

    /**
     * Function that returns the number of jellies to be cleared remaining on
     * the board.
     * 
     * @return The number of jellies remaining uncleared.
     */
    public int getJelliesRemaining() {
        return jelliesRemaining;
    }

    /**
     * Function that returns the ingredients remaining to be passed.
     * 
     * @return The number of ingredients remaining.
     */
    public int getIngredientsRemaining() {
        return ingredientsRemaining;
    }

    /**
     * Function that returns the moves remaining.
     * 
     * @return The moves that remain in the game.
     */
    public int getMovesRemaining() {
        return movesRemaining;
    }

    /**
     * Function that resets the score for the game.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Function that increments the score by the amount specified.
     * 
     * @param amount
     *            The amount to be added to the score.
     */
    public void incrementScore(int amount) {
        score += amount;
    }

    /**
     * Function that decrements the jellies remaining.
     */
    public void decreaseJelliesRemaining() {
        --jelliesRemaining;
    }

    /**
     * Function to decrement the moves remaining in the game.
     */
    public void decreaseMovesRemaining() {
        --movesRemaining;
    }

    /**
     * Function to decrease the ingredients remaining.
     */
    public void decreaseIngredientsRemaining() {
        --ingredientsRemaining;
    }

    /**
     * Function that sets if the shuffling phase was not able to find a shuffle
     * with a move.
     */
    public void setDidFailShuffle() {
        didFailShuffle = true;
    }

    @Override
    public String toString() {
        return new String("Score: " + this.score + "\n" + "Ingredients: " + this.ingredientsRemaining + "\n" + "Moves: "
                + this.movesRemaining + "\n" + "Jellies: " + this.jelliesRemaining + "\n");
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof GameStateProgress) {
            GameStateProgress progress = (GameStateProgress) toCompare;
            return movesRemaining == progress.movesRemaining && ingredientsRemaining == progress.ingredientsRemaining
                    && score == progress.score && jelliesRemaining == progress.jelliesRemaining;
        }
        GameStateProgressView progress = (GameStateProgressView) toCompare;
        return movesRemaining == progress.movesRemaining && ingredientsRemaining == progress.ingredientsRemaining
                && score == progress.score && jelliesRemaining == progress.jelliesRemaining;
    }
}
