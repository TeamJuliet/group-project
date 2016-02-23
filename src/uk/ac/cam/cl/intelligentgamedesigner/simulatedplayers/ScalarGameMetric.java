package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class ScalarGameMetric extends GameStateMetric {
    private final double realScore;
    
    @Override
    public int compareTo(GameStateMetric o) {
        ScalarGameMetric metric = (ScalarGameMetric) o; 
        if(realScore > metric.realScore) return 1;
        if(realScore < metric.realScore) return -1;
        return 0;
    }
    
    public ScalarGameMetric(double realScore) {
        this.realScore = realScore;
    }
}
