package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

//an interface for a human user to manually create a level, to then save
public class UnitTestMakerScreen extends DisplayScreen implements ChangeListener{
	
	//buttons for controlling stuff
	private JButton save_and_quit;
	private JButton just_quit;
	
	private JSlider dimensions_width;
	private JSlider dimensions_height;
	
	private ButtonGroup game_mode;
	private JRadioButton high_score;
	private JRadioButton jelly;
	private JRadioButton ingredients;
	
	private String[] cells;
	private String[] candies;
	private String[] highscore_specials;
	private String[] jelly_specials;
	private String[] ingredients_specials;
	private String[] types;
	private JComboBox<String> selection;
	private ComboBoxModel<String> cells_fill;
	private ComboBoxModel<String> candies_fill;
	private ComboBoxModel<String> highscore_model;
	private ComboBoxModel<String> jelly_model;
	private ComboBoxModel<String> ingredients_model;
	private JComboBox<String> fill_type;

	private JSlider moves;
	
	//state relevant to level creation
	private UnitTestBoard board_before;
	private UnitTestBoard board_after;
	private int width;
	private int height;
	private int above_screen;
	private GameMode mode;
	
	private CellType replace_cell;
	private Candy replace_candy;
	private boolean objective_fill;
	private boolean candy_fill;
	private boolean jelly_fill;
	private boolean null_fill;
	
	private int number_of_moves;
	
	public UnitTestMakerScreen(){
		super();
		identifier = "Unit Test Maker";
	}

	@Override
	protected void makeItems() {
		save_and_quit = new JButton("Save and Quit");
		just_quit = new JButton("Quit without saving");
		
		dimensions_width = new JSlider(5,10);
		dimensions_height = new JSlider(5,10);
		
		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score",true);
		mode = GameMode.HIGHSCORE;
		jelly = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly);
		game_mode.add(ingredients);
		
		types = new String[]{"Regular Cell","Candies","Objective Pieces"};
		cells = new String[]{"Normal","Unusable","Icing","Liquorice"};
		candies = new String[] {"Red","Orange","Yellow","Green","Blue","Purple"};
		highscore_specials = new String[]{"<None>"};
		jelly_specials = new String[]{"Jelly Level"};
		ingredients_specials = new String[]{"Ingredient"};
		selection = new JComboBox<String>(types);
		cells_fill = new DefaultComboBoxModel<String>(cells);
		candies_fill = new DefaultComboBoxModel<String>(candies);
		fill_type = new JComboBox<String>();
		highscore_model = new DefaultComboBoxModel<String>(highscore_specials);
		jelly_model = new DefaultComboBoxModel<String>(jelly_specials);
		ingredients_model = new DefaultComboBoxModel<String>(ingredients_specials);
		fill_type.setModel(cells_fill);

		moves = new JSlider(5,99);

		//The Game Board
		width = 10;
		height = 10;
		above_screen = 5;
		board_before = new UnitTestBoard(width,height+above_screen);
		board_before.watchLevelCreator(this);
		board_after = new UnitTestBoard(width,height+above_screen);
		board_after.watchLevelCreator(this);
		number_of_moves = 10;
		
		replace_cell = CellType.EMPTY;
		objective_fill = false;
		jelly_fill = false;
		null_fill = false;
	}

	@Override
	protected void setUpItems() {
		save_and_quit.setToolTipText("Saves your level and returns to the previous screen");
		save_and_quit.setActionCommand("save and quit");
		save_and_quit.addActionListener(this);
		just_quit.setToolTipText("Warning: unsaved progress will be lost.");
		just_quit.setActionCommand("quit");
		just_quit.addActionListener(this);
		
		high_score.setActionCommand("high score");
		high_score.addActionListener(this);
		jelly.setActionCommand("jelly");
		jelly.addActionListener(this);
		ingredients.setActionCommand("ingredients");
		ingredients.addActionListener(this);
		
		dimensions_width.setValue(width);
		dimensions_width.setMajorTickSpacing(1);
		dimensions_width.setPaintTicks(true);
		dimensions_width.setPaintLabels(true);
		dimensions_width.addChangeListener(this);
		dimensions_height.setValue(height);
		dimensions_height.setMajorTickSpacing(1);
		dimensions_height.setPaintTicks(true);
		dimensions_height.setPaintLabels(true);
		dimensions_height.addChangeListener(this);

		
		selection.setActionCommand("new mode");
		selection.addActionListener(this);
		selection.setSelectedIndex(0);
		fill_type.setActionCommand("new selection");
		fill_type.addActionListener(this);
		fill_type.setSelectedIndex(0);
		
		moves.setValue(number_of_moves);
		
		
	}

	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		//make a box with all the custom settings
		JPanel settings = new JPanel();
		settings.setLayout(new BoxLayout(settings,BoxLayout.Y_AXIS));
		settings.setBorder(BorderFactory.createLineBorder(Color.black));
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		settings.add(new JLabel("Tile Type:"));
		settings.add(Box.createRigidArea(new Dimension(0, 10)));
		settings.add(selection);
		settings.add(Box.createRigidArea(new Dimension(0, 10)));
		settings.add(fill_type);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		settings.add(new JLabel("Level Width:"));
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(dimensions_width);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		settings.add(new JLabel("Level Height:"));
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(dimensions_height);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		settings.add(new JLabel("Select a Game Mode:"));
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(high_score);
		settings.add(jelly);
		settings.add(ingredients);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		add(settings);

		//make a box with all the controls
		JPanel controls = new JPanel();
		controls.setBorder(BorderFactory.createLineBorder(Color.black));
		controls.setLayout(new BoxLayout(controls,BoxLayout.Y_AXIS));
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		save_and_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(save_and_quit);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		just_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(just_quit);	
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		add(controls);
		
		add(board_before);
		add(board_after);

		//set the locations
		position(settings,0.5,0.7,300,400);
		position(controls,0.5,0.25,300,200);
		position(board_before,0.9,0.2,500,1000);
		position(board_after,0.3,0.2,500,1000);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		
		case "save":
			break;
		case "analyse":
			break;
		case "save and quit":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "quit":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
			
		case "high score":
			mode = GameMode.HIGHSCORE;
			board_before.changeMode(mode);
			board_after.changeMode(mode);
			
			if(objective_fill){
				null_fill = true;
				jelly_fill = false;
				fill_type.setModel(highscore_model);				
			}
			break;
		case "jelly":
			mode = GameMode.JELLY;
			board_before.changeMode(mode);
			board_after.changeMode(mode);
			
			if(objective_fill){
				null_fill = false;
				jelly_fill = true;
				fill_type.setModel(jelly_model);				
			}
			break;
		case "ingredients":
			mode = GameMode.INGREDIENTS;
			board_before.changeMode(mode);
			board_after.changeMode(mode);
			
			if(objective_fill){
				null_fill = false;
				jelly_fill = false;
				candy_fill = false;
				fill_type.setModel(ingredients_model);				
			}
			break;

			
		case "new mode":
			switch((String)selection.getSelectedItem()){
			case "Regular Cell":
				objective_fill = false;
				jelly_fill = false;
				null_fill = false;
				candy_fill = false;
				
				//switch the fill type to cells
				fill_type.setModel(cells_fill);
				fill_type.setSelectedIndex(0);
				replace_cell = CellType.EMPTY;
								
				break;
			case "Candies":
				objective_fill = false;
				jelly_fill = false;
				null_fill = false;
				candy_fill = true;
				
				//switch the fill type to cells
				fill_type.setModel(candies_fill);
				fill_type.setSelectedIndex(0);
				replace_candy = new Candy(CandyColour.RED,CandyType.NORMAL);
								
				break;
			case "Objective Pieces":
				objective_fill = true;
				
				//switch the fill type to misc
				switch(mode){
				case HIGHSCORE:
					null_fill = true;
					jelly_fill = false;
					fill_type.setModel(highscore_model);
					break;
				case JELLY:
					null_fill = false;
					jelly_fill = true;
					fill_type.setModel(jelly_model);
					break;
				case INGREDIENTS:
					null_fill = false;
					jelly_fill = false;
					fill_type.setModel(ingredients_model);
					break;
				}
				fill_type.setSelectedIndex(0);
								
				break;
			}
			//System.out.println((String)fill_type.getSelectedItem());
			break;
		case "new selection":
			switch((String)fill_type.getSelectedItem()){
			case "Normal":
				replace_cell = CellType.EMPTY;
				break;
			case "Unusable":
				replace_cell = CellType.UNUSABLE;
				break;
			case "Icing":
				replace_cell = CellType.ICING;
				break;
			case "Liquorice":
				replace_cell = CellType.LIQUORICE;
				break;
			}
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
	    board_before.changeSize(dimensions_width.getValue(), dimensions_height.getValue());
	    board_after.changeSize(dimensions_width.getValue(), dimensions_height.getValue());
	}
	
	public boolean canFill() {
		return !null_fill;
	}
	
	public boolean fillingCandies(){
		return candy_fill;
	}
	
	public boolean fillingCells(){
		return !objective_fill;
	}
	public boolean fillingJellies(){
		return jelly_fill;
	}
	public CellType getReplacer(){
		return replace_cell;
	}
	
	private void makeDesign(){
		Design level = new Design();
		level.setBoard(board_before.getBoard());
		level.setRules(mode, number_of_moves);
	}
}
