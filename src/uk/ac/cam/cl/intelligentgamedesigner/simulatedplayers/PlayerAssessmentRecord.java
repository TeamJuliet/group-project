package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.io.Serializable;

/**
 * 
 * The assessment record for a player on a single level.
 *
 */
public class PlayerAssessmentRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Total number of games won out of the games played.
     */
    public final int          numOfGamesWon;

    /**
     * Total number of games lost out of the games played.
     */
    public final int          numOfGamesLost;

    /**
     * A valuation for the score that is comparable to the moves remaining.
     */
    public final double       normalizedAverageScore;
    /**
     * The average remaining moves in the games that were lost.
     */
    public final double       averageRemainingMoves;

    /**
     * The number of games won over the total number of games played.
     * 
     * @return
     */
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
