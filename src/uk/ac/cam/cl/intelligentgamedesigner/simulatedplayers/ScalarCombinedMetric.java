package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class ScalarCombinedMetric extends GameStateCombinedMetric {
    
    public final double realScore;
    
    public ScalarCombinedMetric(double score) {
        this.realScore = score;
    }
    
    @Override
    public int compareTo(GameStateCombinedMetric o) {
        ScalarCombinedMetric other = (ScalarCombinedMetric) o;
        if(realScore > other.realScore) return 1;
        if(realScore < other.realScore) return -1;
        return 0;
    }
}
