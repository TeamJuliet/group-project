package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelRepresentation;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.PropertyChanges;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;

//The screen while the level designer is working
//will give stats on the progress etc.
public class DesigningLevelScreen extends DisplayScreen implements ActionListener, PropertyChangeListener {
	
	//visual
	private JLabel title;
    private JLabel iterationLabel;
    private JTextArea taskOutput;
    private JButton view_level;
    private JButton back_button;
	
	//functional
    private LevelDesignerManager levelDesignerManager;
    private Design top_design;
	
	public DesigningLevelScreen(){
		super();
		identifier = "Designing Level";
	}
	
	public void startDesign(Specification specification){
        levelDesignerManager = new LevelDesignerManager(specification);
        levelDesignerManager.addPropertyChangeListener(this);
        levelDesignerManager.execute();	
	}
	
	@Override
	protected void makeItems() {
		title = new JLabel("Generating The Level...",SwingConstants.CENTER);
		view_level = new JButton("View Level");
		back_button = new JButton("Back");
		
	}
	
	@Override
	protected void setUpItems() {
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 22));
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		view_level.setEnabled(false);
		view_level.setActionCommand("view");
		view_level.addActionListener(this);
		
		back_button.setActionCommand("back");
		back_button.addActionListener(this);
		
	}
	
	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		add(title);
		add(view_level);
		add(back_button);
		
		position(title,0.5,0.9,400,50);
		position(view_level,0.5,0.3,200,50);
		position(back_button,0.1,0.85,150,30);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			levelDesignerManager = null;//TODO: delete the thread
	    	InterfaceManager.switchScreen(Windows.REQUEST);
			break;
		case "view":
			String level_name = InterfaceManager.level_manager.get_next_num()+". Designed Level.lv";
			InterfaceManager.setSelectedDDesign(top_design,level_name);
			InterfaceManager.setPreviousScreen(Windows.REQUESTING);
			InterfaceManager.switchScreen(Windows.DISPLAY);
			break;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {	
		switch(evt.getPropertyName()){
		case PropertyChanges.PROPERTY_CHANGE_DESIGNS:
            List<LevelRepresentation> topDesigns = (List<LevelRepresentation>) evt.getNewValue();

            for (LevelRepresentation r : topDesigns) {
                taskOutput.append(r.representationToString() + "\n**************\n");
            }
			break;
		case PropertyChanges.PROPERTY_CHANGE_PROGRESS:
            int iterationNumber = (int) evt.getNewValue();

            iterationLabel.setText("Iteration: " + iterationNumber);
			break;
		case PropertyChanges.PROPERTY_CHANGE_DONE:
            view_level.setEnabled(true);
			break;
		}
	}
}
