package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class UnitTestBoard extends CustomBoard {

	private int type;//0 is before, 1 is after, 2 is lookahead
	private Cell[][] watch_board;
	private Position move_from;
	private Position move_to;
	private boolean just_clicked;//so doesn't trigger twice, for click, and down
	
	@Override
	protected Cell defaultCell(){
		if(type == 2){
			return new Cell(CellType.NORMAL,new Candy(CandyColour.RED, CandyType.NORMAL));
		} else {
			return super.defaultCell();
		}
	}
	
	public UnitTestBoard(int width, int height, int type) {
		super(width, height);
		this.type = type;
		tile_size = InterfaceManager.screenHeight()/30;
		watch_board = null;
		move_from = new Position(0,0);
		move_to = new Position(0,0);
		
		if(type==2){
			clearBoard();
		}
	}
	
	public void watchBoard(UnitTestBoard watch){
		if(type == 0){//before watches and updates after
			watch_board = watch.getBoard();
		}
	}
	
	public Move getMove(){
		return new Move(move_from, move_to);
	}

	@Override
	public void changeTile(){
		Point pos = getMousePosition();
		if(pos != null){
			//convert to coordinates
			int x = pos.x/tile_size;
			int y = pos.y/tile_size;
			if(x>=0 && x<width && y>=0 && y<height){
				if(watch_creator.identifier.equals("Unit Test Maker")){ //if instead unit test maker	
					if(type==2){//can only fill with regular candies
						if(watch_creator.identifier.equals("Unit Test Maker")){ //if instead unit test maker
							if(((UnitTestMakerScreen)watch_creator).fillingCandies()){
								if(board[x][y].getCandy() == null || 
										board[x][y].getCandy().getColour() != 
										((UnitTestMakerScreen)watch_creator).getReplacerCandy().getColour()){
									board[x][y].setCandy(((UnitTestMakerScreen)watch_creator).getReplacerCandy());
									//remove the double click issue
									if(!just_clicked){
										just_clicked = true;
										System.out.println("just clicked");
									}
								}
							}
						}
						return;
					}
					if(((UnitTestMakerScreen)watch_creator).fillingCandies()){
						if(board[x][y].getCandy() == null || 
								board[x][y].getCandy().getColour() != 
								((UnitTestMakerScreen)watch_creator).getReplacerCandy().getColour()){
							board[x][y].setCandy(((UnitTestMakerScreen)watch_creator).getReplacerCandy());
						}
					} else if(((UnitTestMakerScreen)watch_creator).placingMove()) {
						move_to = new Position(x*tile_size,y*tile_size);
					} else {
						CellType watch_type = ((UnitTestMakerScreen)watch_creator).getReplacer();
						if(type == 1 || watch_type != CellType.DONT_CARE){ //can only don't care for 'after' board
							if(board[x][y].getCellType() != watch_type){
								board[x][y] = new Cell(watch_type);						
							}							
						}
					}
					if(watch_board != null)watch_board[x][y] = board[x][y];
				}
				
			}
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(watch_creator.identifier.equals("Unit Test Maker")){ //check is unit test maker
			if(((UnitTestMakerScreen)watch_creator).fillingCells() || 
					((UnitTestMakerScreen)watch_creator).fillingCandies() ||
					((UnitTestMakerScreen)watch_creator).placingMove()){
				task = new ClickDrag(this);
		    	timer.scheduleAtFixedRate(task, 0, refresh_rate); // Time is in milliseconds
		    	// The second parameter is delay before the first run
		    	// The third is how often to run it
				if(((UnitTestMakerScreen)watch_creator).placingMove()){
					Point pos = getMousePosition();
					int posx = (pos.x/tile_size)*tile_size;
					if(posx>=width*tile_size)posx = (width-1)*tile_size;
					int posy = (pos.y/tile_size)*tile_size;
					if(posy>=height*tile_size)posy = (height-1)*tile_size;
					move_from = new Position(posx,posy);
					move_to = move_from;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(watch_creator.identifier.equals("Unit Test Maker")){ //check is unit test maker
			if(((UnitTestMakerScreen)watch_creator).fillingCells() || 
					((UnitTestMakerScreen)watch_creator).fillingCandies() ||
					((UnitTestMakerScreen)watch_creator).placingMove()){
				task.cancel();
				// Will not stop execution of task.run() if it is midway
				// But will guarantee that after this call it runs no more than one more time
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//click for special candies
		if(watch_creator.identifier.equals("Unit Test Maker") //check is unit test maker
				&& type != 2 //can't put special things in generator
				&& !just_clicked){ 
			if(!((UnitTestMakerScreen)watch_creator).fillingCells() && ((UnitTestMakerScreen)watch_creator).canFill()){
				int x = e.getX()/tile_size;
				int y = e.getY()/tile_size;
				
				//fill with a jelly (toggle level)
				if(((UnitTestMakerScreen)watch_creator).fillingJellies()){
					//change cell type to one allowed
					if(board[x][y].getCellType() != CellType.NORMAL && board[x][y].getCellType() != CellType.EMPTY){
						board[x][y].setCellType(CellType.EMPTY);
					}
					int jelly_level = board[x][y].getJellyLevel()+1;
					if(jelly_level>Cell.maxJellyLevel)jelly_level = 0;
					board[x][y].setJellyLevel(jelly_level);
				}
				else {
					//make the candy special (toggle level)
					if(((UnitTestMakerScreen)watch_creator).fillingCandies()) {
						Candy replacement = ((UnitTestMakerScreen)watch_creator).getReplacerCandy();
						CandyType upgrade = board[x][y].getCandy().getCandyType();
						int n = upgrade.ordinal()+1;
						if(n == CandyType.INGREDIENT.ordinal())n=0;
						upgrade = CandyType.values()[n];
						if(board[x][y].getCandy().getColour() == replacement.getColour()){
							board[x][y].setCandy(new Candy(replacement.getColour(), upgrade));
						}
					}
					else {
						//fill with an ingredient (toggle on/off)
						if(board[x][y].getCandy() == null || 
								board[x][y].getCandy().getCandyType() != CandyType.INGREDIENT){
							board[x][y].changeCandyType(CandyType.INGREDIENT);
						} else {
							board[x][y] = new Cell(board[x][y].getCellType());
						}
					}					
				}
				if(watch_board != null)watch_board[x][y] = board[x][y];
			}
		}
		//remove double click issue
		just_clicked = false;
		System.out.println("ready to click");
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		//draw the cursor		
		if(type == 0){
			g.setColor(Color.WHITE);
			g.drawRect(move_from.x, move_from.y, tile_size, tile_size);
			g.drawRect(move_to.x, move_to.y, tile_size, tile_size);
			g.setColor(Color.BLACK);
			g.drawRect(move_from.x+1, move_from.y+1, tile_size-2, tile_size-2);
			g.drawRect(move_to.x+1, move_to.y+1, tile_size-2, tile_size-2);
			g.setColor(Color.WHITE);
			g.drawRect(move_from.x+2, move_from.y+2, tile_size-4, tile_size-4);
			g.drawRect(move_to.x+2, move_to.y+2, tile_size-4, tile_size-4);
		}
	}
}
