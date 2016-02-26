package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;

import javax.swing.SwingWorker;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
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
	public static final int CLEAR_SPEED = 15;
	public static final int SHUFFLE_SPEED = 12;
	public static final double DISTANCE_PER_SLEEP = 0.1;//in fractions of a tilesize
	public static final int PHASE_SWITCH_SLEEP = 30;
	public static final int SHUFFLE_SLEEP = 200;
	
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
				Cell[][] board_before = copyBoard(theGame.getBoard());
				theGame.makeInitialMove(move);
				if(notBomb(board_before,move))animate(ProcessState.MAKING_SWAP,board_before,new Object[]{move});
				if(wasMatchNotMove(board_before,move))animate(ProcessState.DETONATE_PENDING,board_before,null);
				board_before = copyBoard(theGame.getBoard());
				
				//variables for the shuffle needed check.
				boolean checkforshuffled = false;
				boolean draw_step = true;
				boolean update_step = true;
				//combine all falling animations into one
				boolean join_fall = false;
				ProcessState state = theGame.getCurrentProcessState();
				while(theGame.makeSmallMove()) {
					draw_step = true;
					update_step = true;
					//handle shuffling
					if(checkforshuffled){
						if(state == ProcessState.MATCH_AND_REPLACE)
							animate(ProcessState.SHUFFLE,board_before,null);
						checkforshuffled = false;
						draw_step = false;
					}
					if(state == ProcessState.SHUFFLE){
						checkforshuffled = true;
						update_step = false;
					}
//					//handle falling
//					if(!join_fall && (
//							state == ProcessState.FILL_BOARD ||
//							state == ProcessState.BRING_DOWN_CANDIES ||
//							state == ProcessState.PASSING_INGREDIENTS
//							)){
//						join_fall = true;
//						System.out.println(state);
//						System.out.println(theGame.getCurrentProcessState());
//						System.out.println();
//					} else {
//						if(	
//							theGame.getCurrentProcessState() != ProcessState.FILL_BOARD &&
//							theGame.getCurrentProcessState() != ProcessState.BRING_DOWN_CANDIES &&
//							theGame.getCurrentProcessState() != ProcessState.PASSING_INGREDIENTS &&
//							wasMatch(board_before)
//							){
//							join_fall = false;
//							animate(ProcessState.FILL_BOARD,board_before,null);
//							draw_step = false;
//						} 
//						//else update_step = false;
//					}
					
					if(update_step){
						if(draw_step)animate(state,board_before,null);
						update();
						board_before = copyBoard(theGame.getBoard());	
					}
					state = theGame.getCurrentProcessState();
				}
				Thread.sleep(PHASE_SWITCH_SLEEP);
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
			case DETONATE_PENDING:// Detonate the special candies that were triggered in the first stage.
			case PASSING_INGREDIENTS:// Pass the ingredients that can be passed through the ingredient sinks.
				if(wasMatch(old_game))animateClear(old_game);
				break;
			case SHUFFLE:// The board is shuffled if there are no moves to be made, until there are.
				animateShuffle(old_game,candy_offsets);
				break;
			case BRING_DOWN_CANDIES:// Bring down the candies to the fillable positions on the board.
			case FILL_BOARD:// Fill the empty cells on the board that are not blocked by blocker candies.
				animateFall(old_game,candy_offsets);
		    	break;
			case MAKING_SWAP:// two candies are swapped
				animateSwap(candy_offsets);
				break;
			}
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	
	private boolean wasMatch(Cell[][] old_game){
		Cell[][] current = theGame.getBoard();
		for(int x=0;x<board.width;x++){
			for(int y=0;y<board.height;y++){
				if(!current[x][y].equals(old_game[x][y])){//if newly cleared
					return true;
				}
			}
		}
		return false;
	}
	private boolean wasMatchNotMove(Cell[][] old_game, Move move){
		Cell[][] current = theGame.getBoard();
		for(int x=0;x<board.width;x++){
			for(int y=0;y<board.height;y++){
				if(!current[x][y].equals(old_game[x][y])){//if newly cleared
					if((x != move.p1.x || y != move.p1.y) && (x != move.p2.x || y != move.p2.y))
						return true;
				}
			}
		}
		return false;
	}
	private boolean notBomb(Cell[][] old_game, Move move){
		return (old_game[move.p1.x][move.p1.y].getCandy().getCandyType() != CandyType.BOMB &&
				old_game[move.p2.x][move.p2.y].getCandy().getCandyType() != CandyType.BOMB);
	}

	
	private void animateClear(Cell[][] old_game) throws InterruptedException{
		Thread.sleep(50);
		board.setBoard(old_game);
		board.repaint();
		//find the cells to animate
		Cell[][] current = theGame.getBoard();
		double[][] scales = new double[board.width][board.height];
		double[][] scales2 = new double[board.width][board.height];
		boolean made_special = false;
		for(int x=0;x<board.width;x++){
			for(int y=0;y<board.height;y++){
				scales[x][y] = -1;
				scales2[x][y] = -1;
				if(old_game[x][y].getCandy() != null && old_game[x][y].getCandy().getCandyType() == CandyType.INGREDIENT)
					System.out.println("daddy I found one");
				if(!current[x][y].equals(old_game[x][y])){//if newly cleared
					if(//replace old with new
							current[x][y].hasCandy() ||//if formed special candy
							current[x][y].getJellyLevel()<old_game[x][y].getJellyLevel() ||//or new jelly level
							old_game[x][y].getCellType() == CellType.LIQUORICE //or removed liquorice
							){
						scales2[x][y] = DISTANCE_PER_SLEEP;
						made_special = true;
					}
					scales[x][y] = 1;
				}
			}
		}
		while(!CandyManipulator.shrink(scales,DISTANCE_PER_SLEEP)){
			board.setResize(scales);
			board.repaint();
			Thread.sleep(CLEAR_SPEED);
		}
		Thread.sleep(PHASE_SWITCH_SLEEP);
		board.setBoard(theGame.getBoard());
		if(made_special){
			board.setResize(scales2);
			while(!CandyManipulator.expand(scales2,DISTANCE_PER_SLEEP)){
				board.setResize(scales2);
				board.repaint();
				Thread.sleep(CLEAR_SPEED);
			}
			Thread.sleep(PHASE_SWITCH_SLEEP);
		}
		board.setResize(null);
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
		Thread.sleep(SHUFFLE_SLEEP);
		board.setBoard(theGame.getBoard());
		CandyManipulator.converge(candy_offsets,0,new Dimension((board.width-1)*board.tile_size/2,(board.height-1)*board.tile_size/2),board.tile_size);
		Thread.sleep(SHUFFLE_SLEEP);
		while(!CandyManipulator.decrement(candy_offsets,DISTANCE_PER_SLEEP,board.tile_size)){
			board.setOffsets(candy_offsets);
			board.repaint();
			Thread.sleep(SHUFFLE_SPEED);
		}
		board.clear_offsets();
		Thread.sleep(PHASE_SWITCH_SLEEP);
	}
	
	private void animateSwap(Dimension[][] candy_offsets) throws InterruptedException{
		board.setMoveLoc(new Dimension(move.p2.x,move.p2.y));
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
		board.setMoveLoc(null);
	}
	
	private void animateFall(Cell[][] old_game, Dimension[][] candy_offsets) throws InterruptedException{
		Cell[][] current = copyBoard(theGame.getBoard());
		boolean made_special = false;
		for(int x=0;x<board.width;x++){
			for(int y=board.height-1;y>=0;y--){
				candy_offsets[x][y] = new Dimension(0,0);
				if(!current[x][y].equals(old_game[x][y]) && old_game[x][y].getCellType() == CellType.EMPTY){//if something fell here
					candy_offsets[x][y].height = -findSourceYAbove(x,y,old_game,current)*board.tile_size;
				}
			}
		}
		CandyManipulator.bumpUp(candy_offsets,old_game);

		board.setOffsets(candy_offsets);
		board.repaint();
		while(!CandyManipulator.decrement(candy_offsets,DISTANCE_PER_SLEEP,board.tile_size)){
			board.setOffsets(candy_offsets);
			board.repaint();
			Thread.sleep(FALL_SPEED);
		}
		board.clear_offsets();
	}
	
	private int findSourceYAbove(int x, int y, Cell[][] before, Cell[][] after){
		if(y==0)return 1;
		int height = 0;
		while(before[x][y-height].getCellType()==CellType.EMPTY){
			if(y-height==0)break;
			height++;
		}
		return height;
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
