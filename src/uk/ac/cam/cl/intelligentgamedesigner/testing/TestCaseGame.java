package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;

public class TestCaseGame extends TestCase {
    private GameState before;
    private GameState after;
    private Move moveMade;

    public TestCaseGame (String description, GameState before, GameState after, Move moveMade) {
        super(description);

        this.before = before;
        this.after = after;
        this.moveMade = moveMade;
    }

    @Override
    public void run () {
        try {
            // Make move
            before.makeMove(moveMade);
            while(before.makeSmallMove()) {}

            if (after.equals(before)) {
                System.out.println("PASS: " + super.description);
            } else {
                System.err.println("FAIL: " + super.description);
            }
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }
}
