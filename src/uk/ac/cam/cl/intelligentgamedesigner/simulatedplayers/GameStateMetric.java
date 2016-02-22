package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

public class GameStateMetric implements Comparable<GameStateMetric> {
    public final int score;
    
    public GameStateMetric(int score){
        this.score = score;
    }
    
    public GameStateMetric() {
        this(0);
    }
    
	@Override
	public int compareTo(GameStateMetric arg0) {
		if(score > arg0.score) return 1;
		if(score < arg0.score) return -1;
		return 0;
	}
	
}
