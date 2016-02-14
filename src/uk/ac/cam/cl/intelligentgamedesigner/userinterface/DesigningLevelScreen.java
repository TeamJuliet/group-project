package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
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
    private JButton view_level;
    private JButton back_button;
    
    private SelectBoard[] topBoards;
	
	//functional
    private LevelDesignerManager levelDesignerManager;
    private static final int boardCount = 5;
    private int currentBoardCount = 0;
    private Design[] boardDesigns;
    private int selected;
	
	public DesigningLevelScreen(){
		super();
		identifier = "Designing Level";
	}
	
	public void startDesign(Specification specification){
        view_level.setEnabled(false);
		for(int n=0;n<boardCount;n++){
			boardDesigns[n] = new Design();
			topBoards[n].setBoard(boardDesigns[n].getBoard());
			topBoards[n].clearBoard();
			topBoards[n].setSelected(false);
		}
		selected = -1;
		
		currentBoardCount = 0;
		positionBoards();
	
        levelDesignerManager = new LevelDesignerManager(specification);
        levelDesignerManager.addPropertyChangeListener(this);
        levelDesignerManager.execute();	
	}
	
	public void selectBoard(int selected){
		if(selected>=0 && selected<boardCount){
			this.selected = selected;
			for(int n=0;n<boardCount;n++){
				topBoards[n].setSelected(n==selected);
			}
			view_level.setEnabled(topBoards[selected].hasDesign());
		}
	}
	
	protected void positionBoards(){
		for(int n=0;n<boardCount;n++){
			double x_offset = 0.5 + 0.9*(n - 0.5*(currentBoardCount - 1))/boardCount;
			if(n<currentBoardCount)positionBoard(topBoards[n],x_offset,0.45);
			else positionBoard(topBoards[n],x_offset,-1);
		}
	}
	
	@Override
	protected void makeItems() {
		title = new JLabel("Generating The Level...",SwingConstants.CENTER);
		view_level = new JButton("View Level");
		back_button = new JButton("Back");
		
		iterationLabel = new JLabel("",SwingConstants.CENTER);
		
		topBoards = new SelectBoard[boardCount];
		boardDesigns = new Design[5];
		for(int n=0;n<boardCount;n++){
			boardDesigns[n] = new Design();
			topBoards[n] = new SelectBoard(boardDesigns[n],n);
			topBoards[n].adjustSize(1.75);
			topBoards[n].setManager(this);
		}
	}
	
	@Override
	protected void setUpItems() {
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 22));
		
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
		add(iterationLabel);
		
		position(title,0.5,0.9,400,50);
		position(iterationLabel,0.5,0.7,300,40);
		position(view_level,0.5,0.2,200,50);
		position(back_button,0.1,0.85,150,30);
		
		for(int n=0;n<boardCount;n++){
			add(topBoards[n]);
		}
		
		positionBoards();

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			levelDesignerManager = null;//TODO: delete the thread
	    	InterfaceManager.switchScreen(Windows.REQUEST);
			break;
		case "view":
			if(selected!=-1){
				String level_name = InterfaceManager.level_manager.get_next_num()+". Designed Level "+(selected+1)+".lv";
				InterfaceManager.setSelectedDDesign(boardDesigns[selected],level_name);
				InterfaceManager.setPreviousScreen(Windows.REQUESTING);
				InterfaceManager.switchScreen(Windows.DISPLAY);
			}
			break;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {	
		switch(evt.getPropertyName()){
		case PropertyChanges.PROPERTY_CHANGE_DESIGNS:
            List<LevelRepresentation> topDesigns = (List<LevelRepresentation>) evt.getNewValue();
            int prevMost = currentBoardCount;
            currentBoardCount = topDesigns.size();
            if(currentBoardCount > boardCount) currentBoardCount = boardCount;
            for(int n=0;n<currentBoardCount;n++){
            	boardDesigns[n] = topDesigns.get(n).getDesign();
            	topBoards[n].setBoard(boardDesigns[n].getBoard());
            }
            if(currentBoardCount < prevMost)currentBoardCount = prevMost;
            positionBoards();
			break;
		case PropertyChanges.PROPERTY_CHANGE_PROGRESS:
            int iterationNumber = (int) evt.getNewValue();
            iterationLabel.setText("Iteration: " + iterationNumber);
			break;
		case PropertyChanges.PROPERTY_CHANGE_DONE:
			break;
		}
	}
}
