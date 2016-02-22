package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class TargetGameMetric extends GameStateMetric {
        
    private double distance = 0.0;
    
    public TargetGameMetric(double dist) {
        this.distance = dist;
    }
    
    @Override
    public int compareTo(GameStateMetric arg0) {
        TargetGameMetric metric = (TargetGameMetric) arg0;
        if(distance > metric.distance) return 1;
        if(distance < metric.distance) return -1;
        return 0;
    }
}
