package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * Class that contains the information after evaluating a state based on how
 * promising the current game state is, to reach the target.
 * 
 * Note: the metric should be comparable, since these objects will be inside a
 * priority queue.
 *
 */
public class GameStatePotential implements Comparable<GameStatePotential> {
    public final int potential;

    public GameStatePotential(GameStateMetric m) {
        potential = m.metric;
    }

    public GameStatePotential(int p) {
        potential = p;
    }

    public GameStatePotential() {
        this(0);
    }

    @Override
    public int compareTo(GameStatePotential o) {
        if (potential > o.potential)
            return 1;
        if (potential < o.potential)
            return -1;
        return 0;
    }

}
