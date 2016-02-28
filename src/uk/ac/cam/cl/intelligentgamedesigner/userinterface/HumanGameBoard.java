package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

//Like a regular game board, but with the addition of clicking and dragging mechanics
//it detects human input and uses this to signal moves, which it sends to the game screen
public class HumanGameBoard extends GameBoard implements MouseListener, MouseMotionListener {

	protected GameDisplayScreen screen;
	private Position move_from;
	private Position move_to;
	private boolean selecting;

	public HumanGameBoard(Design design) {
		super(design);
		selecting = false;
		move_from = new Position(0,0);
		move_to = move_from;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void watchGameDisplay(GameDisplayScreen watcher){
		screen = watcher;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point pos = e.getPoint();
		int posx = (pos.x/tile_size);
		if(posx>=width)posx = (width-1);
		int posy = (pos.y/tile_size);
		if(posy>=height)posy = (height-1);
		move_from = new Position(posx,posy);
		move_to = move_from;
		selecting = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		selecting = false;
		screen.playMove(new Move(move_from,move_to));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (selecting) {
			Point pos = e.getPoint();
			int x = pos.x/tile_size;
			int y = pos.y/tile_size;
			move_to = new Position(x,y);

			//redraw the board
			repaint();
		}
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		//draw the cursor	
		g.setColor(Color.WHITE);
		if(selecting && !animating){
			boolean draw_from = 
					move_from.x<width && move_from.y<height &&
					board[move_from.x][move_from.y].getCellType()!=CellType.UNUSABLE;
			boolean draw_to = 
					move_to.x<width && move_to.y<height &&
					board[move_to.x][move_to.y].getCellType()!=CellType.UNUSABLE;

			//draw the swapping choice above
			redrawCandy(move_from.x,move_from.y,g);
			
			if(draw_from)g.drawRect(move_from.x*tile_size, move_from.y*tile_size, tile_size, tile_size);
			if(draw_to)g.drawRect(move_to.x*tile_size, move_to.y*tile_size, tile_size, tile_size);
			g.setColor(Color.BLACK);
			if(draw_from)g.drawRect(move_from.x*tile_size+1, move_from.y*tile_size+1, tile_size-2, tile_size-2);
			if(draw_to)g.drawRect(move_to.x*tile_size+1, move_to.y*tile_size+1, tile_size-2, tile_size-2);
			g.setColor(Color.WHITE);
			if(draw_from)g.drawRect(move_from.x*tile_size+2, move_from.y*tile_size+2, tile_size-4, tile_size-4);
			if(draw_to)g.drawRect(move_to.x*tile_size+2, move_to.y*tile_size+2, tile_size-4, tile_size-4);
			
		}
	}

	//not needed
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
