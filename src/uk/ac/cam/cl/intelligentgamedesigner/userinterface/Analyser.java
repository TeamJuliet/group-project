package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;

/**This class sets a thread working on a analysing a design using a simulated player and displays the result**/
public class Analyser extends JPanel implements ActionListener,PropertyChangeListener{
	int ability;
	Design design;
	JLabel info;
	JButton start;
	JLabel progress;
	JLabel result;

	public Analyser(Design design, int ability){
		super();
		this.design = design;
		this.ability = ability;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		info = new JLabel("Simulated Player Ability Level: " + ability );
		start = new JButton("Begin Analysis");
		start.addActionListener(this);
		start.setActionCommand("start");
		start.setEnabled(true);
		
		add(info);
		add(start);
		
		progress = new JLabel("Progress: Not Started");
		result = new JLabel("Pass Rate: N/A");
		
		add(progress);
		add(result);
		
	}
	public void startAnalysis(){
		start.setEnabled(false);
		progress.setText("Progress: Analysing...");

		AnalysisThread worker = new AnalysisThread(ability,design);
		worker.addPropertyChangeListener(this);
	    worker.execute();	
	}
	public void endAnalysis(double passrate){
		//trim to 1dp.
		passrate = (double)((int)(passrate * 1000))/10;
		String theText = "Pass Rate: " + passrate + "%";
		
		progress.setText("Progress: Done!");
		result.setText(theText);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "start":
			startAnalysis();
			break;
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		switch(e.getPropertyName()){
		case "done":
			endAnalysis((double)e.getNewValue());
			break;
		}
	}
}
