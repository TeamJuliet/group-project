package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.ac.cam.cl.intelligentgamedesigner.testing.GameStateTestRunner;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCase;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCaseGame;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestLibrary;

public class UnitTestLoader extends JPanel implements ActionListener,ListSelectionListener {

	private JScrollPane test_list;
	private JList<String> test_names;
	
	private JButton cancel_button;
	
	private String[] test_case_names;
	private TestCaseGame[] test_cases;
	private int selected_test;
    
    public TestCaseGame getTest(){
    	if(selected_test!= -1)return test_cases[selected_test];
    	else return null;
    }

    public UnitTestLoader() {
        super(new BorderLayout());

        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);

        //load all the levels (names) and add them to the list
        test_cases = loadTests();
        test_case_names = new String[test_cases.length];
        for(int n=0;n<test_cases.length;n++){
        	test_case_names[n] = test_cases[n].getFileName() + ": " + test_cases[n].getDescription();
        }
                
        test_names = new JList<String>(test_case_names);
        test_list = new JScrollPane(test_names);
        test_names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		test_names.addListSelectionListener(this);
		
		cancel_button = new JButton("Cancel Selection");
		cancel_button.addActionListener(this);
		cancel_button.setActionCommand("cancel");
        
        add(test_list,BorderLayout.CENTER);
        add(cancel_button,BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        selected_test = -1;
    }

	private TestCaseGame[] loadTests() {
		ArrayList<TestCase> all_tests = TestLibrary.getTests();
		ArrayList<TestCaseGame> game_tests = new ArrayList<TestCaseGame>();
		for(TestCase t:all_tests){
			if(t.getClass() == TestCaseGame.class){
				game_tests.add((TestCaseGame)t);
			}
		}
		return game_tests.toArray(new TestCaseGame[game_tests.size()]);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			JList selected = ((JList)e.getSource());
			selected_test = selected.getSelectedIndex();
		}
	}

	private static JFrame frame;
    public static void main (String[] args) {
        //Create and set up the window.
        frame = new JFrame("Loading Unit Tests");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new UnitTestLoader();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    private static void quit(){
    	if(frame != null)frame.dispose();
    }
    
    private void cancel(){
    	test_names.clearSelection();
    	selected_test = -1;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "cancel":
			cancel();
			break;
		}
	}
}
