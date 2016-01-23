package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

// Class to keep track of the objects that have been selected for a move.
public class CellChooser {
	private static Position p1, p2;
	private static CellDisplay d1 = null, d2;
	// private final static Lock lock = new ReentrantLock();
	// final public static Condition hasMadeMove = lock.newCondition();
	
	// Function that attempts to select a new Position.
	public static synchronized void SelectPosition(Position pos, CellDisplay d) {
		// In case they are both selected then we can reset.
		// TODO: In the actual game this should not be performed unless the move
		// has been completed.
		if (p2 != null) {
			reset();
		}
		if (p1 == null || d1 == d) {
			p1 = pos;
			d1 = d;
			d.setSelected(true);
		}
		else if (p2 == null) {
			p2 = pos;
			d2 = d;
			//hasMadeMove.signal();
			d.setSelected(true);
			// reset();
		}
	}
	
	// Function that resets the positions that have been selected.
	public static synchronized void reset() {
		// Reset the borders for the two items.
		d1.setSelected(false);
		d2.setSelected(false);
		d1 = d2 = null;
		p1 = p2 = null;
	}
}
