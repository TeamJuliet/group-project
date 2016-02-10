package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;

//The menu from which you can request levels.
public class LevelRequesterScreen extends DisplayScreen implements ChangeListener{
	
	//buttons etc.
	JButton back_button;
	JButton go_button;
	
	ButtonGroup game_mode;
	JRadioButton high_score;
	JRadioButton jelly_clear;
	JRadioButton ingredients;

	JLabel title;
	
	JLabel game_mode_text;
	JLabel difficulty_text;
	JLabel show_difficulty;
	
	JSlider difficulty;
	
	//specified values
	double val_difficulty;
	
	public LevelRequesterScreen(){
		super();
		identifier = "Level Requester";
	}
	
	@Override
	protected void makeItems() {
		back_button = new JButton("Back");
		go_button = new JButton("Go");	

		title = new JLabel("Choose the type of level to make",SwingConstants.CENTER);
		game_mode_text = new JLabel("Select Game Mode",SwingConstants.CENTER);
		difficulty_text = new JLabel("Select Difficulty",SwingConstants.CENTER);
		show_difficulty = new JLabel("%",SwingConstants.CENTER);

		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score",true);
		jelly_clear = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly_clear);
		game_mode.add(ingredients);
		
		difficulty = new JSlider(0,100);
	}
	
	@Override
	protected void setUpItems() {
		back_button.setActionCommand("back");
		back_button.addActionListener(this);
		go_button.setActionCommand("go");
		go_button.addActionListener(this);
		
		val_difficulty = 0.5;
		show_difficulty.setText("Middling");
		difficulty.setValue(50);
		difficulty.setMajorTickSpacing(10);
		difficulty.setMinorTickSpacing(10);
		difficulty.setPaintTicks(true);
		difficulty.setPaintLabels(true);
		difficulty.addChangeListener(this);
		
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 22));
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		high_score.setAlignmentX(CENTER_ALIGNMENT);
		jelly_clear.setAlignmentX(CENTER_ALIGNMENT);
		ingredients.setAlignmentX(CENTER_ALIGNMENT);
		
		game_mode_text.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		game_mode_text.setAlignmentX(CENTER_ALIGNMENT);
		difficulty_text.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		difficulty_text.setAlignmentX(CENTER_ALIGNMENT);
		show_difficulty.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		show_difficulty.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		//make a box with the settings
		JPanel settings = new JPanel();
		settings.setLayout(new BoxLayout(settings,BoxLayout.Y_AXIS));
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		settings.add(game_mode_text);
		settings.add(Box.createRigidArea(new Dimension(0, 10)));
		settings.add(high_score);
		settings.add(jelly_clear);
		settings.add(ingredients);
		settings.add(Box.createRigidArea(new Dimension(0, 40)));
		settings.add(difficulty_text);
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(difficulty);
		settings.add(Box.createRigidArea(new Dimension(0, 15)));
		settings.add(show_difficulty);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		add(settings);
		
		//add the items
		add(title);
		add(back_button);
		add(go_button);
		add(settings);
		
		//set the locations
		position(title,0.5,0.9,400,50);
		position(go_button,0.5,0.3,200,50);
		position(back_button,0.1,0.85,150,30);
		position(settings,0.5,0.5,400,400);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
	    	InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "go":
			GameMode mode = GameMode.HIGHSCORE;
			if(jelly_clear.isSelected())mode = GameMode.JELLY;
			if(ingredients.isSelected())mode = GameMode.INGREDIENTS;
			InterfaceManager.setLevelSpecifications(new Specification(val_difficulty,mode));
	    	InterfaceManager.switchScreen(Windows.REQUESTING);
			break;
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    val_difficulty = ((double)source.getValue())/100;
	    String difficulty_measure = null;
	    if(val_difficulty<0.10)difficulty_measure = "Trivial";
	    if(val_difficulty>=0.10 && val_difficulty<0.20)difficulty_measure = "Simples";
	    if(val_difficulty>=0.20 && val_difficulty<0.30)difficulty_measure = "Easy";
	    if(val_difficulty>=0.30 && val_difficulty<0.40)difficulty_measure = "Nice";
	    if(val_difficulty>=0.40 && val_difficulty<0.50)difficulty_measure = "Lower Normal";
	    if(val_difficulty==0.50)difficulty_measure = "Middling";
	    if(val_difficulty>0.50 && val_difficulty<=0.60)difficulty_measure = "Upper Normal";
	    if(val_difficulty>0.60 && val_difficulty<=0.70)difficulty_measure = "Challenging";
	    if(val_difficulty>0.70 && val_difficulty<=0.80)difficulty_measure = "Hard";
	    if(val_difficulty>0.80 && val_difficulty<=0.90)difficulty_measure = "Very Hard";
	    if(val_difficulty>0.90)difficulty_measure = "Holy Moley";
	    show_difficulty.setText(difficulty_measure);
	}
}
