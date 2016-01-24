package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private List<Position> cells  = new ArrayList();
    private int               length = 0;
    boolean                   corner = false;

    public Match(Position[] matchedCells, boolean corner) {
        for (Position c : matchedCells) {
            cells.add(c);
            length++;
        }
        this.corner = corner;
    }

    public Match(Position[] matchedCells) {
        this(matchedCells, false);
    }

    public List<Position> getCells() {
        return cells;
    }

    public int getLength() {
        return length;
    }
}
