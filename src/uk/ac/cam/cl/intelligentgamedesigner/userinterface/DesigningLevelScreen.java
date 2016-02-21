package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelRepresentation;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.PropertyChanges;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

//The screen while the level designer is working
//will give stats on the progress etc.
public class DesigningLevelScreen extends DisplayScreen implements ActionListener, PropertyChangeListener {
	
	//visual
	private JLabel title;
    private JLabel iterationLabel;
    private JButton view_level;
    private JButton back_button;
    
    private SelectBoard[] topBoards;
    private DesignDetails[] topBoardsDetails;
	
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
		String objective_text = specification.getGameMode() == GameMode.HIGHSCORE?"High Score":
			specification.getGameMode() == GameMode.JELLY?"Jelly Clear":
				"Ingredients";
		title.setText("Generating "+objective_text+" Levels...");
		
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
		
		iterationLabel = new JLabel("",SwingConstants.CENTER);
		
		topBoards = new SelectBoard[boardCount];
		topBoardsDetails = new DesignDetails[boardCount];
		boardDesigns = new Design[5];
		for(int n=0;n<boardCount;n++){
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
		iterationLabel.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		
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
		add(iterationLabel);
		
		
		for(int n=0;n<boardCount;n++){
			add(topBoards[n]);
			add(topBoardsDetails[n]);
		}
	}
	
	@Override
	protected void placeItems() {
		
		position(title,0.5,0.9,400,50);
		position(iterationLabel,0.5,0.8,300,40);
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
            List<LevelRepresentation> topDesigns = (List<LevelRepresentation>) evt.getNewValue();
            int prevMost = currentBoardCount;
            currentBoardCount = topDesigns.size();
            if(currentBoardCount > boardCount) currentBoardCount = boardCount;
            for(int n=0;n<currentBoardCount;n++){
            	boardDesigns[n] = topDesigns.get(n).getDesign();
            	topBoards[n].setBoard(boardDesigns[n].getBoard());
            	topBoardsDetails[n].setDetails(boardDesigns[n]);
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
	
	@Override
	protected void resizeBoards(){
		for(int n=0;n<boardCount;n++){
			if(topBoards[n] != null)topBoards[n].updateTileSize();
		}
	}
}
