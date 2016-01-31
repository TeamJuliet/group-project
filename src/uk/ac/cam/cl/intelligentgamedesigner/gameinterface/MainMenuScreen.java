package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

//The starting menu. from here you can select:
//request level
//create level
//load level
public class MainMenuScreen extends DisplayScreen{
	
	//Declaring the buttons
	private JLabel title_text;
	private JLabel publisher_text;
	private JButton request_level;
	private JButton create_level;
	private JButton load_level;
	
	private JButton unit_test;
			
	public MainMenuScreen(){
		super(); 
		identifier = "Main Menu";
	}
	
	@Override
	protected void makeItems(){
		//Create the buttons etc.
		title_text = new JLabel("Candy Crush Intelligent Level Designer",SwingConstants.CENTER);
		publisher_text = new JLabel("Team Juliet",SwingConstants.CENTER);
		
		request_level = new JButton("Request Level");
		create_level = new JButton("Create Level");
		load_level = new JButton("Saved Levels");
		
		unit_test = new JButton("Make Unit Test");

		
	}
	
	@Override
	protected void setUpItems(){	
		title_text.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 22));
		title_text.setAlignmentX(CENTER_ALIGNMENT);
		publisher_text.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		
		request_level.setActionCommand("request");
		request_level.addActionListener(this);
		create_level.setActionCommand("create");
		create_level.addActionListener(this);
		load_level.setActionCommand("load");
		load_level.addActionListener(this);	
		unit_test.setActionCommand("unit");
		unit_test.addActionListener(this);		
	}
	
	@Override
	protected void placeItems() {		
		//sort out the window's layout settings:
		setLayout(null);
		
		//add the items to the screen
		add(title_text);
		add(publisher_text);
		add(request_level);
		add(create_level);
		add(load_level);	
		add(unit_test);
		
		//set the locations
		position(title_text,0.5,0.9,500,50);
		position(publisher_text,0.5,0.8,200,50);
		position(request_level,0.5,0.6,200,50);
		position(create_level,0.5,0.45,200,50);
		position(load_level,0.5,0.3,200,50);
		position(unit_test,0.9,0.15,200,30);
	}

	@Override
	//Handle what the buttons do (switch to different windows)
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "request":
	    	InterfaceManager.switchScreen(Windows.REQUEST);
			break;
		case "create":
	    	InterfaceManager.switchScreen(Windows.CREATE);
			break;
		case "load":
	    	InterfaceManager.switchScreen(Windows.LOAD);
			break;
			
		case "unit":
			InterfaceManager.switchScreen(Windows.UNIT_TEST);
			break;
		}
	} 
}
