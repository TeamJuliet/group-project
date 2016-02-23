package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class Tester {
	
	public static void shouldThrow(boolean a) throws InvalidMoveException {
		if (a) throw new InvalidMoveException(new Move(new Position(1, 2), new Position(3, 4)));
	}
	
	public static void main(String[] arg) {
		try {
			shouldThrow(true);
		} catch (InvalidMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
