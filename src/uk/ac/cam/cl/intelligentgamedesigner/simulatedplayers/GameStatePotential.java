package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

// Class that contains the information after evaluating a state based on
// how promising the current game state is, to reach the target.
// Note: the metric should be comparable, since these objects will be inside
//       a priority queue.
public class GameStatePotential implements Comparable<GameStatePotential>{

	@Override
	public int compareTo(GameStatePotential o) {
		
		return 0;
	}

}
