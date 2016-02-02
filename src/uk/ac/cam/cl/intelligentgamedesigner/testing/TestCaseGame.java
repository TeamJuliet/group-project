package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;

public class TestCaseGame extends TestCase {
    private Cell[][] before;
    private Cell[][] lookahead;
    private Cell[][] after;
    private Move moveMade;
    private int scoreBefore;
    private int scoreAfter;

    public TestCaseGame (String description,
                         Cell[][] before,
                         Cell[][] lookahead,
                         Cell[][] after,
                         Move moveMade) {
        this(description, "defaultName", before, lookahead, after, moveMade, 0, 0);
    }

    public TestCaseGame (String description,
                         String fileName,
                         Cell[][] before,
                         Cell[][] lookahead,
                         Cell[][] after,
                         Move moveMade,
                         int scoreBefore,
                         int scoreAfter) {
        super(description, fileName);

        this.before = before;
        this.lookahead = lookahead;
        this.after = after;
        this.moveMade = moveMade;
        this.scoreBefore = scoreBefore;
        this.scoreAfter = scoreAfter;
    }

    @Override
    public boolean run () {
        try {
            // Construct GameState representing game before
            GameState gameStateBefore = new GameState(before,
                    scoreBefore,
                    new PreFilledCandyGenerator(new DesignParameters(6), lookahead));

            // Make the test move
            gameStateBefore.makeMove(moveMade);
            while(gameStateBefore.makeSmallMove()) {}

            // Construct GameState representing game after
            GameState gameStateAfter = new GameState(after,
                    scoreAfter,
                    new PreFilledCandyGenerator(new DesignParameters(6), lookahead));

            // Check the GameStates match
            return after.equals(before);

        } catch (InvalidMoveException e) {
            // Move was invalid, anyway, no point running test
            return false;
        }
    }
}
