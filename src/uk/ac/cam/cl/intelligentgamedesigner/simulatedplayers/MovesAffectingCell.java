package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * 
 * Storage class that contains the number of moves available below and above a
 * cell.
 *
 */
public class MovesAffectingCell {

    /**
     * The moves available above the cell.
     */
    public final int above;
    
    /**
     * The moves available below the cell.
     */
    public final int below;

    public MovesAffectingCell(int above, int below) {
        this.above = above;
        this.below = below;
    }

}
