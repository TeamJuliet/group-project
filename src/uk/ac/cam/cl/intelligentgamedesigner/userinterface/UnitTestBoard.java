package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
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
		
	public UnitTestBoard(int width, int height, int type) {
		super(width, height);
		this.type = type;
		tile_size = InterfaceManager.screenHeight()/30;
		watch_board = null;
		move_from = new Position(0,0);
		move_to = new Position(0,0);
		
		if(type==2){
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					board[x][y] = new Cell(CellType.NORMAL,new Candy(CandyColour.RED,CandyType.NORMAL));				
				}
			}
		}
	}
	
	public void watchBoard(UnitTestBoard watch){
		if(type == 0){//before watches and updates after
			watch_board = watch.getBoard();
		}
	}

	@Override
	public void changeSize(int new_w, int new_h){
		Cell[][] new_board = new Cell[new_w][new_h];
		//keep as much of the old board as possible
		for(int x=0;x<new_w;x++){
			for(int y=0;y<new_h;y++){
				if(x<width && y<height)
					new_board[x][y] = board[x][y];
				else new_board[x][y] = differingDefaultCell();
			}
		}
		//replace the old board
		width = new_w;
		height = new_h;
		board = new_board;
		
		setPreferredSize(new Dimension(width*tile_size,height*tile_size));
	}
	protected Cell differingDefaultCell(){
		if(type == 2)return new Cell(CellType.NORMAL,new Candy(CandyColour.RED,CandyType.NORMAL));
		return new Cell(CellType.UNUSABLE);
	}
	
	public Move getMove(){
		return new Move(move_from, move_to);
	}
	public void setMove(Move move){
		move_from = move.p1;
		move_to = move.p2;
	}

	@Override
	public void changeTile(int x, int y){
		if(x>=0 && x<width && y>=0 && y<height){
			if(watch_creator.identifier.equals("Unit Test Maker")){ //if instead unit test maker	
				if(type==2){//can only fill with regular candies
					if(((UnitTestMakerScreen)watch_creator).fillingCandies()){
						if(board[x][y].getCandy() == null || 
								board[x][y].getCandy().getColour() != 
								((UnitTestMakerScreen)watch_creator).getReplacerCandy().getColour()){
							board[x][y].setCandy(((UnitTestMakerScreen)watch_creator).getReplacerCandy());
						}
					}
					return;
				}
				if(((UnitTestMakerScreen)watch_creator).fillingCandies()){
					if(board[x][y].getCandy() == null || 
							board[x][y].getCandy().getColour() != 
							((UnitTestMakerScreen)watch_creator).getReplacerCandy().getColour()){
						CellType background = CellType.NORMAL;//to keep the correct background type
						if(board[x][y].getCellType() == CellType.LIQUORICE)background = CellType.LIQUORICE;
						board[x][y].setCandy(((UnitTestMakerScreen)watch_creator).getReplacerCandy());
						board[x][y].setCellType(background);
						//remove the double click issue
						if(!just_clicked){
							just_clicked = true;
						}
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
				if(watch_board != null){
					watch_board[x][y] = board[x][y];
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

				Point pos = getMousePosition();
				int posx = (pos.x/tile_size);
				if(posx>=width*tile_size)posx = (width-1);
				int posy = (pos.y/tile_size);
				if(posy>=height*tile_size)posy = (height-1);
				mouse_down = true;
				
				if(((UnitTestMakerScreen)watch_creator).placingMove()){
					move_from = new Position(posx*tile_size,posy*tile_size);
					move_to = move_from;
				}
				else changeTile(posx,posy);
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
					if(board[x][y].getCellType() == CellType.UNUSABLE){
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
						CandyType upgrade;
						if(board[x][y].getCandy() != null){
							upgrade = board[x][y].getCandy().getCandyType();
							int n = upgrade.ordinal()+1;
							if(n == CandyType.INGREDIENT.ordinal())n=0;
							upgrade = CandyType.values()[n];
							if(board[x][y].getCandy().getColour() == replacement.getColour()){
								CellType background = CellType.NORMAL;//to keep the correct background type
								if(board[x][y].getCellType() == CellType.LIQUORICE)background = CellType.LIQUORICE;
								board[x][y].setCandy(new Candy(replacement.getColour(), upgrade));
								board[x][y].setCellType(background);
							}
						}
					}
					else {
						//fill with an ingredient (toggle on/off)
						if(board[x][y].getCandy() == null || 
								board[x][y].getCandy().getCandyType() != CandyType.INGREDIENT){
							if (board[x][y].getCellType() != CellType.NORMAL && 
									board[x][y].getCellType() != CellType.EMPTY && 
									board[x][y].getCellType() != CellType.LIQUORICE){
								board[x][y].setCellType(CellType.EMPTY);
							}
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
	}
	
	@Override
	public void clearBoard(){
		if(type!=2)super.clearBoard();
		else {
			for(int x=0;x<width;x++)
				for(int y=0;y<height;y++)
					board[x][y] = new Cell(CellType.NORMAL, new Candy(CandyColour.RED,CandyType.NORMAL));
		}
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
