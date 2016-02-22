package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class AdvancedGameMetric extends GameStateMetric{
    private double distance = 0.0; 
    
    @Override
    public int compareTo(GameStateMetric arg0) {
        AdvancedGameMetric metric = (AdvancedGameMetric) arg0;
        if(distance > metric.distance) return 1;
        if(distance < metric.distance) return -1;
        return 0;
    }
}
