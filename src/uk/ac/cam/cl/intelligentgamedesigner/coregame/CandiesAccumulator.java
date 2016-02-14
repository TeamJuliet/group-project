package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class CandiesAccumulator {
    private int normalCandies               = 0;
    private int horizontallyStrippedCandies = 0;
    private int verticallyStrippedCandies   = 0;
    private int wrappedCandies              = 0;
    private int colourBombs                 = 0;

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
        case UNMOVEABLE:
            break;
        default:
            break;
        }
    }

    public CandiesAccumulatorViewer toViewer() {
        return new CandiesAccumulatorViewer(normalCandies, horizontallyStrippedCandies, verticallyStrippedCandies,
                wrappedCandies, colourBombs);
    }
    
    public void reset() {
        this.normalCandies = this.horizontallyStrippedCandies = this.verticallyStrippedCandies = this.colourBombs = this.wrappedCandies = 0;
    }
}
