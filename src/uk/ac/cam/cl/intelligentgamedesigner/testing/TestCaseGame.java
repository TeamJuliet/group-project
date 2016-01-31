package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;

public class TestCaseGame extends TestCase {
    private Cell[][] before;
    private Cell[][] lookahead;
    private Cell[][] after;
    private Move moveMade;

    public TestCaseGame (String description, Cell[][] before, Cell[][] lookahead, Cell[][] after, Move moveMade) {
        super(description);

        this.before = before;
        this.lookahead = lookahead;
        this.after = after;
        this.moveMade = moveMade;
    }

    @Override
    public boolean run () {
        try {
            // Construct GameState representing game before
            GameState gameStateBefore = new GameState(before, new PreFilledCandyGenerator(new DesignParameters(6),
                    lookahead));

            // Make the test move
            gameStateBefore.makeMove(moveMade);
            while(gameStateBefore.makeSmallMove()) {}

            // Construct GameState representing game after
            GameState gameStateAfter = new GameState(after, new PreFilledCandyGenerator(new DesignParameters(6),
                    lookahead));

            // Check the GameStates match
            return after.equals(before);

        } catch (InvalidMoveException e) {
            e.printStackTrace();
            return false;
        }
    }
}
