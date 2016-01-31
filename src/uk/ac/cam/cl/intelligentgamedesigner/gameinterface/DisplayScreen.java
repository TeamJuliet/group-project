package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

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
	protected void position(JComponent thing,double frac_w,double frac_h, int width, int height){
		thing.setBounds(
				(int)(InterfaceManager.screenWidth()*frac_w - width/2), 
				(int)(InterfaceManager.screenHeight()*(1-frac_h) - height/2),
				width, 
				height
				);
	}
}
