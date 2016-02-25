package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;

import javax.swing.SwingWorker;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.ProcessState;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

public class AnimationThread extends SwingWorker{
	
	public static final String NEW_STATE = "new state";
	public static final String FINISHED = "game over";
	
	//TODO: animations, make cell objects for drawing purposes?
	public static final int SWAP_SPEED = 15;
	public static final int FALL_SPEED = 10;
	public static final double DISTANCE_PER_SLEEP = 0.1;//in fractions of a tilesize
	
	private GameState theGame;
	private Move move;
	private GameBoard board;
	private boolean animated;
	
	public AnimationThread(GameState gameState, Move madeMove, GameBoard gameBoard, boolean shows_animation){
		theGame = gameState;
		move = madeMove;
		board = gameBoard;
		animated = shows_animation;
	}

	@Override
	protected Object doInBackground() throws Exception {
		try {
			if(animated){
				board.setAnimating(true);
				Cell[][] old_game = theGame.getBoard().clone();
				theGame.makeInitialMove(move);
				animate(ProcessState.MAKING_SWAP,old_game,new Object[]{move});
				old_game = theGame.getBoard().clone();
				while(theGame.makeSmallMove()) {
					animate(theGame.getCurrentProcessState(),old_game,null);
					update();
					old_game = theGame.getBoard().clone();	
				}
				board.setAnimating(false);
			} else {
				theGame.makeFullMove(move);
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

		
	public void animate(ProcessState gameState, Cell[][] old_game, Object[] inputs) throws InterruptedException{
		try
		{
			switch(gameState){
//			case MATCH_AND_REPLACE:// Stage where matches occur and the cells that matched are emptied.
//				break;
//			case SHUFFLE:// The board is shuffled if there are no moves to be made, until there are.
//				break;
//			case DETONATE_PENDING:// Detonate the special candies that were triggered in the first stage.
//				break;
//			case BRING_DOWN_CANDIES:// Bring down the candies to the fillable positions on the board.
//				break;
//			case PASSING_INGREDIENTS:// Pass the ingredients that can be passed through the ingredient sinks.
//				break;
//			case FILL_BOARD:// Fill the empty cells on the board that are not blocked by blocker candies.
//		    	break;
//			case AWAITING_MOVE:// The game state is awaiting a move.
//				break;
			case MAKING_SWAP:// two candies are swapped
				Move move = (Move)inputs[0];
				Dimension[][] candy_offsets = new Dimension[old_game.length][old_game[0].length];
//				//determine 
//				int x1,x2,y1,y2;
//				if(move.p1.x<move.p2.x){
//					x1 = move.p1.x;
//					x2 = move.p2.x;
//					y1 = move.p1.y;
//					y2 = move.p2.y;
//				} else {
//					x1 = move.p1.x;
//					x2 = move.p2.x;
//				}
				candy_offsets[move.p1.x][move.p1.y] = new Dimension(
						board.tile_size * (move.p2.x - move.p1.x),board.tile_size * (move.p2.y - move.p1.y));
				candy_offsets[move.p2.x][move.p2.y] = new Dimension(
						board.tile_size * (move.p1.x - move.p2.x),board.tile_size * (move.p1.y - move.p2.y));
				for(int n=0;n<(int)(1.0/DISTANCE_PER_SLEEP);n++){
					board.setOffsets(candy_offsets);
					board.repaint();
					Thread.sleep(SWAP_SPEED);
					decrement(candy_offsets,DISTANCE_PER_SLEEP);
				}
				board.clear_offsets();
				board.setBoard(theGame.getBoard());
				break;
				default:
					board.clear_offsets();
					board.setBoard(theGame.getBoard());
					Thread.sleep(100);
					break;
			}
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	private void decrement(Dimension[][] offsets, double increment){
		for(int x=0;x<offsets.length;x++){
			for(int y=0;y<offsets[0].length;y++){
				if(offsets[x][y] != null){
					if(offsets[x][y].width<0){
						offsets[x][y].width += increment*board.tile_size;
						if(offsets[x][y].width>=0)offsets[x][y].width = 0;
					}
					if(offsets[x][y].width>0){
						offsets[x][y].width -= increment*board.tile_size;
						if(offsets[x][y].width<=0)offsets[x][y].width = 0;
					}
					if(offsets[x][y].height<0){
						offsets[x][y].height += increment*board.tile_size;
						if(offsets[x][y].height>=0)offsets[x][y].height = 0;
					}
					if(offsets[x][y].height>0){
						offsets[x][y].height -= increment*board.tile_size;
						if(offsets[x][y].height<=0)offsets[x][y].height = 0;
					}
				}
			}
		}
	}

	private void update() {
		firePropertyChange(NEW_STATE, null, theGame);
	}
	
	private void endGame() {
		firePropertyChange(FINISHED, null, theGame);
	}

}
