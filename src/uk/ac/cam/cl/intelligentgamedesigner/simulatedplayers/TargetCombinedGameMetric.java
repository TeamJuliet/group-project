package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class TargetCombinedGameMetric extends GameStateCombinedMetric {
    
    public final TargetGameMetric metric;
    
    public TargetCombinedGameMetric(TargetGameMetric metric) {
        this.metric = metric;
    }

    @Override
    public int compareTo(GameStateCombinedMetric o) {
        TargetCombinedGameMetric other = (TargetCombinedGameMetric) o;
        return metric.compareTo((GameStateMetric) other.metric);
    }

    
}
