package uk.ac.cam.cl.intelligentgamedesigner.gameinterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

//This will be used to navigate between the menu screens
//I will also use this for testing the different screens
public class InterfaceManager extends JFrame {
	
	//Declaring the necessary components
	static JFrame screen;
	static JPanel screens;
	static DisplayScreen main_menu_screen = new MainMenuScreen();
	static DisplayScreen level_requester_screen = new LevelRequesterScreen();
	static CardLayout screenLayout;
	
	static void initialise(){
		//setting the screen properties
		screen = new JFrame("Intelligent Game Designer");
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//creating the different screens
		main_menu_screen = new MainMenuScreen();
		level_requester_screen = new LevelRequesterScreen();
		
		//using CardLayout to allow for switching between screens
		screens = new JPanel(new CardLayout());		
		screenLayout = (CardLayout)screens.getLayout();
		screens.add(main_menu_screen, main_menu_screen.getIdentifier());
		screens.add(level_requester_screen, level_requester_screen.getIdentifier());
		
	}
	
	static void setScreen(){
		screen.getContentPane().add(screens, BorderLayout.CENTER);
		//4. Size the frame.
		screen.pack();
		//5. Show it.
		screen.setVisible(true);
	}
	
	static void switchScreen(DisplayScreen new_screen){
		screenLayout.show(screens, new_screen.identifier);
	}
	
	public static void main(String[] args){
		initialise();
		setScreen();
		
		switchScreen(level_requester_screen);
	}

}
