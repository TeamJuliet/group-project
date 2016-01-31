package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

//This will be used to navigate between the menu screens
//I will also use this for testing the different screens
public class InterfaceManager extends JFrame {
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	//Declaring the necessary components
	private static final JFrame screen = new JFrame("Intelligent Game Designer");
	private static final JPanel screens = new JPanel(new CardLayout());	
	private static final CardLayout screenLayout = (CardLayout)screens.getLayout();
	
	private static final DisplayScreen main_menu_screen = new MainMenuScreen();
	private static final DisplayScreen level_requester_screen = new LevelRequesterScreen();
	private static final DisplayScreen human_game_display_screen = new HumanGameDisplayScreen();
	private static final DisplayScreen computer_game_display_screen = new ComputerGameDisplayScreen();
	private static final DisplayScreen level_browser_screen = new LevelBrowserScreen();
	private static final DisplayScreen level_creator_screen = new LevelCreatorScreen();
	private static final DisplayScreen design_display_screen = new DesignDisplayScreen();
	private static final DisplayScreen designing_level_screen = new DesigningLevelScreen();
	
	private static final DisplayScreen unit_test_screen = new UnitTestMakerScreen();
	
	public static int screenWidth(){
		return screenSize.width;
	}
	public static int screenHeight(){
		return screenSize.height;
	}
	
	static void initialise(){
		//setting the screen properties
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//using CardLayout to allow for switching between screens
		screens.add(main_menu_screen, main_menu_screen.getIdentifier());
		screens.add(level_requester_screen, level_requester_screen.getIdentifier());
		screens.add(human_game_display_screen, human_game_display_screen.getIdentifier());
		screens.add(computer_game_display_screen, computer_game_display_screen.getIdentifier());
		screens.add(level_browser_screen, level_browser_screen.getIdentifier());
		screens.add(level_creator_screen, level_creator_screen.getIdentifier());
		screens.add(design_display_screen, design_display_screen.getIdentifier());
		screens.add(designing_level_screen, designing_level_screen.getIdentifier());
		
		screens.add(unit_test_screen, unit_test_screen.getIdentifier());
	}
	
	static void setScreen(){
		screen.getContentPane().add(screens, BorderLayout.CENTER);
		//4. Size the frame.
		screen.pack();
		//5. Show it.
		screen.setVisible(true);
	}
	
	private static void switchTo(DisplayScreen new_screen){
		screenLayout.show(screens, new_screen.identifier);
	}
	
	public static void switchScreen(Windows window){
		switch(window){
		case MAIN:
			switchTo(main_menu_screen);
			break;
		case REQUEST:
			switchTo(level_requester_screen);
			break;
		case HUMAN:
			switchTo(human_game_display_screen);
			break;
		case SIMULATED:
			switchTo(computer_game_display_screen);
			break;
		case CREATE:
			switchTo(level_creator_screen);
			break;
		case LOAD:
			switchTo(level_browser_screen);
			break;
		case DISPLAY:
			switchTo(design_display_screen);
			break;
		case REQUESTING:
			switchTo(designing_level_screen);
			break;
			
		case UNIT_TEST:
			switchTo(unit_test_screen);
			break;
		}
		
	}
	
	public static void main(String[] args){
		initialise();
		setScreen();
		
		switchScreen(Windows.MAIN);
	}

}
