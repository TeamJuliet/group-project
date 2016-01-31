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
    public boolean run () {
        try {
            // Make move
            before.makeMove(moveMade);
            while(before.makeSmallMove()) {}

            return after.equals(before);

        } catch (InvalidMoveException e) {
            e.printStackTrace();
            return false;
        }
    }
}
