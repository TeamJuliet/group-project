package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.PropertyChanges;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

//The screen while the level designer is working
//will give stats on the progress etc.
public class DesigningLevelScreen extends DisplayScreen implements ActionListener, PropertyChangeListener {

	public static final int BOARD_COUNT = 5;

	//visual
	private JLabel title;
	private JProgressBar progressBar;
	private JButton view_level;

    private JButton back_button;
	private SelectBoard[] topBoards;

    private DesignDetails[] topBoardsDetails;
	
    //functional
    private LevelDesignerManager levelDesignerManager;
    private int currentBoardCount = 0;
    private Design[] boardDesigns;
    private int selected;
    
    private boolean changing_difficulty;
	
	public DesigningLevelScreen(){
		super();
		identifier = "Designing Level";
	}
	
	public void startDesign(Specification specification){
        // Stop the user fucking with our threads
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        back_button.setEnabled(false);

		String objective_text = specification.getGameMode() == GameMode.HIGHSCORE?"High Score":
			specification.getGameMode() == GameMode.JELLY?"Jelly Clear":
				"Ingredients";
		title.setText("Generating "+objective_text+" Levels...");
		
		changing_difficulty = false;
		
        view_level.setEnabled(false);
		for(int n = 0; n < BOARD_COUNT; n++){
			boardDesigns[n] = new Design();
			topBoards[n].setBoard(boardDesigns[n].getBoard());
			topBoards[n].clearBoard();
			topBoards[n].setSelected(false);
			topBoardsDetails[n].setDetails(boardDesigns[n],changing_difficulty);
		}
		selected = -1;
		
		currentBoardCount = 0;
		positionBoards();
	
        levelDesignerManager = new LevelDesignerManager(specification);
        levelDesignerManager.addPropertyChangeListener(this);
        levelDesignerManager.execute();	
	}
	
	public void selectBoard(int selected){
		if(selected>=0 && selected<BOARD_COUNT){
			this.selected = selected;
			for(int n=0;n<BOARD_COUNT;n++){
				topBoards[n].setSelected(n==selected);
			}
            if (back_button.isEnabled()) view_level.setEnabled(topBoards[selected].hasDesign());
		}
	}
	
	protected void positionBoards(){
		for(int n=0;n<BOARD_COUNT;n++){
			double x_offset = 0.5 + 0.9*(n - 0.5*(currentBoardCount - 1))/BOARD_COUNT;
			if(n<currentBoardCount){
				positionBoard(topBoards[n],x_offset,0.55);
				position(topBoardsDetails[n],x_offset,0.33,200,80);
			}
			else {
				positionBoard(topBoards[n],x_offset,-1);
				position(topBoardsDetails[n],x_offset,-1,200,80);
			}
		}
	}
	
	@Override
	protected void makeItems() {
		title = new JLabel("Generating The Level...",SwingConstants.CENTER);
		view_level = new JButton("View Level");
		back_button = new JButton("Back");
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		
		topBoards = new SelectBoard[BOARD_COUNT];
		topBoardsDetails = new DesignDetails[BOARD_COUNT];
		boardDesigns = new Design[BOARD_COUNT];
		for(int n=0;n<BOARD_COUNT;n++){
			boardDesigns[n] = new Design();
			topBoards[n] = new SelectBoard(boardDesigns[n],n);
			topBoards[n].adjustSize(1.75);
			topBoards[n].setManager(this);
			topBoardsDetails[n] = new DesignDetails();
		}
	}
	
	@Override
	protected void setUpItems() {
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 22));
		progressBar.setValue(0);
		
		view_level.setEnabled(false);
		view_level.setActionCommand("view");
		view_level.addActionListener(this);
		
		back_button.setActionCommand("back");
		back_button.addActionListener(this);
	}
	
	private JPanel current_progress; 
	private JLabel doing_what;
	@Override
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);

		current_progress = new JPanel();
		doing_what = new JLabel("Setting the level appearance:");
		current_progress.setLayout(new BoxLayout(current_progress,BoxLayout.Y_AXIS));
		current_progress.add(doing_what);
		current_progress.add(progressBar);
		
		add(title);
		add(view_level);
		add(back_button);
		add(current_progress);

		for(int n=0;n<BOARD_COUNT;n++){
			add(topBoards[n]);
			add(topBoardsDetails[n]);
		}
	}
	
	@Override
	protected void placeItems() {
		
		//size the fonts
		fontScale(title, DisplayScreen.FONT_TITLE);
		fontScale(current_progress, DisplayScreen.FONT_NORMAL);
		fontScale(view_level, DisplayScreen.FONT_NORMAL);
		fontScale(back_button, DisplayScreen.FONT_NORMAL);
		
		position(title,0.5,0.9,400,50);
		position(current_progress,0.5,0.8,300,50);
		position(view_level,0.5,0.2,200,50);
		position(back_button,0.1,0.85,150,30);
		
		positionBoards();

	}
	
	private void stopDesigning(){
		levelDesignerManager.cancel(true);
		levelDesignerManager.removePropertyChangeListener(this);
		DebugFilter.print("Should have stopped", DebugFilterKey.USER_INTERFACE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			stopDesigning();
	    	InterfaceManager.switchScreen(Windows.REQUEST);
			break;
		case "view":
			if(selected!=-1){
				stopDesigning();
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
		case PropertyChanges.PROPERTY_CHANGE_PROGRESS: //update the progress bar
            double progress = (double) evt.getNewValue();
            progressBar.setValue((int) (progress * 100));
			break;
		case PropertyChanges.PROPERTY_CHANGE_DESIGNS: //in phase 1, getting new design appearances
		case PropertyChanges.PROPERTY_CHANGE_OBJECTIVES: //in phase 2, getting new design objectives
            Design[] topDesigns = (Design[]) evt.getNewValue();
            boolean updating_appearance = evt.getPropertyName().equals(PropertyChanges.PROPERTY_CHANGE_DESIGNS);
            int prevMost = currentBoardCount;
            currentBoardCount = 5;
            if(currentBoardCount > BOARD_COUNT) currentBoardCount = BOARD_COUNT;
            for(int n=0;n<currentBoardCount;n++){
            	boardDesigns[n] = topDesigns[n];
            	if(updating_appearance)topBoards[n].setBoard(boardDesigns[n].getBoard());
            	topBoardsDetails[n].setDetails(boardDesigns[n],changing_difficulty);
            }
            if(currentBoardCount < prevMost)currentBoardCount = prevMost;
            
            if(updating_appearance)positionBoards();
			break;
		case PropertyChanges.PROPERTY_CHANGE_PHASE1_DONE: //when the appearance phase is done
			changing_difficulty = true;
			progressBar.setValue(0);
            for(int n=0;n<currentBoardCount;n++){
            	topBoardsDetails[n].setDetails(boardDesigns[n],changing_difficulty);
            }
			break;
		case PropertyChanges.PROPERTY_CHANGE_PHASE2_DONE: //when the entire process is done
            // Let the user use the interface again!
            setCursor(null);
            back_button.setEnabled(true);
            if (selected > -1) view_level.setEnabled(true);

			break;
		}
	}
	
	@Override
	protected void resizeBoards(){
		for(int n=0;n<BOARD_COUNT;n++){
			if(topBoards[n] != null)topBoards[n].updateTileSize();
		}
	}
}
