package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * 
 * One of the simplest forms of GameStatePotentials that contains a real scalar
 * and will be used to indicate the distance from the target (i.e. the smaller
 * the better).
 * 
 * It is similar to the ScalarGamePotential and can be combined to form the
 * ScalarCombinedMetric using addition.
 *
 */
public class ScalarGamePotential extends GameStatePotential {
    /**
     * The real scalar assigned to the potential of the game state.
     */
    public final double score;

    @Override
    public int compareTo(GameStatePotential o) {
        ScalarGamePotential metric = (ScalarGamePotential) o;
        if (score > metric.score)
            return 1;
        if (score < metric.score)
            return -1;
        return 0;
    }

    /**
     * Initialisation using the scalar score potential of the game state.
     * 
     * @param realScore
     *            The scalar real score assigned to it.
     */
    public ScalarGamePotential(double realScore) {
        this.score = realScore;
    }
}
