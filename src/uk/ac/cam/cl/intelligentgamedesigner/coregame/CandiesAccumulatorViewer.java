package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class CandiesAccumulatorViewer {
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

}
