package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;

//This will be used as a template for all following menu screens
public abstract class DisplayScreen extends JPanel  implements ActionListener{
	//The identifier is needed for switching between 'Cards'
	protected String identifier;
	public String getIdentifier(){
		return identifier;
	}
	
	// Lobster font from: http://www.impallari.com/lobster/
	private static Font lobster;
	public static void loadFonts(){
		File font_file = new File(System.getProperty("user.dir") + File.separator + "images" + File.separator + "Lobster.ttf");
		try {
			lobster = Font.createFont(Font.TRUETYPE_FONT, font_file);
		} catch (FontFormatException | IOException e) {
			lobster = null;
			System.err.println("Failed to load the font");
		}
	}
	
	public DisplayScreen(){
		makeItems();
		setUpItems();
		addItems();
		placeItems();
	}
	
	//instantiate any buttons, text etc.
	protected abstract void makeItems();
	//set up ActionListeners etc. on the items
	protected abstract void setUpItems();
	//add the items to the screen
	protected abstract void addItems();
	//place the items on the screen
	protected abstract void placeItems();
	

	//position the item relative to the total window size
	private static int screen_width;
	private static int screen_height;
	public static double scale_factor;
	public static void reScale(){
		//position the item relative to the total window size
		screen_width = InterfaceManager.screenWidth();
		screen_height = InterfaceManager.screenHeight();
		scale_factor = ((double)screen_width)/1200;		
	}
	
	public void rePosition(){
		resizeBoards();
		placeItems();
	}
	protected void resizeBoards(){
		//Default does nothing
	}
	
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
	
	public static final int FONT_NORMAL = 12;
	public static final int FONT_SMALL = 10;
	public static final int FONT_TITLE = 22;
	public static final int FONT_SUBTITLE = 18;
	protected void fontScale(JComponent thing, int fontsize){
		int scaled_font = (int)(scale_factor * fontsize);
		if(lobster != null && (fontsize == FONT_TITLE || fontsize == FONT_SUBTITLE)){
			thing.setFont(new Font(lobster.getFontName(), Font.PLAIN, scaled_font));
		} else {
			thing.setFont(new Font("Helvetica", Font.CENTER_BASELINE, scaled_font));
		}
		//resize the fonts
		Component[] components = (Component[])thing.getComponents();
		if(components != null){
			for(Component c:components){
				//resize if it has text
				c.setFont(new Font("Helvetica", Font.CENTER_BASELINE, scaled_font));
			}
		}
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
