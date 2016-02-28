package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerAccuracy;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.Specification;

//The menu from which you can request levels.
public class LevelRequesterScreen extends DisplayScreen implements ChangeListener{
	
	//buttons etc.
	JButton back_button;
	JButton go_button;
	
	ButtonGroup game_mode;
	JRadioButton high_score;
	JRadioButton jelly_clear;
	JRadioButton ingredients;

	JLabel title;
	
	JLabel game_mode_text;
	JLabel difficulty_text;
	JLabel accuracy_text;
	JLabel show_difficulty;
	JLabel show_accuracy;
	
	JSlider difficulty;
	JSlider accuracy;
	
	//bonus settings:
	JFormattedTextField min_moves;
	JFormattedTextField max_moves;
	JSlider jelly_density;
	JSlider lock_density;
	JSlider icing_density;
	JSlider unusable_density;
	JComboBox<Integer> max_candies;
	JComboBox<Integer> min_candies;
	
	//specified values
	double val_difficulty;
	int val_accuracy;
	int val_max_moves;
	int val_min_moves;
	double val_icing_density;
	double val_lock_density;
	double val_jelly_density;
	double val_unusable_density;
	
	public LevelRequesterScreen(){
		super();
		identifier = "Level Requester";
	}
	
	@Override
	protected void makeItems() {
		back_button = new JButton("Back");
		go_button = new JButton("Go");	

		title = new JLabel("Choose the type of level to make",SwingConstants.CENTER);
		game_mode_text = new JLabel("Select Game Mode:",SwingConstants.CENTER);
		difficulty_text = new JLabel("Select Difficulty (Predicted fail rate):",SwingConstants.CENTER);
		show_difficulty = new JLabel("%",SwingConstants.CENTER);
		show_accuracy = new JLabel("%",SwingConstants.CENTER);
		accuracy_text = new JLabel("Set Accuracy:",SwingConstants.CENTER);

		game_mode = new ButtonGroup();
		high_score = new JRadioButton("High Score",true);
		jelly_clear = new JRadioButton("Jelly Clear");
		ingredients = new JRadioButton("Ingredients");
		game_mode.add(high_score);
		game_mode.add(jelly_clear);
		game_mode.add(ingredients);
		
		difficulty = new JSlider(5,95);
		accuracy = new JSlider(1,3);

		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(0);
	    nf.setMaximumIntegerDigits(2);
	    
		min_moves = new JFormattedTextField(nf);
		max_moves = new JFormattedTextField(nf);
		jelly_density = new JSlider(1,10);
		lock_density = new JSlider(0,10);
		icing_density = new JSlider(0,10);
		unusable_density = new JSlider(0,10);
		min_candies = new JComboBox<Integer>();
		max_candies = new JComboBox<Integer>();
		for(int n=4;n<=6;n++){
			min_candies.addItem(n);
			max_candies.addItem(n);
		}
		min_candies.setSelectedItem(4);
		max_candies.setSelectedItem(6);
		min_candies.addActionListener(this);
		max_candies.addActionListener(this);
		min_candies.setActionCommand("min candy");
		max_candies.setActionCommand("max candy");
		min_moves.addActionListener(this);
		max_moves.addActionListener(this);
		min_moves.setActionCommand("min move");
		max_moves.setActionCommand("max move");
	}
	
	@Override
	protected void setUpItems() {
		back_button.setActionCommand("back");
		back_button.addActionListener(this);
		go_button.setActionCommand("go");
		go_button.addActionListener(this);
		
		val_difficulty = 0.5;
		show_difficulty.setText("Middling");
		difficulty.setValue(50);
		difficulty.setMajorTickSpacing(5);
		difficulty.setMinorTickSpacing(1);
		difficulty.setPaintTicks(true);
		difficulty.setPaintLabels(true);
		difficulty.addChangeListener(this);

		val_accuracy = 2;
		show_accuracy.setText("Balance of accuracy and speed");
		accuracy.setValue(val_accuracy);
		accuracy.setMajorTickSpacing(1);
		accuracy.setPaintTicks(true);
		accuracy.setPaintLabels(true);
		accuracy.addChangeListener(this);
		
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		high_score.setAlignmentX(CENTER_ALIGNMENT);
		jelly_clear.setAlignmentX(CENTER_ALIGNMENT);
		ingredients.setAlignmentX(CENTER_ALIGNMENT);
		
		game_mode_text.setAlignmentX(CENTER_ALIGNMENT);
		difficulty_text.setAlignmentX(CENTER_ALIGNMENT);
		show_difficulty.setAlignmentX(CENTER_ALIGNMENT);

		accuracy_text.setAlignmentX(CENTER_ALIGNMENT);
		show_accuracy.setAlignmentX(CENTER_ALIGNMENT);

		val_min_moves = 5;
		val_max_moves = 50;
		min_moves.setValue(val_min_moves);
		max_moves.setValue(val_max_moves);

		val_jelly_density = 0.3;
		jelly_density.setValue((int) (val_jelly_density * jelly_density.getMaximum()));
		jelly_density.setMajorTickSpacing(1);
		jelly_density.setPaintTicks(true);
		jelly_density.setPaintLabels(true);
		jelly_density.addChangeListener(this);

		val_lock_density = 0.1;
		lock_density.setValue((int) (val_lock_density * lock_density.getMaximum()));
		lock_density.setMajorTickSpacing(1);
		lock_density.setPaintTicks(true);
		lock_density.setPaintLabels(true);
		lock_density.addChangeListener(this);

		val_icing_density = 0.4;
		icing_density.setValue((int) (val_icing_density * icing_density.getMaximum()));
		icing_density.setMajorTickSpacing(1);
		icing_density.setPaintTicks(true);
		icing_density.setPaintLabels(true);
		icing_density.addChangeListener(this);

		val_unusable_density = 0.2;
		unusable_density.setValue((int) (val_unusable_density * unusable_density.getMaximum()));
		unusable_density.setMajorTickSpacing(1);
		unusable_density.setPaintTicks(true);
		unusable_density.setPaintLabels(true);
		unusable_density.addChangeListener(this);
	}
	
	private JPanel settings;
	private JPanel settings2;
	@Override
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);
		
		//make a box with the settings
		settings = new JPanel();
		settings.setLayout(new BoxLayout(settings,BoxLayout.Y_AXIS));
		settings.add(getSpace());
		settings.add(game_mode_text);
		settings.add(getSmallSpace());
		settings.add(high_score);
		settings.add(jelly_clear);
		settings.add(ingredients);
		settings.add(getSpace());
		settings.add(difficulty_text);
		settings.add(getSmallSpace());
		settings.add(difficulty);
		settings.add(getSmallSpace());
		settings.add(show_difficulty);
		settings.add(getSpace());
		settings.add(accuracy_text);
		settings.add(getSmallSpace());
		settings.add(accuracy);
		settings.add(getSmallSpace());
		settings.add(show_accuracy);
		settings.add(getSpace());
		
		//make a box with the settings
		settings2 = new JPanel();
		settings2.setLayout(new BoxLayout(settings2,BoxLayout.Y_AXIS));
		settings2.add(getSpace());
		settings2.add(new JLabel("Number of Moves (High Score and Ingredients only):"));
		settings2.add(getSmallSpace());
		settings2.add(new JLabel("Minimum:"));
		settings2.add(getSmallSpace());
		settings2.add(min_moves);
		settings2.add(getSmallSpace());
		settings2.add(new JLabel("Maximum:"));
		settings2.add(getSmallSpace());
		settings2.add(max_moves);
		settings2.add(getSpace());
		settings2.add(new JLabel("Number of Candies:"));
		settings2.add(getSmallSpace());
		settings2.add(new JLabel("Minimum:"));
		settings2.add(getSmallSpace());
		settings2.add(min_candies);
		settings2.add(new JLabel("Maximum:"));
		settings2.add(getSmallSpace());
		settings2.add(max_candies);
		settings2.add(getSpace());
		settings2.add(new JLabel("Density of Unusable/Absent Tiles:"));
		settings2.add(getSmallSpace());
		settings2.add(unusable_density);
		settings2.add(getSpace());
		settings2.add(new JLabel("Density of Liquorice Locks:"));
		settings2.add(getSmallSpace());
		settings2.add(lock_density);
		settings2.add(getSpace());
		settings2.add(new JLabel("Density of Icing:"));
		settings2.add(getSmallSpace());
		settings2.add(icing_density);
		settings2.add(getSpace());
		settings2.add(new JLabel("Density of Jelly layers (Jelly Clear only):"));
		settings2.add(getSmallSpace());
		settings2.add(jelly_density);
		settings2.add(getSpace());
		
		//add the items
		add(title);
		add(back_button);
		add(go_button);
		add(settings);
		add(settings2);
		
	}
	
	@Override
	protected void placeItems() {
		
		//size the fonts
		fontScale(title, DisplayScreen.FONT_TITLE);
		fontScale(go_button, DisplayScreen.FONT_NORMAL);
		fontScale(back_button, DisplayScreen.FONT_NORMAL);
		fontScale(settings, DisplayScreen.FONT_NORMAL);
		fontScale(settings2, DisplayScreen.FONT_NORMAL);
		
		//set the locations
		position(title,0.5,0.9,400,50);
		position(go_button,0.5,0.5,100,40);
		position(back_button,0.1,0.9,150,30);
		position(settings,0.26,0.47,400,380);
		position(settings2,0.74,0.47,400,470);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String move_to_text;
		switch(e.getActionCommand()){
		case "back":
	    	InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "go":
			move_to_text = max_moves.getValue().toString();
			val_max_moves = Integer.parseInt(move_to_text);
			move_to_text = min_moves.getValue().toString();
			val_min_moves = Integer.parseInt(move_to_text);
			if(val_min_moves>val_max_moves){
				JOptionPane.showMessageDialog(this, "You entered invalid bounds for the moves.","Error",JOptionPane.ERROR_MESSAGE);
				break;
			}
			GameMode mode = GameMode.HIGHSCORE;
			if(jelly_clear.isSelected())mode = GameMode.JELLY;
			if(ingredients.isSelected())mode = GameMode.INGREDIENTS;

			InterfaceManager.setLevelSpecifications(new Specification(
					val_difficulty,
					mode,
					LevelDesignerAccuracy.values()[val_accuracy-1],
					val_max_moves,
					val_min_moves,
					(int)max_candies.getSelectedItem(),
					(int)min_candies.getSelectedItem(),
					val_jelly_density,
					val_icing_density,
					val_lock_density,
					val_unusable_density
							));
	    	InterfaceManager.switchScreen(Windows.REQUESTING);
			break;
		case "max move"://set the min if larger
			move_to_text = max_moves.getValue().toString();
			val_max_moves = Integer.parseInt(move_to_text);
			if(val_min_moves>val_max_moves)
				min_moves.setValue(max_moves.getValue());
			break;
		case "min move"://set the max if smaller
			move_to_text = min_moves.getValue().toString();
			val_min_moves = Integer.parseInt(move_to_text);
			if(val_min_moves>val_max_moves)
				max_moves.setValue(min_moves.getValue());
			break;
		case "max candy"://set the min if larger
			if(min_candies.getSelectedIndex()>max_candies.getSelectedIndex())
				min_candies.setSelectedItem(max_candies.getSelectedItem());
			break;
		case "min candy"://set the max if smaller
			if(min_candies.getSelectedIndex()>max_candies.getSelectedIndex())
				max_candies.setSelectedItem(min_candies.getSelectedItem());
			break;
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
	    val_difficulty = ((double)difficulty.getValue())/100;
	    String difficulty_measure = null;
	    if(val_difficulty<0.10)difficulty_measure = "Trivial";
	    if(val_difficulty>=0.10 && val_difficulty<0.20)difficulty_measure = "Simples";
	    if(val_difficulty>=0.20 && val_difficulty<0.30)difficulty_measure = "Easy";
	    if(val_difficulty>=0.30 && val_difficulty<0.40)difficulty_measure = "Nice";
	    if(val_difficulty>=0.40 && val_difficulty<0.50)difficulty_measure = "Lower Normal";
	    if(val_difficulty==0.50)difficulty_measure = "Middling";
	    if(val_difficulty>0.50 && val_difficulty<=0.60)difficulty_measure = "Upper Normal";
	    if(val_difficulty>0.60 && val_difficulty<=0.70)difficulty_measure = "Challenging";
	    if(val_difficulty>0.70 && val_difficulty<=0.80)difficulty_measure = "Hard";
	    if(val_difficulty>0.80 && val_difficulty<=0.90)difficulty_measure = "Very Hard";
	    if(val_difficulty>0.90)difficulty_measure = "Holy Moley";
	    show_difficulty.setText(difficulty_measure);
	    
	    val_accuracy = accuracy.getValue();
	    switch(val_accuracy){
	    case 1:
	    	show_accuracy.setText("Fast but inaccurate");
	    	break;
	    case 2:
	    	show_accuracy.setText("Balance of accuracy and speed");
	    	break;
	    case 3:
	    	show_accuracy.setText("Maximum accuracy, but slow");
	    	break;
	    }

		val_icing_density = icing_density.getValue() / (double) icing_density.getMaximum();
		val_lock_density = lock_density.getValue() / (double) lock_density.getMaximum();
		val_jelly_density = jelly_density.getValue() / (double) jelly_density.getMaximum();
		val_unusable_density = unusable_density.getValue() / (double) unusable_density.getMaximum();
	}
}
