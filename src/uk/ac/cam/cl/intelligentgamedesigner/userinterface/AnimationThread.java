package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import javax.swing.SwingWorker;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.ProcessState;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.PropertyChanges;

public class AnimationThread extends SwingWorker{
	
	public static final String NEW_STATE = "new state";
	public static final String FINISHED = "game over";
	
	private GameState theGame;
	private Move move;
	private GameBoard board;
	
	public void initialise(GameState gameState, Move madeMove, GameBoard gameBoard){
		theGame = gameState;
		move = madeMove;
		board = gameBoard;
	}

	@Override
	protected Object doInBackground() throws Exception {

		try {
			theGame.makeMove(move);
			while(theGame.makeSmallMove()) {
				animate(theGame.getCurrentProcessState());
				update();		
			}
			
		} catch (InvalidMoveException ex) {
			System.err.println("Invalid move");
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		endGame();	
			
		return null;
	}

		
	public void animate(ProcessState gameState) throws InterruptedException{
		if(gameState != ProcessState.AWAITING_MOVE)board.doAnimation(gameState);
		board.paintImmediately(0,0,InterfaceManager.screenWidth(),InterfaceManager.screenHeight());
	}

	private void update() {
		firePropertyChange(NEW_STATE, null, theGame);
	}
	
	private void endGame() {
		firePropertyChange(FINISHED, null, theGame);
	}

}
