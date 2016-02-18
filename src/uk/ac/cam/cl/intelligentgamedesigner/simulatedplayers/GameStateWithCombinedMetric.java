package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class GameStateWithCombinedMetric implements Comparable<GameStateWithCombinedMetric>{
	public final GameState gameState;
	public final GameStateCombinedMetric gameStateMetric;
	public final Move originalMove;
	
	public GameStateWithCombinedMetric(GameState gameState,
			GameStateCombinedMetric gameStateMetric, Move originalMove) {
		this.gameState = new GameState(gameState);
		this.gameStateMetric = gameStateMetric;
		this.originalMove = originalMove;
	}
	
	@Override
	public int compareTo(GameStateWithCombinedMetric o){
	    return gameStateMetric.compareTo(o.gameStateMetric);
	}

}
