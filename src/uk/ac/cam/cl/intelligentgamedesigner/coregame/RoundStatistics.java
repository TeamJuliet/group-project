package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Class that contains the statistics for a single move. This class will be used as
 * more detailed feedback from a game played and will be fed to the level designer
 * and the esthetic checker.
 *
 */
public class RoundStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * An immutable version of the progress at the end of the round.
     */
    public final GameStateProgressView progress;
    
    /**
     * A record of the candies that were removed in this round.
     */
    public final CandiesAccumulatorViewer candiesRemoved;
    
    /**
     * A record of the candies that were formed in this round.
     */
    public final CandiesAccumulatorViewer candiesFormed;
    
    /**
     * The statistics for the process state during this round.
     */
    public final ProcessStateStatsViewer processStateStats;
    
    RoundStatistics(GameStateProgress progress, CandiesAccumulator candiesRemoved, CandiesAccumulator candiesFormed,
            ProcessStateStats processStats) {
        this.progress = new GameStateProgressView(progress);
        this.candiesRemoved = candiesRemoved.toViewer();
        this.candiesFormed = candiesFormed.toViewer();
        this.processStateStats = processStats.toViewer();
    }
    
}
