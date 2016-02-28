package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.JComponent;
import javax.swing.JPanel;

//This will be used as a template for all following menu screens
public abstract class DisplayScreen extends JPanel  implements ActionListener{
	//default UI settings
	protected static final Color BACKGROUND = new Color(225,143,80);
	protected static final Color EXTRA_PANEL = new Color(235,153,90);
	protected static final Color FIELD_BACK = new Color(255,203,150);
	protected static final Color PROGRESS_BAR_TEXT = new Color(80,50,0);
	protected static final Color BUTTON_COLOUR_TOP = new Color(200,80,0);
	protected static final Color BUTTON_COLOUR_MID = new Color(230,100,0);
	protected static final Color BUTTON_COLOUR_BOTTOM = new Color(180,80,0);
	//resizable box fillers
	public static Component getSpace(){return new Box.Filler(new Dimension(0,0), 
            new Dimension(0, 20), 
            new Dimension(0, 50));}
	public static Component getSmallSpace(){return new Box.Filler(new Dimension(0,0), 
            new Dimension(0, 5), 
            new Dimension(0, 30));}
	
	
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
		setBackground(BACKGROUND);
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
	
	//when resized, the items will need to be repositioned
	public void rePosition(){
		resizeBoards();
		placeItems();
	}
	protected void resizeBoards(){
		//Default does nothing
	}
	
	//scale and place it at the centre of the specified point
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
	
	//define some font sizes
	public static final int FONT_NORMAL = 12;
	public static final int FONT_SMALL = 10;
	public static final int FONT_TITLE = 24;
	public static final int FONT_SUBTITLE = 18;
	//scale the font in components and any subcomponents
	protected void fontScale(JComponent thing, int fontsize){
		float scaled_font = (float) (scale_factor * fontsize);
		if(lobster != null && (fontsize == FONT_TITLE || fontsize == FONT_SUBTITLE)){
			thing.setFont(lobster.deriveFont(scaled_font));
		} else {
			thing.setFont(new Font("Helvetica", Font.CENTER_BASELINE, (int)scaled_font));
		}
		//resize the fonts
		Component[] components = (Component[])thing.getComponents();
		if(components != null){
			for(Component c:components){
				//resize if it has text
				if(lobster != null && (fontsize == FONT_TITLE || fontsize == FONT_SUBTITLE)){
					c.setFont(lobster.deriveFont(scaled_font));
				} else {
					c.setFont(new Font("Helvetica", Font.CENTER_BASELINE, (int)scaled_font));
				}
			}
		}
	}
	//position the scaled board, centred at that point
	protected void positionBoard(DisplayBoard board, double frac_w, double frac_h){
		board.setAlignmentX(CENTER_ALIGNMENT);
		board.setAlignmentY(CENTER_ALIGNMENT);
		int width = 10 * board.tile_size;
		double scaled_width = width;
		double x_offset = - ((double)board.width)/2 * board.tile_size;
		double y_offset = - ((double)board.height)/2 * board.tile_size;
		board.setBounds(
				(int)(screen_width*frac_w + x_offset), 
				(int)(screen_height*(1-frac_h) + y_offset),
				(int) scaled_width, 
				(int) scaled_width
				);
	}
}
