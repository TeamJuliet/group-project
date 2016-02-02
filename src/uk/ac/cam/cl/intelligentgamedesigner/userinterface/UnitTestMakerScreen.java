package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCaseGame;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestLibrary;

//an interface for a human user to manually create a level, to then save
public class UnitTestMakerScreen extends DisplayScreen implements ChangeListener{
	
	//buttons for controlling stuff
	private JButton save_and_quit;
	private JButton just_quit;
	
	private JSlider dimensions_width;
	private JSlider dimensions_height;
	private JSlider dimensions_above;
	JScrollPane infinite_lookahead;
	
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
	
	//state relevant to level creation
	private UnitTestBoard board_before;
	private UnitTestBoard board_after;
	private UnitTestBoard board_above;
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
	private boolean move_place;

	private JTable game_state_stuff;
	private JTextField description;
	
	public UnitTestMakerScreen(){
		super();
		identifier = "Unit Test Maker";
	}

	@Override
	protected void makeItems() {
		save_and_quit = new JButton("Save");
		just_quit = new JButton("Quit");
		
		dimensions_width = new JSlider(5,10);
		dimensions_height = new JSlider(5,10);
		dimensions_above = new JSlider(5,30);
		
		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score",true);
		mode = GameMode.HIGHSCORE;
		jelly = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly);
		game_mode.add(ingredients);
		
		types = new String[]{"Regular Cell","Candies","Objective Pieces","Test Move"};
		cells = new String[]{"Normal","Unusable","Icing","Liquorice","Don't Care"};
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

		String[] column_names = {
				"Game State:", "Before", "After"
		};
		Object[][] data = {
				column_names,
				{"Number of Moves",new Integer(4),new Integer(3)},
				{"Score (-1 is don't care)",new Integer(100),new Integer(200)}
		};
		game_state_stuff = new JTable(data,column_names){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        if(column == 0 || row == 0)return false;
		        return true;
		    }
		};
		game_state_stuff.setBorder(BorderFactory.createLineBorder(Color.black));
		description = new JTextField("This test determines...");

		//The Game Board
		width = 10;
		height = 10;
		above_screen = 5;
		board_before = new UnitTestBoard(width,height,0);
		board_before.watchLevelCreator(this);
		board_after = new UnitTestBoard(width,height,1);
		board_after.watchLevelCreator(this);
		board_above = new UnitTestBoard(width,above_screen,2);
		board_above.watchLevelCreator(this);
		board_before.adjustSize(3);
		board_after.adjustSize(3);
		board_above.adjustSize(3);
		board_before.watchBoard(board_after);
		
		replace_cell = CellType.EMPTY;
		objective_fill = false;
		jelly_fill = false;
		null_fill = false;
	}

	@Override
	protected void setUpItems() {
		save_and_quit.setToolTipText("Saves your level");
		save_and_quit.setActionCommand("save");
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
		dimensions_above.setValue(above_screen);
		dimensions_above.setMajorTickSpacing(5);
		dimensions_above.setMinorTickSpacing(1);
		dimensions_above.setPaintTicks(true);
		dimensions_above.setPaintLabels(true);
		dimensions_above.addChangeListener(this);
		
		selection.setActionCommand("new mode");
		selection.addActionListener(this);
		selection.setSelectedIndex(0);
		fill_type.setActionCommand("new selection");
		fill_type.addActionListener(this);
		fill_type.setSelectedIndex(0);		
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
		settings.add(Box.createRigidArea(new Dimension(0, 10)));
		settings.add(new JLabel("Level Height:"));
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(dimensions_height);
		settings.add(Box.createRigidArea(new Dimension(0, 10)));
		settings.add(new JLabel("Height Above Screen Specified:"));
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(dimensions_above);
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
		save_and_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(save_and_quit);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		just_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(just_quit);	
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		add(controls);
		
		JPanel gameStates = new JPanel();
		gameStates.setBorder(BorderFactory.createLineBorder(Color.black));
		gameStates.setLayout(new BoxLayout(gameStates,BoxLayout.Y_AXIS));
		gameStates.add(new JLabel("Additional Rules:"));
		gameStates.add(Box.createRigidArea(new Dimension(0, 5)));
		gameStates.add(game_state_stuff);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		gameStates.add(new JLabel("Description of Test:"));
		gameStates.add(description);
		add(gameStates);
		
		add(board_before);
		add(board_after);
		
		infinite_lookahead = new JScrollPane(board_above);
		infinite_lookahead.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		infinite_lookahead.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(infinite_lookahead);

		//set the locations
		position(settings,0.5,0.6,300,500);
		position(controls,0.5,0.20,300,100);
		position(infinite_lookahead,0.2,0.8,400,160);
		position(board_before,0.2,0.4,400,400);
		position(board_after,0.8,0.5,400,400);
		position(gameStates,0.8,0.85,400,120);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		
		case "save":
			makeAndSave();
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
				candy_fill = false;
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
				move_place = false;	
				
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
				move_place = false;	
				
				//switch the fill type to cells
				fill_type.setModel(candies_fill);
				fill_type.setSelectedIndex(0);
				replace_candy = new Candy(CandyColour.RED,CandyType.NORMAL);
								
				break;
			case "Objective Pieces":
				objective_fill = true;
				candy_fill = false;
				move_place = false;	
				
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
			case "Test Move":
				objective_fill = false;
				jelly_fill = false;
				null_fill = false;
				candy_fill = false;
				move_place = true;	
				fill_type.setModel(highscore_model);		
				break;
			}
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
			case "Don't Care":
				replace_cell = CellType.DONT_CARE;
				break;
				
			case "Red":
				replace_candy = new Candy(CandyColour.RED,CandyType.NORMAL);
				break;
			case "Yellow":
				replace_candy = new Candy(CandyColour.YELLOW,CandyType.NORMAL);
				break;
			case "Orange":
				replace_candy = new Candy(CandyColour.ORANGE,CandyType.NORMAL);
				break;
			case "Green":
				replace_candy = new Candy(CandyColour.GREEN,CandyType.NORMAL);
				break;
			case "Blue":
				replace_candy = new Candy(CandyColour.BLUE,CandyType.NORMAL);
				break;
			case "Purple":
				replace_candy = new Candy(CandyColour.PURPLE,CandyType.NORMAL);
				break;
			}
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
	    board_before.changeSize(dimensions_width.getValue(), dimensions_height.getValue());
	    board_after.changeSize(dimensions_width.getValue(), dimensions_height.getValue());
	    board_above.changeSize(dimensions_width.getValue(), dimensions_above.getValue());
	}
	
	public boolean canFill() {
		return !null_fill;
	}
	
	public boolean fillingCandies(){
		return candy_fill;
	}
	
	public boolean fillingCells(){
		return !objective_fill && !candy_fill;
	}
	public boolean fillingJellies(){
		return jelly_fill;
	}
	public boolean placingMove(){
		return move_place;
	}
	public CellType getReplacer(){
		return replace_cell;
	}
	public Candy getReplacerCandy(){
		return replace_candy;
	}
	
	private void makeAndSave(){
		TestLibrary.addTest(new TestCaseGame(description.getText(), 
				board_before.getBoard(), 
				board_above.getBoard(), 
				board_after.getBoard(), board_before.getMove()));
		JOptionPane.showMessageDialog(this,"Unit Test Saved!","Notification",JOptionPane.INFORMATION_MESSAGE);
	}
}
