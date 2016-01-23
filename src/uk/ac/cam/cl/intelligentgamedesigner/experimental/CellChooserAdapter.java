package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellChooserAdapter extends MouseAdapter {
	/* This code is for colour choosing */
	@Override
    public void mousePressed(MouseEvent e) {
		CellDisplay display = (CellDisplay) e.getSource();
    	CellChooser.SelectPosition(display.getPosition(), display);
    }

}
