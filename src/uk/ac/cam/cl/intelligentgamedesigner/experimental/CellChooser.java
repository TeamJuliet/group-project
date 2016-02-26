package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.TextField;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

import javax.swing.*;

/**
 * Class to keep track of the objects that have been selected for a move.
 */
public class CellChooser {
    // The positions that will be swapped on the board.
    private static Position    p1, p2;

    // The displays for the cells at the positions p1 and p2.
    private static CellDisplay d1 = null, d2;

    // The state of the game that is being operated on.
    public static GameState    game;

    // The GameDiplay that is being used for the board.
    public static GameDisplay  display;

    // A string that contains the moves on the board in text format so that bugs
    // can be replicated.
    public static TextField    movesRecord;
    
    // The score label so that it can be updated and displayed on the screen.
    public static JLabel       scoreLabel;
    
        // Function that attempts to select a new Position.
    public static synchronized void SelectPosition(Position pos, CellDisplay d) {
        // In case they are both selected then we can reset.
        // In the actual game this should not be performed unless the move
        // has been completed.
        if (p1 == null || d1 == d) {
            p1 = pos;
            d1 = d;
            d.setSelected(true);
        } else if (p2 == null) {
            p2 = pos;
            d2 = d;
            d.setSelected(true);
            try {
                game.makeInitialMove(new Move(p1, p2));
                movesRecord.setText(movesRecord.getText() + "|" + p1 + " " + p2);
                while (game.makeSmallMove()) {
                    scoreLabel.setText("Score: " + game.getGameProgress().score);
                    display.setBoard(game.getBoard());
                    display.paintImmediately(0, 0, display.getWidth(), display.getHeight());
                    Thread.sleep(500);
                }
            } catch (InvalidMoveException ex) {
                System.err
                        .println("You performed an invalid move." + p1 + " " + p2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
