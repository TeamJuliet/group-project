package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;

public class UnitTestBoard extends CustomBoard {

	private int type;//0 is before, 1 is after, 2 is lookahead
	private Cell[][] watch_board;
	
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
		
		if(type==2){
			clearBoard();
		}
	}
	
	public void watchBoard(UnitTestBoard watch){
		if(type == 0){//before watches and updates after
			watch_board = watch.getBoard();
		}
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
					} else {
						if(board[x][y].getCellType() != ((UnitTestMakerScreen)watch_creator).getReplacer()){
							board[x][y] = new Cell(((UnitTestMakerScreen)watch_creator).getReplacer());						
						}
					}
					if(watch_board != null)watch_board[x][y] = board[x][y];
				}
				
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//click for special candies
		if(watch_creator.identifier.equals("Unit Test Maker") //check is unit test maker
				&& type != 2){ //can't put special things in generator
			if(!((UnitTestMakerScreen)watch_creator).fillingCells() && ((UnitTestMakerScreen)watch_creator).canFill()){
				int x = e.getX()/tile_size;
				int y = e.getY()/tile_size;
				
				//fill with a jelly (toggle level)
				if(((UnitTestMakerScreen)watch_creator).fillingJellies()){
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
	}
}
