package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import javax.swing.SwingWorker;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.ProcessState;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.PropertyChanges;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

public class AnimationThread extends SwingWorker{
	
	public static final String NEW_STATE = "new state";
	public static final String FINISHED = "game over";
	
	private GameState theGame;
	private Move move;
	private GameBoard board;
	
	public AnimationThread(GameState gameState, Move madeMove, GameBoard gameBoard){
		theGame = gameState;
		move = madeMove;
		board = gameBoard;
	}

	@Override
	protected Object doInBackground() throws Exception {

		try {
			theGame.makeInitialMove(move);
			while(theGame.makeSmallMove()) {
				animate(theGame.getCurrentProcessState());
				update();		
			}
			
		} catch (InvalidMoveException ex) {
			DebugFilter.println("Invalid move",DebugFilterKey.GAME_IMPLEMENTATION);
		}catch (InterruptedException e) {
			DebugFilter.println("Stopped the animation", DebugFilterKey.USER_INTERFACE);
		}
		endGame();	
			
		return null;
	}

		
	public void animate(ProcessState gameState) throws InterruptedException{
		if(gameState != ProcessState.AWAITING_MOVE)board.doAnimation(gameState);
		Thread.sleep(200);
	}

	private void update() {
		firePropertyChange(NEW_STATE, null, theGame);
	}
	
	private void endGame() {
		firePropertyChange(FINISHED, null, theGame);
	}

}
