package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import javax.swing.SwingWorker;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelDesignerManager;

/**
 * 
 * This calls the level designer manager to analyse the level
 * it gets a design and a computer player ability level
 * The level designer manager then runs a number of test games and returns
 * the percentage of levels that it succeeded at.
 *
 */
public class AnalysisThread extends SwingWorker{
	
	int ability;
	Design design;
	public AnalysisThread(int ability, Design design){
		this.ability = ability;
		this.design = design;
	}

	@Override
	protected Object doInBackground() throws Exception {
		double passrate = LevelDesignerManager.calculatePassRate(ability, design);
		firePropertyChange("done", null, passrate);
		return null;
	}

}
