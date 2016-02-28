package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * The starting menu. from here you can select:
 * request level
 * create level
 * load level
 * (unit test creator omitted)
 *
 */

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
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);
		
	}
	
	@Override
	protected void placeItems() {	
		
		//size the fonts
		fontScale(title_text, DisplayScreen.FONT_TITLE);
		fontScale(publisher_text, DisplayScreen.FONT_SUBTITLE);
		fontScale(request_level, DisplayScreen.FONT_NORMAL);
		fontScale(create_level, DisplayScreen.FONT_NORMAL);
		fontScale(load_level, DisplayScreen.FONT_NORMAL);
		fontScale(unit_test, DisplayScreen.FONT_NORMAL);
		
		
		//add the items to the screen
		add(title_text);
		add(publisher_text);
		add(request_level);
		add(create_level);
		add(load_level);	
		//add(unit_test);
		
		//set the locations
		position(title_text,0.5,0.9,500,50);
		position(publisher_text,0.5,0.75,200,50);
		position(request_level,0.5,0.6,140,40);
		position(create_level,0.5,0.45,140,40);
		position(load_level,0.5,0.3,140,40);
		//position(unit_test,0.9,0.15,160,40);
	}

	@Override
	//Handle what the buttons do (switch to different windows)
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "request":
	    	InterfaceManager.switchScreen(Windows.REQUEST);
			break;
		case "create":
			InterfaceManager.setSelectedCDesign(null,null, 0);
	    	InterfaceManager.switchScreen(Windows.CREATE);
			break;
		case "load":
	    	InterfaceManager.switchScreen(Windows.LOAD);
			break;
			
		case "unit":
			InterfaceManager.setSelectedTest(null);
			InterfaceManager.switchScreen(Windows.UNIT_TEST);
			break;
		}
	} 
}
