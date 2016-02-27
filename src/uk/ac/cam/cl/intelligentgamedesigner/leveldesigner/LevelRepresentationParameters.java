package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class LevelRepresentationParameters {
    public final Random random;
    public final int numberOfCandyColours;
    public final double targetIcingDensity;
    public final double targetLiquoriceDensity;
    public final double targetJellyDensity;

    public LevelRepresentationParameters (Random random,
                                          int numberOfCandyColours,
                                          double targetIcingDensity,
                                          double targetLiquoriceDensity,
                                          double targetJellyDensity) {
        this.random = random;
        this.numberOfCandyColours = numberOfCandyColours;
        this.targetIcingDensity = targetIcingDensity;
        this.targetLiquoriceDensity = targetLiquoriceDensity;
        this.targetJellyDensity = targetJellyDensity;
    }
}
