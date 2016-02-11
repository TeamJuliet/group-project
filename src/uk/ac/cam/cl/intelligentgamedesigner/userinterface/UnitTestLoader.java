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
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.ac.cam.cl.intelligentgamedesigner.testing.GameStateTestRunner;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCase;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCaseGame;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestLibrary;

public class UnitTestLoader extends JPanel implements ActionListener, ListSelectionListener {

    private JButton loadButton;
    private TestCaseGame loaded_test;
	private JScrollPane test_list;
	private JList<String> test_names;
	
	private int selected_index;
	private String selected_name;
    
    public TestCaseGame getTest(){
    	return loaded_test;
    }

    public UnitTestLoader() {
        super(new BorderLayout());

        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        
        loadButton = new JButton("Load Level");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        loadButton.setEnabled(false);
        
        String[] names = new String[0];
        test_names = new JList<String>(names);
        test_list = new JScrollPane(test_names);

        JPanel panel = new JPanel();
        panel.add(loadButton);
        
        add(test_list,BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

    public static void main (String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("Loading Unit Tests");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new UnitTestLoader();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
