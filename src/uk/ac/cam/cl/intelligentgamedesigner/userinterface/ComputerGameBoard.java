package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

//The game board used by the simulated player watcher.
//moves selected by the simulated player are shown momentarily before the board performs the move
public class ComputerGameBoard extends GameBoard{
	
	private Move move;
	
	public ComputerGameBoard(Design design) {
		super(design);
		move = null;
	}
	
	public void showMove(Move move){
		this.move = move;
		paintImmediately(0,0,InterfaceManager.screenWidth(),InterfaceManager.screenHeight());
	}
	public void hideMove(){
		move = null;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		//draw the cursor	
		g.setColor(Color.WHITE);
		if(move != null){
			
			//draw the swapping choice above
			redrawCandy(move.p1.x,move.p2.y,g);
			
			g.drawRect(move.p1.x*tile_size, move.p1.y*tile_size, tile_size, tile_size);
			g.drawRect(move.p2.x*tile_size, move.p2.y*tile_size, tile_size, tile_size);
			g.setColor(Color.BLACK);
			g.drawRect(move.p1.x*tile_size+1, move.p1.y*tile_size+1, tile_size-2, tile_size-2);
			g.drawRect(move.p2.x*tile_size+1, move.p2.y*tile_size+1, tile_size-2, tile_size-2);
			g.setColor(Color.WHITE);
			g.drawRect(move.p1.x*tile_size+2, move.p1.y*tile_size+2, tile_size-4, tile_size-4);
			g.drawRect(move.p2.x*tile_size+2, move.p2.y*tile_size+2, tile_size-4, tile_size-4);
		}
	}
}
