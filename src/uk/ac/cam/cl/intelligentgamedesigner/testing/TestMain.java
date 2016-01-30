package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;

public class TestMain {

    public static void main (String[] args) {
        try {
            GameState state = new GameState(new Design());
            System.out.println("*** BEFORE MOVE: ***");
            state.debugBoard();

            // Take snapshot of state beforehand
            GameState state1 = (GameState) state.clone();

            // Make a move
            Move moveToMake = new Move (new Position(8, 6), new Position(7, 6));
            state.makeMove(moveToMake);
            while(state.makeSmallMove()) {}

            // TODO: Discover why the state after the move is incorrect
            System.out.println("*** AFTER MOVE: ***");
            state.debugBoard();

            // Take a snapshot of the state after
            GameState state2 = (GameState) state.clone();

            // Uncomment once above is fixed
//            TestCase gameTestCase = new TestCaseGame("testMove", state1, state2, moveToMake);
//
//            TestLibrary.addTest(gameTestCase);
//            TestLibrary.runTests();
        } catch (InvalidMoveException e) {

        }
    }
}
