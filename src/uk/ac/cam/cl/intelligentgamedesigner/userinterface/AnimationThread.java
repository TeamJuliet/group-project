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
	public static final int CLEAR_SPEED = 10;
	public static final int SHUFFLE_SPEED = 12;
	public static final double DISTANCE_PER_SLEEP = 0.1;//in fractions of a tilesize
	public static final int PHASE_SWITCH_SLEEP = 150;
	
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
				Cell[][] old_game = copyBoard(theGame.getBoard());
				theGame.makeInitialMove(move);
				animate(ProcessState.MAKING_SWAP,old_game,new Object[]{move});
				old_game = copyBoard(theGame.getBoard());
				
				//variables for the shuffle needed check.
				boolean checkforshuffled = false;
				boolean draw_step = true;
				
				while(theGame.makeSmallMove()) {
					draw_step = true;
					if(checkforshuffled){
						if(theGame.getCurrentProcessState() == ProcessState.MATCH_AND_REPLACE)
							animate(ProcessState.SHUFFLE,old_game,null);
						checkforshuffled = false;
					}
					if(theGame.getCurrentProcessState() == ProcessState.SHUFFLE){
						checkforshuffled = true;
						draw_step = false;
					}
					//handle shuffling if needed
					
					if(draw_step){
						animate(theGame.getCurrentProcessState(),old_game,null);
						update();
						old_game = copyBoard(theGame.getBoard());	
					}
				}
			} else {
				theGame.makeFullMove(move);
				update();
			}	
		} catch (InvalidMoveException ex) {
			DebugFilter.println("Invalid move",DebugFilterKey.GAME_IMPLEMENTATION);
		}catch (InterruptedException e) {
			DebugFilter.println("Stopped the animation", DebugFilterKey.USER_INTERFACE);
		} finally {
			board.setAnimating(false);
		}
		endGame();	
			
		return null;
	}

	
	public void animate(ProcessState gameState, Cell[][] old_game, Object[] inputs) throws InterruptedException{
		try
		{
			Dimension[][] candy_offsets = new Dimension[old_game.length][old_game[0].length];
			switch(gameState){
			
			
			case MATCH_AND_REPLACE:// Stage where matches occur and the cells that matched are emptied.
				animateClear(old_game);
				break;
			case SHUFFLE:// The board is shuffled if there are no moves to be made, until there are.
				animateShuffle(old_game,candy_offsets);
				break;
//			case DETONATE_PENDING:// Detonate the special candies that were triggered in the first stage.
//				break;
//			case BRING_DOWN_CANDIES:// Bring down the candies to the fillable positions on the board.
//				break;
//			case PASSING_INGREDIENTS:// Pass the ingredients that can be passed through the ingredient sinks.
//				break;
//			case FILL_BOARD:// Fill the empty cells on the board that are not blocked by blocker candies.
//		    	break;
			case AWAITING_MOVE:// The game state is awaiting a move.
				break;
			case MAKING_SWAP:// two candies are swapped
				animateSwap(candy_offsets);
				break;
				default:
					board.clear_offsets();
					board.setBoard(theGame.getBoard());
					Thread.sleep(PHASE_SWITCH_SLEEP);
				break;
			}
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	
	private void animateClear(Cell[][] old_game) throws InterruptedException{
		Thread.sleep(50);
		board.setBoard(old_game);
		board.repaint();
		Thread.sleep(PHASE_SWITCH_SLEEP);
		//find the cells to animate
		Cell[][] current = theGame.getBoard();
		double[][] scales = new double[board.width][board.height];
		for(int x=0;x<board.width;x++){
			for(int y=0;y<board.height;y++){
				scales[x][y] = -1;
				if(current[x][y].equals(old_game[x][y])){
					scales[x][y] = 1;
					System.out.println(x+", "+y);
				}
			}
		}
		while(!CandyManipulator.shrink(scales,DISTANCE_PER_SLEEP)){
			board.setResize(scales);
			board.repaint();
			Thread.sleep(CLEAR_SPEED);
		}
		board.setResize(null);
		Thread.sleep(PHASE_SWITCH_SLEEP);
	}
	
	private void animateShuffle(Cell[][] old_game,Dimension[][]candy_offsets) throws InterruptedException{
		Thread.sleep(50);
		board.setBoard(old_game);
		board.repaint();
		Thread.sleep(PHASE_SWITCH_SLEEP);
		while(!CandyManipulator.converge(candy_offsets,DISTANCE_PER_SLEEP,new Dimension((board.width-1)*board.tile_size/2,(board.height-1)*board.tile_size/2),board.tile_size)){
			board.setOffsets(candy_offsets);
			board.repaint();
			Thread.sleep(SHUFFLE_SPEED);
		}
		Thread.sleep(PHASE_SWITCH_SLEEP);
		board.setBoard(theGame.getBoard());
		CandyManipulator.converge(candy_offsets,0,new Dimension((board.width-1)*board.tile_size/2,(board.height-1)*board.tile_size/2),board.tile_size);
		Thread.sleep(100);
		while(!CandyManipulator.decrement(candy_offsets,DISTANCE_PER_SLEEP,board.tile_size)){
			board.setOffsets(candy_offsets);
			board.repaint();
			Thread.sleep(SHUFFLE_SPEED);
		}
		board.clear_offsets();
		Thread.sleep(PHASE_SWITCH_SLEEP);
	}
	
	private void animateSwap(Dimension[][] candy_offsets) throws InterruptedException{
		candy_offsets[move.p1.x][move.p1.y] = new Dimension(
				board.tile_size * (move.p2.x - move.p1.x),board.tile_size * (move.p2.y - move.p1.y));
		candy_offsets[move.p2.x][move.p2.y] = new Dimension(
				board.tile_size * (move.p1.x - move.p2.x),board.tile_size * (move.p1.y - move.p2.y));
		while(!CandyManipulator.decrement(candy_offsets,DISTANCE_PER_SLEEP,board.tile_size)){
			board.setOffsets(candy_offsets);
			board.repaint();
			Thread.sleep(SWAP_SPEED);
		}
		Thread.sleep(PHASE_SWITCH_SLEEP);
	}

	private void update() {
		firePropertyChange(NEW_STATE, null, theGame);
	}
	
	private void endGame() {
		firePropertyChange(FINISHED, null, theGame);
	}

	
	public static Cell[][] copyBoard(Cell[][] toCopy){
		if(toCopy==null)return null;
		Cell[][] result = new Cell[toCopy.length][toCopy[0].length];
		for(int x=0;x<toCopy.length;x++){
			for(int y=0;y<toCopy[0].length;y++){
				result[x][y] = (Cell) toCopy[x][y].clone();
			}
		}
		return result;
	}
}
