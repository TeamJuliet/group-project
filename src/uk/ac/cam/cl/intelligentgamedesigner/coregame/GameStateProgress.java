package uk.ac.cam.cl.intelligentgamedesigner.coregame;

// Class that contains the progress of the game state (score, ingredients and jellies remaining).
public class GameStateProgress {
    private int score;
    private int jelliesRemaining;
    private int ingredientsRemaining;
    private int movesRemaining;

    public GameStateProgress(int score, int jellies, int ingredients, int moves) {
        this.score = score;
        this.jelliesRemaining = jellies;
        this.ingredientsRemaining = ingredients;
        this.movesRemaining = moves;
    }

    public GameStateProgress(GameStateProgress progress) {
        this.score = progress.score;
        this.jelliesRemaining = progress.jelliesRemaining;
        this.ingredientsRemaining = progress.ingredientsRemaining;
        this.movesRemaining = progress.movesRemaining;
    }

    public GameStateProgress(Design design) {
        this.movesRemaining = design.getNumberOfMovesAvailable();
        if (design.getMode() == GameMode.INGREDIENTS) {
            this.ingredientsRemaining = design.getObjectiveTarget();
        } else
            this.ingredientsRemaining = 0;

        if (design.getMode() == GameMode.JELLY) {
            this.jelliesRemaining = design.getObjectiveTarget();
        } else
            this.jelliesRemaining = 0;
    }

    public boolean isGameOver(Design design) {
        return movesRemaining == 0 || isGameWon(design);
    }

    public boolean isGameWon(Design design) {
        GameMode gameMode = design.getMode();
        return (gameMode.equals(GameMode.JELLY) && jelliesRemaining == 0)
                || (gameMode.equals(GameMode.HIGHSCORE) && score >= design.getObjectiveTarget())
                || (gameMode.equals(GameMode.INGREDIENTS) && ingredientsRemaining == 0);
    }

    public int getScore() {
        return score;
    }

    public int getJelliesRemaining() {
        return jelliesRemaining;
    }

    public int getIngredientsRemaining() {
        return ingredientsRemaining;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public void resetScore() {
        score = 0;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public void decreaseJelliesRemaining() {
        --jelliesRemaining;
    }

    public void decreaseMovesRemaining() {
        --movesRemaining;
    }

    public void decreaseIngredientsRemaining() {
        --ingredientsRemaining;
    }

    @Override
    public String toString() {
        return new String("Score: " + this.score + "\n" + "Ingredients: " + this.ingredientsRemaining + "\n"
                + "Moves: " + this.movesRemaining + "\n" + "Jellies: " + this.jelliesRemaining + "\n");
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
