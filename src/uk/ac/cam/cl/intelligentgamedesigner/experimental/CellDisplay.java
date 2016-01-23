package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class CellDisplay extends JPanel  {
	final private int borderSize = 3;
	final private static Color emptyColor = Color.WHITE;
	private Color clr;
	private CandyColour cellColour;
	private int x = 0, y = 0;
	private Position position;
	
	// Indicates whether this is a selected block.
	private boolean selected = false;
	
	// Get the java colour that corresponds to the candy colour.
	// or WHITE is the cell is empty
	public static Color getCellColor(CandyColour type) {
		if (type == null) return emptyColor;
		switch (type) {
		case GREEN : return Color.GREEN;
		case RED : return Color.RED;
		case YELLOW : return Color.YELLOW;
		case BLUE : return Color.BLUE;
		case ORANGE: return Color.ORANGE;
		default: return Color.CYAN;
		}
	}
	
	// Returns the next colour in a round-robin fashion.
	public static CandyColour next(CandyColour candy) {
		if (candy == null ) return CandyColour.RED; 
		switch(candy) {
		case RED: return CandyColour.BLUE;
		case BLUE: return CandyColour.GREEN;
		case GREEN: return CandyColour.YELLOW;
		case YELLOW: return CandyColour.PURPLE;
		case PURPLE: return CandyColour.ORANGE;
	    default: return null;
		}
	}
	
	
	public void setCandyColor(CandyColour clr) {
		this.cellColour = clr;
		this.clr = getCellColor(clr);
	}
	
	public Color getColor() { return clr; }
	public CandyColour getCandyColour() { return cellColour; }
	
	
	// Sets the position that the CellDisplay is displaying.
	public void setPosition(Position pos) {
		this.position = pos;
	}
	
	// Returns the position that the CellDisplay is displaying.
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Dimension dim = getSize();
		int width = (int) dim.getWidth(), height = (int) dim.getHeight();
		g.setColor(clr);
		g.fillRect(0,0, width, height);
		// Draw border to selected component.
		if (selected) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, borderSize);
			g.fillRect(0, height - borderSize, width, borderSize);
			g.fillRect(0, 0, borderSize, height);
			g.fillRect(width - borderSize, 0, borderSize, height);
		}
	}
	
	public void setSelected(boolean selected ) {
		this.selected = selected;
		repaint();
	}

	
}
