package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.TextField;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

import javax.swing.*;

// Class to keep track of the objects that have been selected for a move.
public class CellChooser {
	private static Position p1, p2;
	private static CellDisplay d1 = null, d2;
	public static GameState game;
	public static GameDisplay display;
	public static TextField movesRecord;
	public static JLabel scoreLabel;
	// private final static Lock lock = new ReentrantLock();
	// final public static Condition hasMadeMove = lock.newCondition();
	
	private static String positionToString(Position pos) {
		return "(" + pos.x + ", " + pos.y + ")";
	}
	
	// Function that attempts to select a new Position.
	public static synchronized void SelectPosition(Position pos, CellDisplay d) {
		// In case they are both selected then we can reset.
		// TODO: In the actual game this should not be performed unless the move
		// has been completed.
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
			try {
				game.makeMove(new Move(p1, p2));
				movesRecord.setText(movesRecord.getText() + "|" + positionToString(p1) + positionToString(p2));
				while(game.makeSmallMove()) {
					// System.err.println("Moving on");
					// game.debugBoard();
					scoreLabel.setText("Score: " + game.getGameProgress().score);
					display.setBoard(game.getBoard());
					display.paintImmediately(0, 0, display.getWidth(), display.getHeight());
					// game.debugBoard();
					Thread.sleep(500);
				}
			} catch (InvalidMoveException ex) {
				System.err.println("You performed an invalid move." + positionToString(p1) + " " + positionToString(p2));
				
				// game.debugBoard();
			} /*catch (InterruptedException inter) {
				inter.printStackTrace();
			}*/ catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("The move has ended");
			
			System.out.println(movesRecord.getText());
			reset();
		}
	}
	
	// Function that resets the positions that have been selected.
	public static void reset() {
		// Reset the borders for the two items.
		d1.setSelected(false);
		d2.setSelected(false);
		d1 = d2 = null;
		p1 = p2 = null;
	}
}
