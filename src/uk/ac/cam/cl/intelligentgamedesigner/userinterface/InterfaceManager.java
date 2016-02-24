package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCaseGame;

//This will be used to navigate between the menu screens
//I will also use this for testing the different screens
public class InterfaceManager extends JFrame {
	public static final LevelManager level_manager = new LevelManager();
	
	//Declaring the necessary components
	private static final JFrame screen = new JFrame("Intelligent Game Designer");
	private static final JPanel screens = new JPanel(new CardLayout());	
	private static final CardLayout screenLayout = (CardLayout)screens.getLayout();
	
	private static DisplayScreen main_menu_screen;
	private static DisplayScreen level_requester_screen;
	private static DisplayScreen human_game_display_screen;
	private static DisplayScreen computer_game_display_screen;
	private static DisplayScreen level_browser_screen;
	private static DisplayScreen level_creator_screen;
	private static DisplayScreen design_display_screen;
	private static DisplayScreen designing_level_screen;
	private static DisplayScreen unit_test_screen;
		
	public static int screenWidth(){
		return screen.getWidth();
	}
	public static int screenHeight(){
		return screen.getHeight();
	}
	
	static void initialise(){
		setAppearanceDefaults();
		//set the screen to detect resizing
		screen.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	scaleScreens();
            }
        });
		
		//setting the screen properties
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setPreferredSize(new Dimension(1400,740));
		
		DisplayBoard.loadTextures();
		DisplayScreen.loadFonts();

		initialiseScreens();
	}
	
	private static void initialiseScreens(){
		//intisialise the screens
		main_menu_screen = new MainMenuScreen();
		level_requester_screen = new LevelRequesterScreen();
		human_game_display_screen = new HumanGameDisplayScreen();
		computer_game_display_screen = new ComputerGameDisplayScreen();
		level_browser_screen = new LevelBrowserScreen();
		level_creator_screen = new LevelCreatorScreen();
		design_display_screen = new DesignDisplayScreen();
		designing_level_screen = new DesigningLevelScreen();
		unit_test_screen = new UnitTestMakerScreen();		
		
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
		//Size the frame.
		screen.pack();
		//Resize the images
		scaleScreens();
		//Show it.
		screen.setVisible(true);
	}
	
	private static void scaleScreens(){

		DisplayScreen.reScale();
		main_menu_screen.rePosition();
		level_requester_screen.rePosition();
		human_game_display_screen.rePosition();	
		computer_game_display_screen.rePosition();
		level_browser_screen.rePosition();
		level_creator_screen.rePosition();
		design_display_screen.rePosition();
		designing_level_screen.rePosition();
		unit_test_screen.rePosition();
	}
	
	private static void setAppearanceDefaults(){
		//Create linked list that will store all gradient information
		//You can try to understand it by change it's value
		LinkedList<Object> button_gradient=new LinkedList<Object>();
		button_gradient.add(0.3);
		button_gradient.add(0.3);
		//First colour :
		button_gradient.add(new ColorUIResource(DisplayScreen.BUTTON_COLOUR_TOP));
		button_gradient.add(new ColorUIResource(DisplayScreen.BUTTON_COLOUR_MID));
		button_gradient.add(new ColorUIResource(DisplayScreen.BUTTON_COLOUR_BOTTOM));
		//Set Button.gradient key with new value
		
		//backgrounds
		UIManager.put("Panel.background", DisplayScreen.EXTRA_PANEL);
		UIManager.put("RadioButton.background", DisplayScreen.EXTRA_PANEL);
		UIManager.put("Slider.background", DisplayScreen.EXTRA_PANEL);
		
		//fields
		UIManager.put("TextField.background", DisplayScreen.FIELD_BACK);
		UIManager.put("TextField.selectionBackground", DisplayScreen.EXTRA_PANEL);
		UIManager.put("FormattedTextField.background", DisplayScreen.FIELD_BACK);
		UIManager.put("FormattedTextField.selectionBackground", DisplayScreen.EXTRA_PANEL);
		UIManager.put("Label.background", DisplayScreen.FIELD_BACK);
		UIManager.put("List.background", DisplayScreen.FIELD_BACK);
		UIManager.put("List.selectionBackground", DisplayScreen.EXTRA_PANEL);
		UIManager.put("Table.background", DisplayScreen.FIELD_BACK);
		UIManager.put("Table.selectionBackground", DisplayScreen.EXTRA_PANEL);
		UIManager.put("ComboBox.background", DisplayScreen.FIELD_BACK); //TODO: keep button colour
		UIManager.put("ComboBox.selectionBackground", DisplayScreen.EXTRA_PANEL);
		UIManager.put("ComboBox.buttonShadow", button_gradient); //TODO: keep button colour
		UIManager.put("ProgressBar.background",DisplayScreen.FIELD_BACK);
		UIManager.put("ProgressBar.foreground",DisplayScreen.EXTRA_PANEL);
		UIManager.put("ProgressBar.selectionBackground",DisplayScreen.PROGRESS_BAR_TEXT);
		UIManager.put("ProgressBar.selectionForeground",DisplayScreen.PROGRESS_BAR_TEXT);
		UIManager.put("ProgressBar.border",BorderFactory.createLineBorder(DisplayScreen.PROGRESS_BAR_TEXT));

		//buttons
		UIManager.put("Button.gradient",button_gradient);
		UIManager.put("Button.select",DisplayScreen.EXTRA_PANEL);
		UIManager.put("RadioButton.gradient",button_gradient);
		//add slider, dropdown and scrollpane button colours,
		//add button disabled colour
	}
	
	private static void switchTo(DisplayScreen new_screen){
		
		screenLayout.show(screens, new_screen.identifier);
	}
	
	public static void setSelectedCDesign(Design design, String name, int level_on){
		if(level_on == 0)level_on = level_manager.get_next_num();
		((LevelCreatorScreen)level_creator_screen).reload(design, name, level_on);
	}
	public static void setSelectedDDesign(Design design, String name){
		((DesignDisplayScreen)design_display_screen).reload(design, name);
	}
	public static void refreshLevelBrowser(){
		((LevelBrowserScreen)level_browser_screen).refreshList();
	}
	public static void setSelectedTest(TestCaseGame test){
		((UnitTestMakerScreen)unit_test_screen).reload(test);
	}
	public static void setSelectedHumanGame(Design design){
		((HumanGameDisplayScreen)human_game_display_screen).giveInfo(design);
		((HumanGameDisplayScreen)human_game_display_screen).setInfo();
	}
	public static void setSelectedComputerGame(Design design,int ability){
		((ComputerGameDisplayScreen)computer_game_display_screen).giveInfo(design);
		((ComputerGameDisplayScreen)computer_game_display_screen).setAbility(ability);
		((ComputerGameDisplayScreen)computer_game_display_screen).setInfo();
	}
	public static void setPreviousScreen(Windows window){
		((DesignDisplayScreen)design_display_screen).setPreviousScreen(window);
	}
	public static void setLevelSpecifications(Specification specification){
		((DesigningLevelScreen)designing_level_screen).startDesign(specification);
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
			((HumanGameDisplayScreen)human_game_display_screen).initialiseGame();
			switchTo(human_game_display_screen);
			break;
		case SIMULATED:
			((ComputerGameDisplayScreen)computer_game_display_screen).initialiseGame();
			switchTo(computer_game_display_screen);
			break;
		case CREATE:
			switchTo(level_creator_screen);
			break;
		case LOAD:
			((LevelBrowserScreen) level_browser_screen).refreshList();
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
