package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class GameStateMetric implements Comparable<GameStateMetric> {
    public final int metric;

    public GameStateMetric(int score) {
        this.metric = score;
    }

    public GameStateMetric() {
        this(0);
    }

    @Override
    public int compareTo(GameStateMetric arg0) {
        if (arg0 == null)
            return -1;
        if (metric > arg0.metric)
            return 1;
        if (metric < arg0.metric)
            return -1;
        return 0;
    }

    public static GameStateMetric sub(GameStateMetric first, GameStateMetric second) {
        return new GameStateMetric(first.metric - second.metric);

    }

    public static GameStateMetric add(GameStateMetric first, GameStateMetric second) {
        return new GameStateMetric(first.metric + second.metric);

    }
}
