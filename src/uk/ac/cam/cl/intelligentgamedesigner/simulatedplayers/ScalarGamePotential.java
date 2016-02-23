package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class ScalarGamePotential extends GameStatePotential {
    private final double realScore;
    
    @Override
    public int compareTo(GameStatePotential o) {
        ScalarGamePotential metric = (ScalarGamePotential) o; 
        if(realScore > metric.realScore) return 1;
        if(realScore < metric.realScore) return -1;
        return 0;
    }
    
    public ScalarGamePotential(double realScore) {
        this.realScore = realScore;
    }
}
