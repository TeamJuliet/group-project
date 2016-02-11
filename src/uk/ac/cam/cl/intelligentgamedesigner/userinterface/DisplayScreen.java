package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

//This will be used as a template for all following menu screens
public abstract class DisplayScreen extends JPanel  implements ActionListener{
	//The identifier is needed for switching between 'Cards'
	protected String identifier;
	public String getIdentifier(){
		return identifier;
	}
	
	public DisplayScreen(){
		makeItems();
		setUpItems();
		placeItems();
	}
	
	//instantiate any buttons, text etc.
	protected abstract void makeItems();
	//set up ActionListeners etc. on the items
	protected abstract void setUpItems();
	//place the items on the screen
	protected abstract void placeItems();
	
	//position the item relative to the total screen size
	private static int screen_width = InterfaceManager.screenWidth();
	private static int screen_height = InterfaceManager.screenHeight();
	public static double scale_factor = 1600.0/screen_width;
	protected void position(JComponent thing,double frac_w,double frac_h, int width, int height){
		double scaled_width = scale_factor * width;
		double scaled_height = scale_factor * height;
		thing.setAlignmentX(CENTER_ALIGNMENT);
		thing.setAlignmentY(CENTER_ALIGNMENT);
		thing.setBounds(
				(int)(screen_width*frac_w - scaled_width/2), 
				(int)(screen_height*(1-frac_h) - scaled_height/2),
				(int) scaled_width, 
				(int) scaled_height
				);
	}
	protected void positionBoard(DisplayBoard board, double frac_w, double frac_h){
		board.setAlignmentX(CENTER_ALIGNMENT);
		board.setAlignmentY(CENTER_ALIGNMENT);
		int width = 10 * board.tile_size;
		double scaled_width = scale_factor * width;
		double offset = -scale_factor * 4.25 * board.tile_size;
		board.setBounds(
				(int)(screen_width*frac_w + offset), 
				(int)(screen_height*(1-frac_h) + offset),
				(int) scaled_width, 
				(int) scaled_width
				);
	}
}
