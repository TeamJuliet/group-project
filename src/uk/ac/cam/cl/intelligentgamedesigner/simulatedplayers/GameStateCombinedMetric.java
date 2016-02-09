package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

// Class that contains the combined evaluation for the current form of the gameState
// and its potential to reach the required target.
public class GameStateCombinedMetric implements Comparable<GameStateCombinedMetric> {
    //Not sure if we need metric and potential to be stored here as well
    public final GameStateMetric metric;
    public final GameStatePotential potential;
    public final int value;
    
    public GameStateCombinedMetric(GameStateMetric m, GameStatePotential p, int v) {
        metric = m;
        potential = p;
        value = v;
    }
    
    public GameStateCombinedMetric() {
        this(new GameStateMetric(), new GameStatePotential(), 0);
    }
	@Override
	public int compareTo(GameStateCombinedMetric o) {
		if(value > o.value) return 1;
		if(value < o.value) return -1;
		return 0;
	}

}
