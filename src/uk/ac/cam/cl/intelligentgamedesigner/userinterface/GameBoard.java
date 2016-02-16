package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class GameBoard extends DisplayBoard{
	
	private AnimationType animationType;

	public GameBoard(Design design) {
		super(design);
		showing_unusables = false;
	}
	public GameBoard(int width, int height) {
		super(width,height);
		showing_unusables = false;
	}

	//drawing the screen
	private void draw_cell(int x, int y, Graphics g){
		if(board[x][y].getCellType() == CellType.UNUSABLE)return;
		//draw the cell
		switch(board[x][y].getCellType()){
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
		
		//when a match happens:
		if(board[x][y].getCellType() == CellType.EMPTY){
			g.setColor(Color.WHITE);
			g.drawLine(x*tile_size, y*tile_size, x*tile_size + tile_size, y*tile_size + tile_size);
			g.drawLine(x*tile_size, y*tile_size + tile_size, x*tile_size + tile_size, y*tile_size);
			g.drawLine(x*tile_size, y*tile_size+ tile_size/2, x*tile_size + tile_size , y*tile_size + tile_size/2);
			g.drawLine(x*tile_size+ tile_size/2, y*tile_size , x*tile_size + tile_size/2, y*tile_size  + tile_size);
			
		}
	}
}
