package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//The screen while the level designer is working
//will give stats on the progress etc.
public class DesigningLevelScreen extends DisplayScreen implements ChangeListener{
	
	private JLabel title;
	
	public DesigningLevelScreen(){
		super();
		identifier = "Designing Level";
	}
	
	@Override
	protected void makeItems() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void setUpItems() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void placeItems() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
