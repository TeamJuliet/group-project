package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class DisplayBoard extends JComponent {

	protected int tile_size;
	protected int width;
	protected int height;
	protected Cell[][] board;
	
	public static Cell[][] blank_board(){
		Cell[][] new_board = new Cell[10][10];

		for(int x=0;x<10;x++){
			for(int y=0;y<10;y++){
				new_board[x][y] = defaultCell();
			}
		}
		return new_board;
	}
	public static Cell[][] blank_board(int width, int height){
		Cell[][] new_board = new Cell[width][height];

		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				new_board[x][y] = defaultCell();
			}
		}
		return new_board;
	}
	
	public DisplayBoard(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		board = new Cell[width][height];

		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				board[x][y] = defaultCell();
			}
		}
		
		adjustSize(4);
		
	}
	public DisplayBoard(Design design){
		if(design == null){
			width = 5;
			height = 5;
			board = new Cell[width][height];

			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					board[x][y] = new Cell(CellType.UNUSABLE);
				}
			}
		} else {
			width = design.getWidth();
			height = design.getHeight();
			board = design.getBoard();
		}
		adjustSize(4);
	}
	
	protected static Cell defaultCell(){
		return new Cell(CellType.UNUSABLE);
	}
	
	public void adjustSize(int scaleFactor) {
		tile_size = InterfaceManager.screenHeight()/(60/scaleFactor);
	}
	
	public Cell[][] getBoard(){
		return board;
	}
	public void setBoard(Cell[][] board){
		if(board!= null){
			this.board = board.clone();
			width = board.length;
			height = board[0].length;
		} else{
			System.out.println("Null board sent");
		}
	}
	
	public void clearBoard(){
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				board[x][y] = defaultCell();				
			}
		}
	}

	//drawing the screen
	private void draw_cell(int x, int y, Graphics g){
		
		//draw the cell
		switch(board[x][y].getCellType()){
		case UNUSABLE:
			g.setColor(Color.GRAY);
			break;
		case ICING:
			g.setColor(Color.WHITE);
			break;
		case LIQUORICE:
			g.setColor(Color.BLACK);
			break;
		case DONT_CARE:
			g.setColor(Color.PINK);
			break;
		default:
			g.setColor(Color.LIGHT_GRAY);
			break;
		}
		g.fillRect(x*tile_size, y*tile_size, tile_size, tile_size);
		//don't care state
		if(board[x][y].getCellType() == CellType.DONT_CARE){
			g.setColor(Color.BLACK);
			g.drawLine(x*tile_size, y*tile_size, x*tile_size + tile_size, y*tile_size + tile_size);
			g.drawLine(x*tile_size, y*tile_size + tile_size, x*tile_size + tile_size, y*tile_size);
		}
		
		//draw the candies
		if(board[x][y].getCandy()!=null){
			switch(board[x][y].getCandy().getCandyType())
			{
			case INGREDIENT:
				g.setColor(Color.YELLOW);
				g.fillOval(x*tile_size, y*tile_size, tile_size, tile_size);
				g.setColor(Color.ORANGE);
				g.fillRect(x*tile_size, y*tile_size+tile_size/2-tile_size/8, tile_size, tile_size/4);
				break;
			case BOMB:
				g.setColor(new Color(100,80,0));
				g.fillOval(x*tile_size, y*tile_size, tile_size, tile_size);
				g.setColor(Color.MAGENTA);
				g.fillOval(x*tile_size+tile_size/2, y*tile_size+tile_size/2, 3, 3);
				g.fillOval(x*tile_size+tile_size/2+10, y*tile_size+tile_size/2, 3, 3);
				g.fillOval(x*tile_size+tile_size/2-10, y*tile_size+tile_size/2, 3, 3);
				g.fillOval(x*tile_size+tile_size/2, y*tile_size+tile_size/2+10, 3, 3);
				g.fillOval(x*tile_size+tile_size/2, y*tile_size+tile_size/2-10, 3, 3);
				break;
			case VERTICALLY_STRIPPED:
			case HORIZONTALLY_STRIPPED:
			case WRAPPED:
			case NORMAL:
				switch (board[x][y].getCandy().getColour()){
				case RED:
					g.setColor(Color.RED);
					break;
				case ORANGE:
					g.setColor(Color.ORANGE);
					break;
				case YELLOW:
					g.setColor(Color.YELLOW);
					break;
				case GREEN:
					g.setColor(Color.GREEN);
					break;
				case BLUE:
					g.setColor(Color.BLUE);
					break;
				case PURPLE:
					g.setColor(new Color(120,0,150));
					break;
				}
				g.fillOval(x*tile_size, y*tile_size+tile_size/8, tile_size, tile_size-tile_size/4);
				break;
			}
			//an additional switch for special properties
			g.setColor(Color.WHITE);
			switch(board[x][y].getCandy().getCandyType())
			{
			case VERTICALLY_STRIPPED:
				g.fillRect(x*tile_size + tile_size/2-1, y*tile_size+tile_size/8, 4, tile_size-tile_size/4);
				break;
			case HORIZONTALLY_STRIPPED:
				g.fillRect(x*tile_size, y*tile_size+tile_size/2-1, tile_size, 4);
				break;
			case WRAPPED:
				g.fillRect(x*tile_size + tile_size/4, y*tile_size + tile_size/4, tile_size/2, tile_size/2);
				break;
			default:
					break;
			}
		}
		
		//draw the jelly
		int jelly_level = board[x][y].getJellyLevel();
		if(jelly_level>0){
			g.setColor(new Color(100,200,255,128));
			g.fillOval(x*tile_size, y*tile_size, tile_size, tile_size);
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(jelly_level), x*tile_size + tile_size/2, y*tile_size + tile_size/2);
		}
		
		//outline the tiles
		g.setColor(Color.BLACK);
		g.drawRect(x*tile_size, y*tile_size, tile_size, tile_size);		
	}
	
	public void paint(Graphics g){
		setBorder(BorderFactory.createLineBorder(Color.black));
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				draw_cell(x,y,g);
			}
		}
	}
	
	public void minimumBoundingBox() {
		int min_x = width-1;
		int min_y = height-1;
		int max_x = 0;
		int max_y = 0;
		
		//scan for min and max pixels
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(board[x][y].getCellType() != CellType.UNUSABLE){
					if(x<min_x)min_x = x;
					if(y<min_y)min_y = y;
					if(x>max_x)max_x = x;
					if(y>max_y)max_y = y;
				}
			}
		}
		
		//check for no items found
		if(min_x>max_x){
			min_x = 0;
			max_x = 4;
		}
		if(min_y>max_y){
			min_y = 0;
			max_y = 4;
		}
		
		//make new board from this
		width = max_x - min_x + 1;
		height = max_y - min_y + 1;
		if(width<5){
			width = 5;
			if(min_x>5)min_x = 5;
		}
		if(height<5){
			height = 5;
			if(min_y>5)min_y = 5;
		}
		Cell[][] new_board = new Cell[width][height];
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				new_board[x][y] = board[x+min_x][y+min_y];
			}
		}
		
		//replace the board
		board = new_board;
		
	}
}
