package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.ProcessState;

public class GameBoard extends DisplayBoard{

	public GameBoard(Design design) {
		super(design);
		showing_unusables = false;
	}
	public GameBoard(int width, int height) {
		super(width,height);
		showing_unusables = false;
	}
	
	private ProcessState animationState;
	private boolean animating;
	public void doAnimation(ProcessState gameState) throws InterruptedException{
		animationState = gameState;
		animating = true;
		Thread.sleep(10);
	}
}
