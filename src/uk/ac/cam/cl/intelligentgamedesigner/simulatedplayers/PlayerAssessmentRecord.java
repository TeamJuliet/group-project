package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class PlayerAssessmentRecord {
    public final int    numOfGamesWon;
    public final int    numOfGamesLost;
    public final double normalizedAverageScore;
    public final double averageRemainingMoves;

    public double getAverageGamesWon() {
        return ((double) this.numOfGamesWon) / (this.numOfGamesWon + (double) this.numOfGamesLost);
    }

    public PlayerAssessmentRecord(int numOfGamesWon, int numOfGamesLost, double normalizedAverageScore,
            double averageRemainingMoves) {
        this.numOfGamesWon = numOfGamesWon;
        this.numOfGamesLost = numOfGamesLost;
        this.normalizedAverageScore = normalizedAverageScore;
        this.averageRemainingMoves = averageRemainingMoves;
    }

}
