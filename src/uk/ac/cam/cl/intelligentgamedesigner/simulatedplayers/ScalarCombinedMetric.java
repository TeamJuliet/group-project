package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class ScalarCombinedMetric extends GameStateCombinedMetric {
    
    public final double score;
    
    public ScalarCombinedMetric(double score) {
        this.score = score;
    }
    
    @Override
    public int compareTo(GameStateCombinedMetric o) {
        ScalarCombinedMetric other = (ScalarCombinedMetric) o;
        if(score > other.score) return 1;
        if(score < other.score) return -1;
        return 0;
    }
}
