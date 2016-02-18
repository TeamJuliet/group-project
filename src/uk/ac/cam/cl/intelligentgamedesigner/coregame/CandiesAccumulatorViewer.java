package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Immutable class version of CandiesAccumulator.
 *
 */
public class CandiesAccumulatorViewer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public final int normalCandies;
    public final int horizontallyStrippedCandies;
    public final int verticallyStrippedCandies;
    public final int wrappedCandies;
    public final int colourBombs;

    public CandiesAccumulatorViewer(int normalCandies, int horizontallyStrippedCandies, int verticallyStrippedCandies,
            int wrappedCandies, int colourBombs) {
        this.normalCandies = normalCandies;
        this.horizontallyStrippedCandies = horizontallyStrippedCandies;
        this.verticallyStrippedCandies = verticallyStrippedCandies;
        this.colourBombs = colourBombs;
        this.wrappedCandies = wrappedCandies; 
    }

    @Override
    public String toString () {
        return    "Normal candies:                " + normalCandies +
                "\nHorizontally stripped candies: " + horizontallyStrippedCandies +
                "\nVertically stripped candies:   " + verticallyStrippedCandies +
                "\nWrapped candies:               " + wrappedCandies +
                "\nColour bombs:                  " + colourBombs + "\n";
    }
}
