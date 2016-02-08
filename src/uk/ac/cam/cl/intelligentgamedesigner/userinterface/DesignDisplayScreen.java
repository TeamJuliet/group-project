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

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

public class DesignDisplayScreen extends DisplayScreen{
	private JLabel title;
	private GameBoard board;
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

	GameMode mode;
	int number_of_moves;
	int objective_value;
	int number_of_candies;

	public DesignDisplayScreen(){
		super();
		identifier = "Design Display";
	}
	
	public void reload(Design design, String name){
		title.setText(name);
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
			target.setText("Gather all "+objective_value+" ingrediens!");
			break;
		}
		number_of_moves = design.getNumberOfMovesAvailable();
		moves.setText("Moves allowed: "+number_of_moves);
		number_of_candies = design.getNumberOfCandyColours();
		candies.setText(number_of_candies+" candy colours in play");
		board.setBoard(design.getBoard());
	}

	@Override
	protected void makeItems() {
		//initialise with some noncommittal information
		title = new JLabel("title");
		board = new GameBoard(new Design());
		play_level = new JButton("Play Level");
		watch_level = new JButton("Watch Level");
		save_level = new JButton("Save Level");
		edit_level = new JButton("Edit Level");
		back = new JButton("back");
		game_mode = new JLabel("Game Mode");
		moves = new JLabel("moves");
		target = new JLabel("goal");
		difficulty = new JLabel("Difficulty: Unknown");
		candies = new JLabel("Candies in play");
		ai_strength = new JSlider(1,5);
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
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		
		ai_strength.setValue(3);
		ai_strength.setMajorTickSpacing(1);
		ai_strength.setPaintTicks(true);
		ai_strength.setPaintLabels(true);
	}

	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		//make a box with all the custom settings
		JPanel details = new JPanel();
		details.setLayout(new BoxLayout(details,BoxLayout.Y_AXIS));
		details.setBorder(BorderFactory.createLineBorder(Color.black));
		details.add(Box.createRigidArea(new Dimension(0, 10)));
		details.add(game_mode);
		details.add(Box.createRigidArea(new Dimension(0, 10)));
		details.add(target);
		details.add(Box.createRigidArea(new Dimension(0, 10)));
		details.add(moves);
		details.add(Box.createRigidArea(new Dimension(0, 10)));
		
		//make a box with the options
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
		buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		buttons.add(play_level);
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		buttons.add(new JLabel("Simulated Player Strength:"));
		buttons.add(ai_strength);
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		buttons.add(watch_level);
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		buttons.add(save_level);
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		buttons.add(edit_level);
		buttons.add(Box.createRigidArea(new Dimension(0, 10)));
		
		//add everything to the scene
		add(title);
		add(back);
		add(buttons);
		add(board);
		add(details);
		
		position(title, 0.5, 0.9, 400, 30);
		position(details, 0.7,0.4,300,300);
		position(buttons,0.7,0.8,300,300);
		position(board,0.4,0.3,800,800);
		position(back,0.1,0.85,100,30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "play":
			InterfaceManager.setSelectedGame(
					board.getBoard(),
					objective_value,
					number_of_moves,
					mode,
					number_of_candies,
					0
					);
			InterfaceManager.switchScreen(Windows.HUMAN);
			break;
		case "watch":
			InterfaceManager.switchScreen(Windows.SIMULATED);
			break;
		case "save":
			makeAndSave();
			break;
		}
	}
	
	private void makeAndSave(){
		Design level = new Design();
		
		level.setBoard(board.getBoard());
		level.setSize(board.width, board.height);
		level.setRules(mode, number_of_moves, objective_value, number_of_candies);
		
		boolean success = InterfaceManager.level_manager.saveLevel(title.getText(), level);
		String message = success?(title.getText()+" Saved!"):("Failed to save.");
		JOptionPane.showMessageDialog(this,message,"Notification",JOptionPane.INFORMATION_MESSAGE);
	}
}
