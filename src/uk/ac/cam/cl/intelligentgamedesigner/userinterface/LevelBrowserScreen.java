package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

//loads the saved levels and allows you to scroll between them and select one
public class LevelBrowserScreen extends DisplayScreen implements ListSelectionListener{
	
	private JScrollPane level_list;
	private JList<String> level_names;
	private DisplayBoard board_display;
	private Design board_design;
	private JButton back_button;
	private JButton edit_button;
	private JButton delete_button;
	private JLabel title;
	
	private int selected_index;
	private String selected_name;
	
	public LevelBrowserScreen(){
		super();
		identifier = "Level Browser";
	}
	public void refreshList(){
		String[] ln = InterfaceManager.level_manager.getLevelNames();
		if(ln.length>0) {
			board_design = InterfaceManager.level_manager.getLevel(1);
			level_names.setEnabled(true);
		} else {
			ln = new String[]{"<No Levels Saved>"};
			board_design = new Design();
			DebugFilter.println("no levels",DebugFilterKey.USER_INTERFACE);
			level_names.setEnabled(false);
		}
		level_names.setListData(ln);
		board_design = InterfaceManager.level_manager.getLevel(1);
		refreshBoard();
		
		selected_name = null;
	}
	private void refreshBoard(){
		try{
			board_display.setBoard(board_design.getBoard());
		} catch(NullPointerException e){
			board_design = new Design();
			board_display.setBoard(board_design.getBoard());
			board_display.clearBoard();
		}
	}
	
	@Override
	protected void makeItems() {
		String[] ln = InterfaceManager.level_manager.getLevelNames();
		level_names = new JList<String>();
		if(ln.length>0) {
			level_names.setListData(ln);
			level_list = new JScrollPane(level_names);
			level_names.setEnabled(true);
			board_design = InterfaceManager.level_manager.getLevel(1);
		} else {
			level_names.setListData(new String[]{"<No Levels Saved>"});
			level_list = new JScrollPane(level_names);
			level_names.setEnabled(false);
			board_design = new Design();
		}

		board_display = new DisplayBoard(board_design);
		back_button = new JButton("Back");
		edit_button = new JButton("View Level Options");
		delete_button = new JButton("Delete Selected Level");
		
		title = new JLabel("Select A Level Design:",SwingConstants.CENTER);
	}
	
	@Override
	protected void setUpItems() {
		board_display.adjustSize(3);
		
		if(level_names != null) {
	        level_names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        level_names.setSelectedIndex(0);
	        selected_name = level_names.getSelectedValue();
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
	protected void addItems(){
		//sort out the window's layout settings:
		setLayout(null);
		
		add(title);
		add(level_list);
		add(back_button);
		add(board_display);
		add(delete_button);
		add(edit_button);
		
	}
	
	@Override
	protected void placeItems() {
		
		//size the fonts
		fontScale(title, DisplayScreen.FONT_TITLE);
		fontScale(edit_button, DisplayScreen.FONT_NORMAL);
		fontScale(delete_button, DisplayScreen.FONT_NORMAL);
		fontScale(level_list, DisplayScreen.FONT_NORMAL);
		fontScale(back_button, DisplayScreen.FONT_NORMAL);
		
		position(title,0.5,0.9,300,50);
		position(edit_button,0.75,0.6,150,20);
		position(delete_button,0.25,0.6,180,20);
		position(level_list,0.5,0.2,300,70);
		position(back_button,0.1,0.9,80,20);
		positionBoard(board_display,0.5,0.55);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "back":
			InterfaceManager.switchScreen(Windows.MAIN);
			break;
		case "view":
			if(level_names!=null && selected_name != null){
				InterfaceManager.setSelectedDDesign(board_design,selected_name);
				InterfaceManager.setPreviousScreen(Windows.LOAD);
				InterfaceManager.switchScreen(Windows.DISPLAY);
			}
			break;
		case "delete":
			if(level_names!=null && selected_name != null){
				System.out.println("Deleting level" + (selected_index + 1));
				InterfaceManager.level_manager.deleteLevel(selected_index+1);
			}
			refreshList();
			JOptionPane.showMessageDialog(this,"Deleted the level","Notification",JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			JList selected = ((JList)e.getSource());
			selected_index = selected.getSelectedIndex();
			selected_name = (String)selected.getSelectedValue();
			board_design = InterfaceManager.level_manager.getLevel(selected_index+1);
			refreshBoard();
		}
	}
	
	@Override
	protected void resizeBoards(){
		if(board_display!=null)board_display.updateTileSize();
	}
}
