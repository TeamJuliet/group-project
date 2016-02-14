package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class RoundStatistics {
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
