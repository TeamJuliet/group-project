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
    
    public final GameStateProgressView progress;
    public final CandiesAccumulatorViewer candiesRemoved;
    public final CandiesAccumulatorViewer candiesFormed;
    public final ProcessStateStatsViewer processStateStats;
    
    RoundStatistics(GameStateProgress progress, CandiesAccumulator candiesRemoved, CandiesAccumulator candiesFormed,
            ProcessStateStats processStats) {
        this.progress = new GameStateProgressView(progress);
        this.candiesRemoved = candiesRemoved.toViewer();
        this.candiesFormed = candiesFormed.toViewer();
        this.processStateStats = processStats.toViewer();
    }
    
}
