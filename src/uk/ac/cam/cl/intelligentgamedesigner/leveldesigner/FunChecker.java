package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

import java.util.List;

public class FunChecker {

    // Just for testing purposes
    static final boolean DEBUG = false;

    public static double getFunFitness (List<RoundStatistics> roundStatisticsList) {

        // I've done this because this prints a hell of a lot to standard output if DEBUG is true
        if (DEBUG) {
            // Useful for debugging
            DebugFilter.println("************************************************************************************************************", DebugFilterKey.LEVEL_DESIGN);
            DebugFilter.println("GAME STATS:", DebugFilterKey.LEVEL_DESIGN);
            DebugFilter.println("************************************************************************************************************", DebugFilterKey.LEVEL_DESIGN);
            for (RoundStatistics roundStatistics : roundStatisticsList) {
                DebugFilter.println("ROUND STATS:", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println("-------------------------------", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println("***** Progress: *****", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println(roundStatistics.progress.toString(), DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println("***** State: *****", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println(roundStatistics.processStateStats.toString(), DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println("***** Candies formed: *****", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println(roundStatistics.candiesFormed.toString(), DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println("***** Candies removed: *****", DebugFilterKey.LEVEL_DESIGN);
                DebugFilter.println(roundStatistics.candiesRemoved.toString(), DebugFilterKey.LEVEL_DESIGN);
            }
        }


        return 0;
    }
}
