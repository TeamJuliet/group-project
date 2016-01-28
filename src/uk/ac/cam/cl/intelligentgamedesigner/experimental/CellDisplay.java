package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyColour;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class CellDisplay extends JPanel  {
	final private int borderSize = 3;
	final private static Color emptyColor = Color.WHITE;
	private Color clr;
	private CandyColour cellColour;
	private CandyType candyType;
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
	
	public void setNextCandyColour() {
		cellColour = next(cellColour);
	}
	
	public void setCandy(Candy candy) {
		if (candy == null) {
			this.cellColour = null;
			this.candyType = null;
		} else {
			this.candyType = candy.getCandyType();
			this.cellColour = candy.getColour();
		}
		// this.cellColour = candy != null ? candy.getColour() : null;
		this.clr = getCellColor(this.cellColour);
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
	
	final static private int numStripes = 9;
	 
	private void drawVerticalStripes(Graphics g) {
		// System.out.println("Something has been drawn");
		Dimension dim = getSize();
		int stripeWidth = (int) (dim.getWidth()/(double) numStripes), height = (int) dim.getHeight();
		for (int i = 0; i < numStripes; ++i) {
			if (i % 2 == 1) g.fillRect(i * stripeWidth, 0, stripeWidth, height);
		}
	}
	
	private void drawHorizontalStripes(Graphics g) {
		Dimension dim = getSize();
		int stripeWidth = (int) (dim.getHeight()/(double) numStripes), width = (int) dim.getHeight();
		for (int i = 0; i < numStripes; ++i) {
			if (i % 2 == 1) g.fillRect(0, i * stripeWidth, width, stripeWidth);
		}
	}
	
	private void drawWrapped(Graphics g) {
		Dimension dim = getSize();
		int stepWidth = (int) (dim.getWidth()/4.0), stepHeight = (int) (dim.getHeight() /4.0);
		g.fillRect(stepWidth, stepHeight, 2 * stepWidth, 2 * stepHeight);
	}
	
	private void drawBomb(Graphics g) {
		Dimension dim = getSize();
		g.setColor(Color.BLACK);
		int stepWidth = (int) (dim.getWidth()/4.0), stepHeight = (int) (dim.getHeight() /4.0);
		g.fillOval(stepWidth, stepHeight, 2 * stepWidth, 2 * stepHeight);
	}
	
	private void drawSpecial(Graphics g) {
		if (candyType == null) return;
		g.setColor(Color.WHITE);
		switch(candyType) {
		case VERTICALLY_STRIPPED:
			drawVerticalStripes(g);
			break;
		case HORIZONTALLY_STRIPPED:
			drawHorizontalStripes(g);
			break;
		case WRAPPED:
			drawWrapped(g);
			break;
		case BOMB:
			drawBomb(g);
			break;
		default:
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Dimension dim = getSize();
		int width = dim.width, height = dim.height;
		g.setColor(clr);
		g.fillRect(0,0, width, height);
		drawSpecial(g);
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
