package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;

public class TestMain {

    public static void main (String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                GameState state = new GameState(new Design());

                // Take snapshot of state beforehand
                GameState state1 = (GameState) state.clone();

                // Make a move
                Move moveToMake = new Move (new Position(8, 6), new Position(7, 6));
                state.makeMove(moveToMake);
                while(state.makeSmallMove()) {
                    System.err.println("Moving on");
                }

                // Take a snapshot of the state after
                GameState state2 = (GameState) state.clone();

                // Create a test case
                TestCase gameTestCase = new TestCaseGame("testMove", state1, state2, moveToMake);

                TestLibrary.addTest(gameTestCase);
            }
        } catch (InvalidMoveException e) {

        }
    }
}
