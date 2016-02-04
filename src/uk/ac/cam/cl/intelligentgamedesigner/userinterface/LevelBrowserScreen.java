package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

//loads the saved levels and allows you to scroll between them and select one
public class LevelBrowserScreen extends DisplayScreen implements ListSelectionListener{
	
	private JScrollPane level_list;
	private JList<String> level_names;
	private GameBoard board_display;
	private JButton back_button;
	private JButton edit_button;
	private JButton delete_button;
	private JLabel title;
	
	public LevelBrowserScreen(){
		super();
		identifier = "Level Browser";
	}
	public void refreshList(){
		makeItems();
		setUpItems();
		placeItems();
	}
	
	@Override
	protected void makeItems() {
		String[] ln = InterfaceManager.level_manager.getLevelNames();
		if(ln.length>0) {
			level_names = new JList<String>(ln);
			level_list = new JScrollPane(level_names);
			Design design = InterfaceManager.level_manager.getLevel(1);
			board_display = new GameBoard(design);
		} else {
			level_list = new JScrollPane(new JLabel("<No Levels Saved>"));
			board_display = new GameBoard(new Design());
			System.out.println("new Design");
		}
		
		back_button = new JButton("Back");
		edit_button = new JButton("View Level Options");
		delete_button = new JButton("Delete Selected Level");
		
		title = new JLabel("Select A Level Design:");
	}
	
	@Override
	protected void setUpItems() {
		board_display.adjustSize(3);
		
		if(level_names != null) {
	        level_names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        level_names.setSelectedIndex(0);
			level_names.addListSelectionListener(this);
		}
		
		back_button.setActionCommand("back");
		back_button.addActionListener(this);

		edit_button.setEnabled(level_list!=null);
		edit_button.setActionCommand("view");
		edit_button.addActionListener(this);

		//delete_button.setEnabled(level_list!=null);
		delete_button.setActionCommand("delete");
		delete_button.addActionListener(this);
		
		title.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 18));
		title.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		add(title);
		add(level_list);
		add(back_button);
		add(board_display);
		add(delete_button);
		add(edit_button);
		
		position(title,0.5,0.9,300,50);
		position(edit_button,0.75,0.6,150,20);
		position(delete_button,0.2,0.6,180,20);
		position(level_list,0.5,0.2,300,50);
		position(back_button,0.1,0.9,80,20);
		position(board_display,0.5,0.50,500,500);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "view":
			InterfaceManager.switchScreen(Windows.DISPLAY);
			break;
		case "delete":
			if(level_names!=null){
				System.out.println("Deleting level" + level_names.getSelectedIndex() + "+ 1");
				InterfaceManager.level_manager.deleteLevel(level_names.getSelectedIndex()+1);
			}
			refreshList();
			break;
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			int design_num = level_names.getSelectedIndex();
			System.out.println("New number "+design_num);
			board_display = new GameBoard(InterfaceManager.level_manager.getLevel(design_num));
			//board_display.repaint();
		}
	}
}
