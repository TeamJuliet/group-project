package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;
import java.awt.Graphics;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class GameBoard extends DisplayBoard{
	protected Dimension[][] candy_offsets;
	protected double[][] scale_factors;

	public GameBoard(Design design) {
		super(design);
		showing_unusables = false;
		candy_offsets = new Dimension[width][height];
		scale_factors = null;
	}
	public GameBoard(int width, int height) {
		super(width,height);
		showing_unusables = false;
		candy_offsets = new Dimension[width][height];
		scale_factors = null;
	}
	
	protected boolean animating;
	public void setAnimating(boolean animating){
		this.animating = animating;
	}
	protected void clear_offsets(){
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(candy_offsets[x][y]!=null)candy_offsets[x][y].setSize(0, 0);
			}
		}
	}
	protected void setOffsets(Dimension[][] offsets){
		candy_offsets = offsets;
	}
	protected void setResize(double[][] new_size){
		scale_factors = new_size;
	}
	
	


	//drawing the screen, with animations!
	@Override
	public void paint(Graphics g){
		if(!using_textures)super.paint(g);
		else{
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					int x_loc = x*tile_size;
					int y_loc = y*tile_size;
					if(candy_offsets != null && candy_offsets[x][y] != null){
						x_loc += candy_offsets[x][y].width;
						y_loc += candy_offsets[x][y].height;
					}
					draw_textured_cell(x,y,g,x_loc,y_loc,scale_factors);
				}
			}
		}
	}
	
}
