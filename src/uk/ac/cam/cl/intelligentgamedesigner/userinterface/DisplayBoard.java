package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class DisplayBoard extends JComponent {

	protected int tile_size;
	protected int width;
	protected int height;
	protected Cell[][] board;
	
	protected static boolean using_textures;
	protected boolean showing_unusables;
	private static BufferedImage[][] candy;//[candy colour][candy type
	private static BufferedImage colour_bomb;
	private static BufferedImage[] cell;
	private static BufferedImage[] objective_piece;//ingredient, then jelly levels
	private static final String prefix = System.getProperty("user.dir") + File.separator + "images" + File.separator;
	private static final String suffix = ".png";
	private static final int image_size = 100;
	
	public static void loadTextures(){
	       try {     
	    	   
	    	   //load the candies
	    	   candy = new BufferedImage[CandyColour.values().length][4];
	    	   for(int c=0;c<6;c++){
	    		   for(int t=0;t<4;t++){
	    			   String name = "";
	    			   switch(c){
	    			   case 0: name = "red"; break;
	    			   case 1: name = "blue"; break;
	    			   case 2: name = "green"; break;
	    			   case 3: name = "yellow"; break;
	    			   case 4: name = "purple"; break;
	    			   case 5: name = "orange"; break;
	    			   }
	    			   switch(t){
	    			   case 1: name+="_vs"; break;
	    			   case 2: name+="_hs"; break;
	    			   case 3: name+="_w"; break;
	    			   }
	    			   candy[c][t] = ImageIO.read(new File(prefix + name + suffix));
	    		   }
	    	   }
	    	   colour_bomb = ImageIO.read(new File(prefix + "bomb" + suffix));
	    	   
	    	   //load the cells
	    	   cell = new BufferedImage[CellType.values().length+1];
	    	   cell[0] = ImageIO.read(new File(prefix + "unusable" + suffix));
	    	   cell[1] = ImageIO.read(new File(prefix + "normal_o" + suffix));
	    	   cell[2] = ImageIO.read(new File(prefix + "icing" + suffix));
	    	   cell[3] = ImageIO.read(new File(prefix + "normal_e" + suffix));
	    	   cell[4] = ImageIO.read(new File(prefix + "liquorice" + suffix));
	    	   cell[5] = ImageIO.read(new File(prefix + "dontcare" + suffix));
	    	   
	    	   //load the objective pieces
	    	   objective_piece = new BufferedImage[6];
	    	   objective_piece[0] = ImageIO.read(new File(prefix + "ingredient" + suffix));
	    	   objective_piece[1] = ImageIO.read(new File(prefix + "Jelly1" + suffix));
	    	   objective_piece[2] = ImageIO.read(new File(prefix + "Jelly2" + suffix));
	    	   objective_piece[3] = ImageIO.read(new File(prefix + "Jelly3" + suffix));
	    	   objective_piece[4] = ImageIO.read(new File(prefix + "Jelly4" + suffix));
	    	   objective_piece[5] = ImageIO.read(new File(prefix + "Jelly5" + suffix));
	    	   
	           using_textures = true;
	        } catch (IOException ex) {
	        	System.out.println("Error in loading textures");
	        	using_textures = false;
	        }
	}
	
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
		
		showing_unusables = true;
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
		
		showing_unusables = true;
	}
	
	protected static Cell defaultCell(){
		return new Cell(CellType.UNUSABLE);
	}
	
	public void adjustSize(double d) {
		tile_size = (int) (InterfaceManager.screenWidth()/(100/d));
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
	private void draw_textured_cell(int x, int y, Graphics g){
		BufferedImage item = null;
		
		//before the candy
		switch(board[x][y].getCellType()){
		case UNUSABLE:
			if(showing_unusables){
				g.drawImage(cell[0]
						, x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
				//outline the tiles
				g.setColor(Color.BLACK);
				g.drawRect(x*tile_size, y*tile_size, tile_size, tile_size);	
			}
			break;
		default:
			if((x+y)%2 == 0){
				g.drawImage(cell[1]
						, x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
			}
			else {
				g.drawImage(cell[3]
					, x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
			}
			break;
		}
		
		if(board[x][y].getCandy() != null){
			switch(board[x][y].getCandy().getCandyType()){
			case BOMB:
				item = colour_bomb;
				break;
			case INGREDIENT:
				item = objective_piece[0];
				break;
			case UNMOVEABLE:
				break;
			default:
				item = candy[board[x][y].getCandy().getColour().ordinal()][board[x][y].getCandy().getCandyType().ordinal()];
				break;
			}
		}

		int jl = board[x][y].getJellyLevel();
		if(jl>0){
			g.drawImage(objective_piece[jl], x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
		}
		
		if(item != null)g.drawImage(item, x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
		
		//after the candy
		switch(board[x][y].getCellType()){
		case DONT_CARE:
		case LIQUORICE:
		case ICING:
			g.drawImage(cell[board[x][y].getCellType().ordinal()]
					, x*tile_size, y*tile_size, (x+1)*tile_size, (y+1)*tile_size, 0, 0, image_size, image_size, null);
			break;
		default: break;
		}
		
	}
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
				if(using_textures)draw_textured_cell(x,y,g);
				else draw_cell(x,y,g);
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
