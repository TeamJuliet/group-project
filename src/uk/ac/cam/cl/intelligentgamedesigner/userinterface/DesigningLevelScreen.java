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
		
        view_level.setEnabled(false);
		for(int n = 0; n < BOARD_COUNT; n++){
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
	
	@Override
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);
		
		add(title);
		add(view_level);
		add(back_button);
		add(progressBar);

		for(int n=0;n<BOARD_COUNT;n++){
			add(topBoards[n]);
			add(topBoardsDetails[n]);
		}
	}
	
	@Override
	protected void placeItems() {
		
		position(title,0.5,0.9,400,50);
		position(progressBar,0.5,0.8,300,40);
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
		case PropertyChanges.PROPERTY_CHANGE_DESIGNS:
            List<Design> topDesigns = (List<Design>) evt.getNewValue();
            int prevMost = currentBoardCount;
            currentBoardCount = topDesigns.size();
            if(currentBoardCount > BOARD_COUNT) currentBoardCount = BOARD_COUNT;
            for(int n=0;n<currentBoardCount;n++){
            	boardDesigns[n] = topDesigns.get(n);
            	topBoards[n].setBoard(boardDesigns[n].getBoard());
            	topBoardsDetails[n].setDetails(boardDesigns[n]);
            }
            if(currentBoardCount < prevMost)currentBoardCount = prevMost;
            
            positionBoards();
			break;
		case PropertyChanges.PROPERTY_CHANGE_PROGRESS:
            double progress = (double) evt.getNewValue();
            progressBar.setValue((int) (progress * 100));
			break;
		case PropertyChanges.PROPERTY_CHANGE_DONE:
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
