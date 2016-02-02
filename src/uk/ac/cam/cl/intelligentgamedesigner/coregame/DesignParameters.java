package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class DesignParameters implements Serializable {
    private int numberOfCandyColours;

    public DesignParameters () {
        this(6);
    }

    public DesignParameters (int numberOfCandyColours) {
        this.numberOfCandyColours = numberOfCandyColours;
    }

    public int getNumberOfCandyColours () {
        return numberOfCandyColours;
    }
}
