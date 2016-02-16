package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class DesignDetails extends JPanel{
	private JLabel numberOfCandies;
	private JLabel movesAllowed;
	private JLabel objectiveTarget;
	
	public DesignDetails(){
		super();
		GameMode mode = GameMode.HIGHSCORE;
		numberOfCandies = new JLabel("",SwingConstants.CENTER);
		movesAllowed = new JLabel("",SwingConstants.CENTER);
		objectiveTarget = new JLabel("",SwingConstants.CENTER);

		numberOfCandies.setAlignmentX(CENTER_ALIGNMENT);
		movesAllowed.setAlignmentX(CENTER_ALIGNMENT);
		objectiveTarget.setAlignmentX(CENTER_ALIGNMENT);

		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.black));

		add(Box.createRigidArea(new Dimension(0, 10)));
		add(objectiveTarget);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(movesAllowed);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(numberOfCandies);
		add(Box.createRigidArea(new Dimension(0, 10)));
	}
	
	public void setDetails(Design design){
		GameMode mode = design.getMode();
		int objective = design.getObjectiveTarget();
		switch(mode){
		case HIGHSCORE: 
			objectiveTarget.setText("Objective: Get "+objective+" points");
			break;
		case JELLY: 
			objectiveTarget.setText("Objective: Clear all jelly layers");
			break;
		default: //ingredients
			objectiveTarget.setText("Objective: Clear "+objective+" ingredient"+(objective!=1?"s":""));
			break; 
		}
		numberOfCandies.setText("Candy Colours in play: "+design.getNumberOfCandyColours());
		movesAllowed.setText("Moves Allowed: "+design.getNumberOfMovesAvailable());
	}
	
}
