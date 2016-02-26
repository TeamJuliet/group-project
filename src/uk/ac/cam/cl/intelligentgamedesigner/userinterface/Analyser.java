package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class Analyser extends JPanel{

	public Analyser(Design design){
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JLabel text = new JLabel("Analysing...");
		add(text);
		analyse(design);
		text.setText("Done!");
		JTable table = new JTable(2,2);
		fillTable(table);
		add(table);
	}
	
	private void analyse(Design design){
		
	}
	
	private void fillTable(JTable table){
		
	}
}
