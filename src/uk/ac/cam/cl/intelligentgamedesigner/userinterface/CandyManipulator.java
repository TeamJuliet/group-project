package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;

public class CandyManipulator {
//CANDY SIZE
	public static boolean shrink(double[][] sizes, double increment){
		boolean done = true;
		for(int x=0;x<sizes.length;x++){
			for(int y=0;y<sizes[0].length;y++){
				if(sizes[x][y]>0){
					sizes[x][y]-=increment;
					if(sizes[x][y]<0)sizes[x][y] = 0;
					else done = false;
				}
			}
		}
		return done;
	}
	public static boolean expand(double[][] sizes, double increment){
		boolean done = true;
		for(int x=0;x<sizes.length;x++){
			for(int y=0;y<sizes[0].length;y++){
				if(sizes[x][y]>0 && sizes[x][y]<1){
					sizes[x][y]+=increment;
					if(sizes[x][y]>1)sizes[x][y] = 1;
					else done = false;
				}
			}
		}
		return done;
	}
	
	
//CANDY OFFSET
	//ensure that candies above fall in sync
	public static void bumpUp(Dimension[][] offsets, int size){
		for(int x=0;x<offsets.length;x++){
			for(int y=offsets[0].length-1;y>0;y--){
				if(offsets[x][y].height<0){
					//bump up one
					if(offsets[x][y].height-size != offsets[x][y-1].height)offsets[x][y-1].height = offsets[x][y].height-size;
				}
			}
		}
	}
	
	//move all candies back to their natural positions
	public static boolean decrement(Dimension[][] offsets, double increment,int size){
		boolean done = true;
		for(int x=0;x<offsets.length;x++){
			for(int y=0;y<offsets[0].length;y++){
				if(offsets[x][y] != null){
					if(offsets[x][y].width<0){
						offsets[x][y].width += increment*size;
						if(offsets[x][y].width>=0)offsets[x][y].width = 0;
						else done = false;
					}
					if(offsets[x][y].width>0){
						offsets[x][y].width -= increment*size;
						if(offsets[x][y].width<=0)offsets[x][y].width = 0;
						else done = false;
					}
					if(offsets[x][y].height<0){
						offsets[x][y].height += increment*size;
						if(offsets[x][y].height>=0)offsets[x][y].height = 0;
						else done = false;
					}
					if(offsets[x][y].height>0){
						offsets[x][y].height -= increment*size;
						if(offsets[x][y].height<=0)offsets[x][y].height = 0;
						else done = false;
					}
				}
			}
		}
		return done;
	}
	
	//move all candies to their target positions
	public static boolean increment(Dimension[][] offsets, double increment, Dimension[][] targets, int size){
		boolean done = true;
		for(int x=0;x<offsets.length;x++){
			for(int y=0;y<offsets[0].length;y++){
				if(offsets[x][y] != null){
					if(offsets[x][y].width-targets[x][y].width<0){
						offsets[x][y].width += increment*size;
						if(offsets[x][y].width-targets[x][y].width>=0)offsets[x][y].width = targets[x][y].width;
						else done = false;
					}
					if(offsets[x][y].width-targets[x][y].width>0){
						offsets[x][y].width -= increment*size;
						if(offsets[x][y].width-targets[x][y].width<=0)offsets[x][y].width = targets[x][y].width;
						else done = false;
					}
					if(offsets[x][y].height-targets[x][y].height<0){
						offsets[x][y].height += increment*size;
						if(offsets[x][y].height-targets[x][y].height>=0)offsets[x][y].height = targets[x][y].height;
						else done = false;
					}
					if(offsets[x][y].height-targets[x][y].height>0){
						offsets[x][y].height -= increment*size;
						if(offsets[x][y].height-targets[x][y].height<=0)offsets[x][y].height = targets[x][y].height;
						else done = false;
					}
				}
			}
		}
		return done;
	}
	
	//move all candies to a single position
	public static boolean converge(Dimension[][] offsets, double increment, Dimension point, int size){
		if(increment == 0){
			for(int x=0;x<offsets.length;x++){
				for(int y=0;y<offsets[0].length;y++){
					offsets[x][y].width = point.width - x*size;
					offsets[x][y].height = point.height - y*size;
				}
			}
			return true;
		}
		boolean done = true;
		for(int x=0;x<offsets.length;x++){
			for(int y=0;y<offsets[0].length;y++){
				if(offsets[x][y] == null) offsets[x][y] = new Dimension(0,0);
				if(offsets[x][y].width+x*size - point.width<0){
					offsets[x][y].width += increment*size;
					if(offsets[x][y].width+x*size - point.width>=0)offsets[x][y].width = point.width - x*size;
					else done = false;
				}
				if(offsets[x][y].width+x*size - point.width>0){
					offsets[x][y].width -= increment*size;
					if(offsets[x][y].width+x*size - point.width<=0)offsets[x][y].width = point.width - x*size;
					else done = false;
				}
				if(offsets[x][y].height+y*size - point.height<0){
					offsets[x][y].height += increment*size;
					if(offsets[x][y].height+y*size - point.height>=0)offsets[x][y].height = point.height - y*size;
					else done = false;
				}
				if(offsets[x][y].height+y*size - point.height>0){
					offsets[x][y].height -= increment*size;
					if(offsets[x][y].height+y*size - point.height<=0)offsets[x][y].height = point.height - y*size;
					else done = false;
				}
			} 
		}
		return done;
	}
}
