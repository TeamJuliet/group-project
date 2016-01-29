package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class CustomBoard extends JComponent implements MouseListener{

	private final int tile_size;
	private int width;
	private int height;
	private Cell[][] board;
	
	private Timer timer;
	private TimerTask task;
	private final int refresh_rate = 20;
	
	private LevelCreatorScreen watch_creator;
	
	public CustomBoard(int width, int height)	{
		super();
		this.width = width;
		this.height = height;
		board = new Cell[width][height];

		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				board[x][y] = new Cell(CellType.UNUSABLE);
			}
		}
		
		tile_size = InterfaceManager.screenHeight()/15;
		
		setPreferredSize(new Dimension(width*tile_size,height*tile_size));
		
		addMouseListener(this);
		timer = new Timer();
	}
	public void watchLevelCreator(LevelCreatorScreen level_creator){
		watch_creator = level_creator;
	}
	
	public void changeSize(int new_w, int new_h){
		Cell[][] new_board = new Cell[new_w][new_h];
		//keep as much of the old board as possible
		for(int x=0;x<new_w;x++){
			for(int y=0;y<new_h;y++){
				if(x<width && y<height)
					new_board[x][y] = board[x][y];
				else new_board[x][y] = new Cell(CellType.UNUSABLE);
			}
		}
		//replace the old board
		width = new_w;
		height = new_h;
		board = new_board;
		
		setPreferredSize(new Dimension(width*tile_size,height*tile_size));
	}
	
	public Cell[][] getBoard(){
		return board;
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
		default:
			g.setColor(Color.LIGHT_GRAY);
			break;
		}
		g.fillRect(x*tile_size, y*tile_size, tile_size, tile_size);
		
		//draw the jelly
		int jelly_level = board[x][y].getJellyLevel();
		if(jelly_level>0){
			g.setColor(Color.CYAN);
			g.fillOval(x*tile_size, y*tile_size, tile_size, tile_size);
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(jelly_level), x*tile_size + tile_size/2, y*tile_size + tile_size/2);
		}
		
		//draw the ingredients
		if(board[x][y].getCandy()!=null &&
				board[x][y].getCandy().getCandyType() == CandyType.INGREDIENT){
			g.setColor(Color.RED);
			g.fillOval(x*tile_size, y*tile_size, tile_size, tile_size);			
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
	
	public void changeMode(GameMode mode){
		switch(mode){
		case HIGHSCORE:
			//(removes any objective cells)
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					board[x][y] = new Cell(board[x][y].getCellType());
				}
			}
			break;
		case JELLY:
			//(removes any objective cells)
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					int temp_jl = board[x][y].getJellyLevel();
					board[x][y] = new Cell(board[x][y].getCellType());
					board[x][y].setJellyLevel(temp_jl);
				}
			}
			break;
		case INGREDIENTS:
			//(removes any jellies)
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					board[x][y].setJellyLevel(0);
				}
			}
			break;
		}
	}

	public void changeTile(){

		Point pos = getMousePosition();
		
		if(pos != null){
			int x = pos.x/tile_size;
			int y = pos.y/tile_size;
			//convert to coordinates
			if(
				x>=0 && x<width &&
				y>=0 && y<height &&
				board[x][y].getCellType() != watch_creator.getReplacer()
					)
				board[x][y] = new Cell(watch_creator.getReplacer());
		}
	}

	public void mousePressed(MouseEvent e) {
		if(watch_creator.fillingCells()){
			task = new ClickDrag(this);
	    	timer.scheduleAtFixedRate(task, 0, refresh_rate); // Time is in milliseconds
	    	// The second parameter is delay before the first run
	    	// The third is how often to run it
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(watch_creator.fillingCells()){
			task.cancel();
			// Will not stop execution of task.run() if it is midway
			// But will guarantee that after this call it runs no more than one more time
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//click for special candies
		if(!watch_creator.fillingCells() && watch_creator.canFill()){
			int x = e.getX()/tile_size;
			int y = e.getY()/tile_size;
			
			//fill with a jelly (toggle level)
			if(watch_creator.fillingJellies()){
				int jelly_level = board[x][y].getJellyLevel()+1;
				if(jelly_level>Cell.maxJellyLevel)jelly_level = 0;
				board[x][y].setJellyLevel(jelly_level);
			}
			//fill with an ingredient (toggle on/off)
			else {
				if(board[x][y].getCandy() == null){
					board[x][y].changeCandyType(CandyType.INGREDIENT);
				} else {
					board[x][y] = new Cell(board[x][y].getCellType());
				}
			}
		}
	}

	//Don't care about these
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
