package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

//The menu from which you can request levels.
public class LevelRequesterScreen extends DisplayScreen{
	JButton back_button;
	JButton go_button;
	
	ButtonGroup game_mode;
	JRadioButton high_score;
	JRadioButton jelly_clear;
	JRadioButton ingredients;

	JTextField game_mode_text;
	JTextField difficulty_text;
	
	JSlider difficulty;
	
	
	
	public LevelRequesterScreen(){
		super();
		identifier = "Level Requester";
		
		back_button = new JButton("Back");
		go_button = new JButton("Go");
		
		game_mode_text = new JTextField("Select Game Mode:");
		game_mode_text.setEditable(false);
		difficulty_text = new JTextField("Select Difficulty:");
		difficulty_text.setEditable(false);
		
		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score");
		jelly_clear = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly_clear);
		game_mode.add(ingredients);
		
		difficulty = new JSlider();
		
		draw();
	}
	protected void draw(){
		add(back_button);
		add(high_score);
		add(game_mode_text);
		add(jelly_clear);
		add(ingredients);
		add(difficulty_text);
		add(difficulty);
		add(go_button);
	}
}
