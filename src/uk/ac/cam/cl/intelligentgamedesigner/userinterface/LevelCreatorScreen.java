package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

//an interface for a human user to manually create a level, to then save
public class LevelCreatorScreen extends DisplayScreen implements ChangeListener{
	
	//buttons for controlling stuff
	private JButton save_and_quit;
	private JButton just_quit;
	private JButton just_save;
	private JButton analyse;
	private JButton reset_board;
	
	private JSlider dimensions_width;
	private JSlider dimensions_height;
	
	private ButtonGroup game_mode;
	private JRadioButton high_score;
	private JRadioButton jelly;
	private JRadioButton ingredients;
	
	private String[] cells;
	private String[] highscore_specials;
	private String[] jelly_specials;
	private String[] ingredients_specials;
	private String[] types;
	private JComboBox<String> selection;
	private ComboBoxModel<String> cells_fill;
	private ComboBoxModel<String> highscore_model;
	private ComboBoxModel<String> jelly_model;
	private ComboBoxModel<String> ingredients_model;
	private JComboBox<String> fill_type;
	
	private JLabel level_on_label;

	private String[] objectives;
	private JFormattedTextField moves;
	private JFormattedTextField mode_objective;
	private JLabel what_objective;

	private int level_on;
	private JFormattedTextField level_name;

	private JSlider number_of_candies;
	
	//state relevant to level creation
	private CustomBoard board;
	private int width;
	private int height;
	private GameMode mode;
	
	private CellType replace_cell;
	private boolean objective_fill;
	private boolean jelly_fill;
	private boolean null_fill;
	
	public LevelCreatorScreen(){
		super();
		identifier = "Level Creator";
	}
	
	public void reload(Design design, String name, int level_on){
		if(design == null){
			board.setBoard(DisplayBoard.blank_board());
			level_name.setValue("the name");
			number_of_candies.setValue(6);
			moves.setValue(10);
			mode_objective.setValue(100);
			high_score.setSelected(true);
		}
		else {
			board.setBoard(design.getBoard());
			number_of_candies.setValue(design.getNumberOfCandyColours());
			moves.setValue(design.getNumberOfMovesAvailable());
			level_name.setValue(name);
			mode_objective.setValue(design.getObjectiveTarget());
			GameMode obj = design.getMode();
			switch(obj){
			case HIGHSCORE:
				high_score.setSelected(true);
				break;
			case JELLY:
				jelly.setSelected(true);
				break;
			case INGREDIENTS:
				ingredients.setSelected(true);
				break;
			}
		}
		this.level_on = level_on;
		level_on_label.setText(level_on+". ");
		width = board.width;
		height = board.height;
		dimensions_width.setValue(width);
		dimensions_height.setValue(height);
	}

	@Override
	protected void makeItems() {
		save_and_quit = new JButton("Save and Quit");
		just_quit = new JButton("Quit without saving");
		analyse = new JButton("Analyse Level");
		just_save = new JButton("Save Level");
		reset_board = new JButton("Clear Board");
		
		dimensions_width = new JSlider(5,10);
		dimensions_height = new JSlider(5,10);

		number_of_candies = new JSlider(4,6);
		
		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score",true);
		mode = GameMode.HIGHSCORE;
		jelly = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly);
		game_mode.add(ingredients);
		
		types = new String[]{"Regular Cell","Objective Pieces"};
		cells = new String[]{"Normal","Unusable","Icing","Liquorice"};
		highscore_specials = new String[]{"<None>"};
		jelly_specials = new String[]{"Jelly Level"};
		ingredients_specials = new String[]{"Ingredient"};
		selection = new JComboBox<String>(types);
		cells_fill = new DefaultComboBoxModel<String>(cells);
		fill_type = new JComboBox<String>();
		highscore_model = new DefaultComboBoxModel<String>(highscore_specials);
		jelly_model = new DefaultComboBoxModel<String>(jelly_specials);
		ingredients_model = new DefaultComboBoxModel<String>(ingredients_specials);
		fill_type.setModel(cells_fill);
		
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(0);
	    nf.setMaximumIntegerDigits(9);
	    
		moves = new JFormattedTextField(nf);
		mode_objective = new JFormattedTextField(nf);
		objectives = new String[]{"High Score Target","No additional objective","Ingredients Target"};
		what_objective = new JLabel(objectives[0]);
		
		level_on_label = new JLabel(level_on+". ");

		//The Game Board
		width = 10;
		height = 10;
		board = new CustomBoard(width,height);
		board.watchLevelCreator(this);
		
		replace_cell = CellType.EMPTY;
		objective_fill = false;
		jelly_fill = false;
		null_fill = false;
		
		level_on = InterfaceManager.level_manager.get_next_num();
		level_name = new JFormattedTextField();
		level_name.setValue("the name");
	}

	@Override
	protected void setUpItems() {
		save_and_quit.setToolTipText("Saves your level and returns to the previous screen");
		save_and_quit.setActionCommand("save and quit");
		save_and_quit.addActionListener(this);
		just_save.setToolTipText("Saves your level");
		just_save.setActionCommand("save");
		just_save.addActionListener(this);
		just_quit.setToolTipText("Warning: unsaved progress will be lost.");
		just_quit.setActionCommand("quit");
		just_quit.addActionListener(this);
		analyse.setToolTipText("Runs the simulated players on the level to get an estimated difficulty");
		analyse.setActionCommand("analyse");
		analyse.addActionListener(this);
		reset_board.setToolTipText("Clears the game board entirely");
		reset_board.setActionCommand("reset");
		reset_board.addActionListener(this);
		
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
		
		number_of_candies.setValue(6);
		number_of_candies.setMajorTickSpacing(1);
		number_of_candies.setPaintTicks(true);
		number_of_candies.setPaintLabels(true);		

		selection.setActionCommand("new mode");
		selection.addActionListener(this);
		selection.setSelectedIndex(0);
		fill_type.setActionCommand("new selection");
		fill_type.addActionListener(this);
		fill_type.setSelectedIndex(0);
		
		moves.setValue(10);
		mode_objective.setValue(100);
		
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
		settings.add(new JLabel("Number of moves:"));
		settings.add(moves);
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(what_objective);
		settings.add(mode_objective);
		settings.add(Box.createRigidArea(new Dimension(0, 5)));
		settings.add(new JLabel("Number of candy types:"));
		settings.add(number_of_candies);
		settings.add(Box.createRigidArea(new Dimension(0, 20)));
		add(settings);

		//make a box with all the controls
		JPanel controls = new JPanel();
		controls.setBorder(BorderFactory.createLineBorder(Color.black));
		controls.setLayout(new BoxLayout(controls,BoxLayout.Y_AXIS));
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		just_save.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(just_save);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		analyse.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(analyse);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		save_and_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(save_and_quit);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		just_quit.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(just_quit);	
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		reset_board.setAlignmentX(CENTER_ALIGNMENT);
		controls.add(reset_board);	
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		add(controls);
		
		//make a title box
		JPanel title = new JPanel();
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		title.setLayout(new BoxLayout(title,BoxLayout.X_AXIS));
		title.add(level_on_label);
		title.add(level_name);
		add(title);
		
		add(board);

		//set the locations
		position(settings,0.15,0.5,300,600);
		position(controls,0.85,0.5,300,300);
		position(board,0.65,0.2,900,1000);
		position(title,0.5,0.9,200,40);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		
		case "save":
			makeAndSave();
			break;
		case "analyse":
			break;
		case "save and quit":
			makeAndSave();
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "quit":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "reset":
			board.clearBoard();
			break;
			
		case "high score":
			mode = GameMode.HIGHSCORE;
			board.changeMode(mode);
			
			//set the objective text
			what_objective.setText(objectives[0]);
			mode_objective.setEditable(true);
			mode_objective.setValue(100);
			
			if(objective_fill){
				null_fill = true;
				jelly_fill = false;
				fill_type.setModel(highscore_model);				
			}
			break;
		case "jelly":
			mode = GameMode.JELLY;
			board.changeMode(mode);
			
			//set the objective text
			what_objective.setText(objectives[1]);
			mode_objective.setEditable(false);
			mode_objective.setValue(0);
			
			if(objective_fill){
				null_fill = false;
				jelly_fill = true;
				fill_type.setModel(jelly_model);				
			}
			break;
		case "ingredients":
			mode = GameMode.INGREDIENTS;
			board.changeMode(mode);
			
			//set the objective text
			what_objective.setText(objectives[2]);
			mode_objective.setEditable(true);
			mode_objective.setValue(1);
			
			if(objective_fill){
				null_fill = false;
				jelly_fill = false;
				fill_type.setModel(ingredients_model);				
			}
			break;

			
		case "new mode":
			switch((String)selection.getSelectedItem()){
			case "Regular Cell":
				objective_fill = false;
				jelly_fill = false;
				null_fill = false;
				
				//switch the fill type to cells
				fill_type.setModel(cells_fill);
				fill_type.setSelectedIndex(0);
				replace_cell = CellType.EMPTY;
								
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
	    JSlider source = (JSlider)e.getSource();
	    board.changeSize(dimensions_width.getValue(), dimensions_height.getValue());
	}
	
	public boolean canFill() {
		return !null_fill;
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
	
	private void makeAndSave(){
		Design level = new Design();
		
		board.minimumBoundingBox();
		
		int temp_width = board.width;
		int temp_height = board.height;
		dimensions_width.setValue(temp_width);
		dimensions_height.setValue(temp_height);
		
//		int number_of_moves = 1;
//		if (moves.getValue() instanceof Long) { 
//			number_of_moves = ((Long) moves.getValue()).intValue();
//		} else if (moves.getValue() instanceof Integer) { 
//			number_of_moves = (int) moves.getValue(); 
//		}
		int number_of_moves = (int)((long) moves.getValue());
//		int objective_value = 1;
//		if (mode_objective.getValue() instanceof Long) { 
//			objective_value = ((Long) mode_objective.getValue()).intValue();
//		} else if (mode_objective.getValue() instanceof Integer) { 
//			objective_value = (int) mode_objective.getValue(); 
//		}
		int objective_value = (int)((long) mode_objective.getValue());
		
		level.setBoard(board.getBoard());
		level.setSize(temp_width, temp_height);
		level.setRules(mode, number_of_moves, objective_value, number_of_candies.getValue());
		
		String fileName = level_on + ". " + level_name.getValue();
		boolean success = InterfaceManager.level_manager.saveLevel(fileName, level);
		if(success){
			InterfaceManager.refreshLevelBrowser();
		}
		String message = success?(fileName+" Saved!"):("Failed to save.");
		JOptionPane.showMessageDialog(this,message,"Notification",JOptionPane.INFORMATION_MESSAGE);
	}
}
