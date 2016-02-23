package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Class that is used for keeping statistics for the candies that were either
 * formed or removed during the course of a game. The results of these can be
 * converted to the corresponding storage class that is immutable (using the
 * toViewer() function).
 * 
 * Note: this function does not count ingredients nor unmovable candies.
 *
 */
public class CandiesAccumulator {
    // The counters for each of the respective candy types.
    private int normalCandies               = 0;
    private int horizontallyStrippedCandies = 0;
    private int verticallyStrippedCandies   = 0;
    private int wrappedCandies              = 0;
    private int colourBombs                 = 0;

    /**
     * Function that increments the counter for the candy passed.
     * 
     * @param candy
     *            the candy that we want to increment its respective counter.
     */
    public void candyProcessed(Candy candy) {
        if (candy == null || candy.getCandyType() == null)
            return;
        switch (candy.getCandyType()) {
        case HORIZONTALLY_STRIPPED:
            ++this.horizontallyStrippedCandies;
            break;
        case VERTICALLY_STRIPPED:
            ++this.verticallyStrippedCandies;
            break;
        case WRAPPED:
            ++this.wrappedCandies;
            break;
        case BOMB:
            ++this.colourBombs;
            break;
        case NORMAL:
            ++this.normalCandies;
            break;
        case INGREDIENT:
            break;
        case UNMOVABLE:
            break;
        default:
            break;
        }
    }

    /**
     * Function that converts the results of the counters to an immutable storage class.
     * 
     * @return viewer class for the counters.
     */
    public CandiesAccumulatorViewer toViewer() {
        return new CandiesAccumulatorViewer(normalCandies, horizontallyStrippedCandies, verticallyStrippedCandies,
                wrappedCandies, colourBombs);
    }

    /**
     * Function that resets all counters to 0.
     */
    public void reset() {
        this.normalCandies = this.horizontallyStrippedCandies = this.verticallyStrippedCandies = this.colourBombs = this.wrappedCandies = 0;
    }
}
