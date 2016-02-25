package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * 
 * One of the simplest forms of GameStateCombinedMetrics that contains a real
 * scalar and will be used to indicate the distance from the target (i.e. the
 * smaller the better).
 * 
 * Note: see also ScalarGameStateMetric and ScalarGameStatePotential.
 *
 */
public class ScalarCombinedMetric extends GameStateCombinedMetric {

    /**
     * The scalar metric score.
     */
    public final double score;

    public ScalarCombinedMetric(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(GameStateCombinedMetric o) {
        ScalarCombinedMetric other = (ScalarCombinedMetric) o;
        if (score > other.score)
            return 1;
        if (score < other.score)
            return -1;
        return 0;
    }
}
