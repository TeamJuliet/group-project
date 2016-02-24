package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerManager;

public class DesignDisplayScreen extends DisplayScreen{
	private JPanel title;
	private JLabel level_number;
	private JTextField level_name;
	
	private DisplayBoard board;
	private JButton play_level;
	private JButton watch_level;
	private JButton save_level;
	private JButton edit_level;
	private JButton back;
	private JLabel game_mode;
	private JLabel moves;
	private JLabel target;
	private JLabel difficulty;
	private JLabel candies;
	private JSlider ai_strength;
	private Windows previous_screen;

	GameMode mode;
	int number_of_moves;
	int objective_value;
	int number_of_candies;
	Design level;
	int level_num;

	public DesignDisplayScreen(){
		super();
		identifier = "Design Display";
		previous_screen = Windows.MAIN;
	}
	
	public void setPreviousScreen(Windows previous){
		previous_screen = previous;
	}
	
	public void reload(Design design, String name){
		String[] split_name = name.split("\\.");
		level_num = Integer.parseInt(split_name[0]);
		level_number.setText(level_num+". ");
		level_name.setText(split_name[1].trim());
		objective_value = design.getObjectiveTarget();
		mode = design.getMode();
		//TODO: Difficulty
		switch(mode){
		case HIGHSCORE:
			game_mode.setText("Game Mode: High Score");
			target.setText("Get "+objective_value+" points!");
			break;
		case JELLY:
			game_mode.setText("Game Mode: Jelly Clear");
			target.setText("Clear all the jellies!");
			break;
		case INGREDIENTS:
			game_mode.setText("Game Mode: Ingredients");
			target.setText(objective_value==1?"Gather all "+objective_value+" ingredients!":"Gather 1 ingredient!");
			break;
		}
		number_of_moves = design.getNumberOfMovesAvailable();
		moves.setText("Moves allowed: "+number_of_moves);
		number_of_candies = design.getNumberOfCandyColours();
		candies.setText(number_of_candies+" candy colours in play");
		board.setBoard(design.getBoard());
		level = design;
		positionBoard(board,0.35,0.5);
	}

	@Override
	protected void makeItems() {
		//initialise with some noncommittal information
		title = new JPanel();
		level_number = new JLabel("1. ");
		level_name = new JTextField("Level Design");
		level = new Design();
		board = new DisplayBoard(level);
		play_level = new JButton("Play Level");
		watch_level = new JButton("Watch Level");
		save_level = new JButton("Save Level");
		edit_level = new JButton("Edit Level");
		back = new JButton("Back");
		game_mode = new JLabel("Game Mode");
		moves = new JLabel("moves");
		target = new JLabel("goal");
		difficulty = new JLabel("Difficulty: Unknown");
		candies = new JLabel("Candies in play");
		ai_strength = new JSlider(1,SimulatedPlayerManager.getMaxAbilityLevel()+1);
	}

	@Override
	protected void setUpItems() {
		play_level.addActionListener(this);
		play_level.setActionCommand("play");
		play_level.setToolTipText("Starts the level for a human to play");
		watch_level.addActionListener(this);
		watch_level.setActionCommand("watch");
		watch_level.setToolTipText("Watch an AI player attempt to play the level");
		save_level.addActionListener(this);
		save_level.setActionCommand("save");
		save_level.setToolTipText("Saves the level to your level list");
		edit_level.addActionListener(this);
		edit_level.setActionCommand("edit");
		edit_level.setToolTipText("Opens the level in the editor");
		back.addActionListener(this);
		back.setActionCommand("back");
		
		title.setAlignmentX(CENTER_ALIGNMENT);
		level_number.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		level_name.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		
		ai_strength.setValue(1);
		ai_strength.setMajorTickSpacing(1);
		ai_strength.setPaintTicks(true);
		ai_strength.setPaintLabels(true);
	}

	private JPanel details;
	private JPanel buttons;
	@Override
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);
		
		//sort out the window's layout settings:
		setLayout(null);
		
		title.add(level_number);
		title.add(level_name);
		
		//make a box with all the custom settings
		details = new JPanel();
		details.setLayout(new BoxLayout(details,BoxLayout.Y_AXIS));
		details.setBorder(BorderFactory.createLineBorder(Color.black));
		details.add(getSpace());
		details.add(game_mode);
		details.add(getSpace());
		details.add(target);
		details.add(getSpace());
		details.add(moves);
		details.add(getSpace());
		details.add(candies);
		details.add(getSpace());
		
		//make a box with the options
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
		buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buttons.add(getSpace());
		buttons.add(play_level);
		buttons.add(getSpace());
		buttons.add(new JLabel("Simulated Player Strength:"));
		buttons.add(ai_strength);
		buttons.add(getSpace());
		buttons.add(watch_level);
		buttons.add(getSpace());
		buttons.add(save_level);
		buttons.add(getSpace());
		buttons.add(edit_level);
		buttons.add(getSpace());
		
		//add everything to the scene
		add(title);
		add(back);
		add(buttons);
		add(board);
		add(details);
	}
	
	@Override
	protected void placeItems() {
		
		//size the fonts
		fontScale(title, DisplayScreen.FONT_TITLE);
		fontScale(details, DisplayScreen.FONT_NORMAL);
		fontScale(buttons, DisplayScreen.FONT_NORMAL);
		fontScale(back, DisplayScreen.FONT_NORMAL);
		
		position(title, 0.35, 0.9, 300, 50);
		position(details, 0.7,0.3,250,150);
		position(buttons,0.7,0.7,250,250);
		positionBoard(board,0.35,0.5);
		position(back,0.1,0.85,100,30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			InterfaceManager.switchScreen(previous_screen);
			break;
		case "play":
			InterfaceManager.setSelectedHumanGame(level);
			InterfaceManager.switchScreen(Windows.HUMAN);
			break;
		case "watch":
			InterfaceManager.setSelectedComputerGame(level,ai_strength.getValue()-1);
			InterfaceManager.switchScreen(Windows.SIMULATED);
			break;
		case "save":
			makeAndSave();
			break;
		case "edit":
			InterfaceManager.setSelectedCDesign(level, level_name.getText(), level_num);
			InterfaceManager.switchScreen(Windows.CREATE);
			break;
		}
	}
	
	private void makeAndSave(){
		String fileName = level_num+". "+level_name.getText();
		boolean success = InterfaceManager.level_manager.saveLevel(fileName, level);
		InterfaceManager.refreshLevelBrowser();
		String message = success?(fileName+".lv Saved!"):("Failed to save.");
		JOptionPane.showMessageDialog(this,message,"Notification",JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	protected void resizeBoards(){
		if(board!=null)board.updateTileSize();
	}
}
