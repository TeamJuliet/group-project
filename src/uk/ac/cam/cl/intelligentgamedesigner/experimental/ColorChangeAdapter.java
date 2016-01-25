package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorChangeAdapter extends MouseAdapter {
	/* This code is for colour choosing */
	@Override
    public void mousePressed(MouseEvent e) {
    	CellDisplay cell = (CellDisplay) e.getSource();
    	cell.setNextCandyColour();
    	cell.repaint();
    }
}
