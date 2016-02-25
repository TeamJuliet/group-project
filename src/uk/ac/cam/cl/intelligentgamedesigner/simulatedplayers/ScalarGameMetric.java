package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * 
 * One of the simplest forms of GameStateMetrics that contains a real scalar and
 * will be used to indicate the distance from the target (i.e. the smaller the
 * better).
 *
 */
public class ScalarGameMetric extends GameStateMetric {
    /**
     * The score that is given for this configuration.
     */
    public final double score;

    @Override
    public int compareTo(GameStateMetric o) {
        ScalarGameMetric metric = (ScalarGameMetric) o;
        if (score > metric.score)
            return 1;
        if (score < metric.score)
            return -1;
        return 0;
    }

    /**
     * This should be initialized with the real score that the game state has
     * been assigned with.
     * 
     * @param realScore
     *            The real scalar score given to it.
     */
    public ScalarGameMetric(double realScore) {
        this.score = realScore;
    }
}
