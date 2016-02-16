package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class GameBoard extends DisplayBoard{
	
	private AnimationType animationType;

	public GameBoard(Design design) {
		super(design);
		showing_unusables = false;
	}
	public GameBoard(int width, int height) {
		super(width,height);
		showing_unusables = false;
	}
}
