package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextField;

//The starting menu. from here you can select:
//request level
//create level
//load level
public class MainMenuScreen extends DisplayScreen{
	
	JTextField title_text;
	JTextField publisher_text;
	JButton request_level;
	JButton create_level;
	JButton load_level;
			
	public MainMenuScreen(){
		super(); 
		identifier = "Main Menu";
		
		title_text = new JTextField("Candy Crush Intelligent Level Designer");
		title_text.setEditable(false);
		publisher_text = new JTextField("Team Juliet");
		publisher_text.setEditable(false);
		
		request_level = new JButton("Request Level");
		create_level = new JButton("Create Level");
		load_level = new JButton("Saved Levels");
		
		draw();
	}
	protected void draw(){
		add(title_text);
		add(publisher_text);
		add(request_level);
		add(create_level);
		add(load_level);
	}
}
