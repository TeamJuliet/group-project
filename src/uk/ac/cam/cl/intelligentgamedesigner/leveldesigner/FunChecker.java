package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.RoundStatistics;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

import java.util.List;

/**
 * A class for checking how fun a level is.
 */
public class FunChecker {

    // Just for testing purposes
    static final boolean DEBUG = false;

    public static double getFunFitness (List<RoundStatistics> statList) {

        if (statList.size() == 0) return 0;

        // I've done this because this prints a hell of a lot to standard output if DEBUG is true
        if (DEBUG) {
            // Useful for debugging
            DebugFilter.println("************************************************************************************************************", DebugFilterKey.LEVEL_DESIGN);
            DebugFilter.println("GAME STATS:", DebugFilterKey.LEVEL_DESIGN);
            DebugFilter.println("************************************************************************************************************", DebugFilterKey.LEVEL_DESIGN);
            for (RoundStatistics roundStatistics : statList) {
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

        // Use doubles to accumulate values so that we don't have to bother casting for division, etc.
        double numberOfRoundsShuffled                      = 0;
        double numberOfRoundsWithLessThan3MovesAvailable   = 0;
        double numberOfColourBombsDetonated                = 0;
        double numberOfWrappedCandiesDetonated             = 0;
        double numberOfStrippedCandiesDetonated            = 0;

        for (RoundStatistics roundStatistics : statList) {
            // Count the number of rounds which required a board re-shuffle (these are deemed bad)
            if (roundStatistics.processStateStats.wasShuffled) numberOfRoundsShuffled++;

            // Count the number of rounds which only have a choice of 1 or 2 moves (these are deemed boring)
            if (roundStatistics.processStateStats.numOfValidMoves < 3) numberOfRoundsWithLessThan3MovesAvailable++;

            // Count the number of colour bombs detonated (more of these are deemed more fun)
            numberOfColourBombsDetonated += roundStatistics.candiesRemoved.colourBombs;

            // Count the number of wrapped candies detonated (more of these are deemed more fun)
            numberOfWrappedCandiesDetonated += roundStatistics.candiesRemoved.wrappedCandies;

            // Count the number of stripped candies detonated (more of these are deemed more fun)
            numberOfStrippedCandiesDetonated += roundStatistics.candiesRemoved.horizontallyStrippedCandies +
                    roundStatistics.candiesRemoved.verticallyStrippedCandies;
        }

        int[] scoreDifferences = new int[statList.size() - 1];
        for (int i = 1; i < statList.size(); i++) {
            scoreDifferences[i - 1] = statList.get(i).progress.score - statList.get(i - 1).progress.score;
        }

        int totalNumberOfMovesAvailable = statList.get(0).progress.movesRemaining;
        double availableMovesScore = evaluateAvailableMoves(numberOfRoundsShuffled,
                numberOfRoundsWithLessThan3MovesAvailable, totalNumberOfMovesAvailable);
        double specialCandyScore = evaluateSpecialCandyFrequency(numberOfColourBombsDetonated,
                numberOfWrappedCandiesDetonated, numberOfStrippedCandiesDetonated, totalNumberOfMovesAvailable);
        double scoreProgressionScore = evaluateScoreProgression(scoreDifferences);

        return availableMovesScore * specialCandyScore * scoreProgressionScore;
    }

    /**
     * This returns a double between 0 and 1, where values towards 0 indicate the user didn't have much choice in the
     * number of moves they could make and values towards 1 indicate that they did.
     *
     * @param numberOfRoundsShuffled
     * @param numberOfRoundsWithLessThan3MovesAvailable
     * @param totalMovesAvailable
     * @return
     */
    private static double evaluateAvailableMoves (double numberOfRoundsShuffled,
                                                  double numberOfRoundsWithLessThan3MovesAvailable,
                                                  double totalMovesAvailable) {
        return Math.exp(-10 * (numberOfRoundsShuffled / totalMovesAvailable)) *
               Math.exp(-3  * (numberOfRoundsWithLessThan3MovesAvailable / totalMovesAvailable));
    }

    /**
     * This returns a double between 0 and 1, where values towards 0 indicate the user didn't have fun detonating
     * special candies and values toward 1 indicate that they did.
     *
     * @param numberOfColourBombsDetonated
     * @param numberOfWrappedCandiesDetonated
     * @param numberOfStrippedCandiesDetonated
     * @param totalMovesAvailable
     * @return
     */
    private static double evaluateSpecialCandyFrequency (double numberOfColourBombsDetonated,
                                                         double numberOfWrappedCandiesDetonated,
                                                         double numberOfStrippedCandiesDetonated,
                                                         double totalMovesAvailable) {
        double total = Math.min(numberOfColourBombsDetonated + numberOfWrappedCandiesDetonated +
                numberOfStrippedCandiesDetonated, totalMovesAvailable);

        return (0.5 + 0.5 * (total / totalMovesAvailable));
    }

    /**
     * This returns a double between 0 and 1, where values towards 0 indicate the user didn't make exciting
     * progressions in score and values toward 1 indicate that they did.
     *
     * @param scoreProgression
     * @return
     */
    private static double evaluateScoreProgression (int[] scoreProgression) {
        // TODO: Implement this
        return 1;
    }
}
