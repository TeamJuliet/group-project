package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that contains statistics that concern the: - number of valid moves. -
 * number of transitions in the states. - number of passed ingredients. - number
 * of icing blockers removed. - pair of swapped candies.
 *
 */
public class ProcessStateStats {
    private boolean wasShuffled            = false;
    private int     numOfTransitions       = 0;
    private int     numOfValidMoves        = 0;
    private int     numOfPassedIngredients = 0;
    private int     numOfRemovedIcing      = 0;
    private Candy   candySwapped1, candySwapped2;

    /**
     * Function that resets the counters to the default values.
     */
    public void reset() {
        this.wasShuffled = false;
        this.numOfPassedIngredients = this.numOfRemovedIcing = this.numOfTransitions = this.numOfValidMoves = 0;
        this.candySwapped1 = this.candySwapped2 = null;
    }

    /**
     * Set the number of available valid moves in the current state.
     * 
     * @param moves
     *            The number of moves in the state.
     */
    public void setValidMoves(int moves) {
        this.numOfValidMoves = moves;
    }

    /**
     * Indicate that the board was shuffled in this round.
     * 
     * @param wasShuffled
     *            whether it was shuffled or not.
     */
    public void setShuffled(boolean wasShuffled) {
        this.wasShuffled = wasShuffled;
    }

    /**
     * Function that increments the transitions in the process state diagram.
     */
    public void incrementTransitions() {
        ++this.numOfTransitions;
    }

    /**
     * Function that increments the icing removed in that move.
     */
    public void incrementIcing() {
        ++this.numOfRemovedIcing;
    }

    /**
     * Indicate that one more ingredient was removed from the board.
     */
    public void incrementPassedIngredients() {
        ++this.numOfPassedIngredients;
    }

    /**
     * Set the candies that were swapped in the last move.
     * 
     * @param candy1
     *            The first candy that was swapped.
     * @param candy2
     *            The second candy that was swapped.
     */
    public void setCandiesSwapped(Candy candy1, Candy candy2) {
        this.candySwapped1 = new Candy(candy1);
        this.candySwapped2 = new Candy(candy2);
    }

    /**
     * Get an immutable version of the stats that have been gathered so far.
     * 
     * @return The immutable version of the stats.
     */
    public ProcessStateStatsViewer toViewer() {
        return new ProcessStateStatsViewer(this.wasShuffled, this.numOfTransitions, this.numOfValidMoves,
                this.numOfPassedIngredients, this.numOfRemovedIcing, candySwapped1, candySwapped2);
    }
}
