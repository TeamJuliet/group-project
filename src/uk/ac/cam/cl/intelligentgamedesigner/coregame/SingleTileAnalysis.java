package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 	Class that contains the intervals that contain this tile and have the same
 * 	CandyColour and forming a match (i.e. their lengths are greater than 2).
 */
public class SingleTileAnalysis {
    /**
     * The start and end of the match on x-axis.
     */
    public final int start_x, end_x;
    
    /**
     * The start and end of the match on y-axis.
     */
    public final int start_y, end_y;

    public SingleTileAnalysis(int start_x, int end_x, int start_y, int end_y) {
        this.start_x = start_x;
        this.end_x = end_x;
        this.start_y = start_y;
        this.end_y = end_y;
    }

    /**
     * Returns the size of the horizontal match containing the point.
     * @return size of the horizontal match containing the initial point.
     */
    public int getLengthX() {
        return end_x - start_x + 1;
    }

    /**
     * Returns the size of the vertical match containing the point.
     * @return size of the vertical match containing the initial point.
     */
    public int getLengthY() {
        return end_y - start_y + 1;
    }
    
    /**
     * Function that checks if the analysis shows that there is a horizontal match.
     * @return whether the analysis deduced that this cell forms a horizontal match.
     */
    public boolean formsHorizontalMatch() {
        return getLengthX() > 2;
    }
    
    /**
     * Function that checks if the analysis shows that there is a vertical match.
     * @return whether the analysis deduced that this cell forms a vertical match.
     */
    public boolean formsVerticalMatch() {
        return getLengthY() > 2;
    }
}
