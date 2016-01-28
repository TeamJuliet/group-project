package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import javax.swing.JPanel;

//This will be used as a template for all following menu screens
public abstract class DisplayScreen extends JPanel {
	protected String identifier;
	public String getIdentifier(){
		return identifier;
	}
	public DisplayScreen(){
	}
	
	protected abstract void draw();
	public void switchTo(){		
		draw();
	}
}
