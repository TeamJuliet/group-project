package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private List<Coordinates> cells  = new ArrayList();
    private int               length = 0;
    boolean                   corner = false;

    public Match(Coordinates[] matchedCells, boolean corner) {
        for (Coordinates c : matchedCells) {
            cells.add(c);
            length++;
        }
        this.corner = corner;
    }

    public Match(Coordinates[] matchedCells) {
        this(matchedCells, false);
    }

    public List<Coordinates> getCells() {
        return cells;
    }

    public int getLength() {
        return length;
    }
}
