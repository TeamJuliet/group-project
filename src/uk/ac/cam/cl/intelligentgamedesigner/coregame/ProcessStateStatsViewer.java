package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Immutable version of the class ProcessStateStats. 
 * See ProcessStateStats for more information.
 *
 */
public class ProcessStateStatsViewer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public final boolean wasShuffled;
    public final int     numOfTransitions;
    public final int     numOfValidMoves;
    public final int     numOfPassedIngredients;
    public final int     numOfRemovedIcing;
    public final Candy   candySwapped1, candySwapped2;

    ProcessStateStatsViewer(boolean shuffled, int transitions, int moves, int ingredients, int icing, Candy candySwapped1, Candy candySwapped2) {
        this.wasShuffled = shuffled;
        this.numOfTransitions = transitions;
        this.numOfValidMoves = moves;
        this.numOfPassedIngredients = ingredients;
        this.numOfRemovedIcing = icing;
        this.candySwapped1 = candySwapped1 == null ? null : new Candy(candySwapped1);
        this.candySwapped2 = candySwapped2 == null ? null : new Candy(candySwapped2);
    }

    @Override
    public String toString () {
        return    "Was shuffled:                  " + wasShuffled +
                "\nNumber of transitions:         " + numOfTransitions +
                "\nNumber of valid moves:         " + numOfValidMoves +
                "\nNumber of passed ingredients:  " + numOfPassedIngredients +
                "\nNumber of removed icing:       " + numOfRemovedIcing +
                "\nCandy swapped:                 " + candySwapped1.getCandyType() + " with " +
                candySwapped2.getCandyType() + "\n";
    }
}
