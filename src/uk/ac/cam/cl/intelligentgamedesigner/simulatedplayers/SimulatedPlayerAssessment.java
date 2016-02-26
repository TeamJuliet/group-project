package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateProgress;

/**
 * 
 * Class that is responsible for the assessment of players based on the records
 * of several players.
 *
 */
public class SimulatedPlayerAssessment {

    public static double getAverage(List<Double> values) {
        if (values.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum / ((double) values.size());
    }

    public static double getStandardDeviationEstimate(List<Double> values) {
        if (values.isEmpty())
            return 0.0;

        double sum = 0.0;
        double mean = getAverage(values);

        for (Double value : values) {
            sum += (value - mean) * (value - mean);
        }

        return Math.sqrt(sum / ((double) values.size()));
    }

    private static List<Double> extractAverageMovesRemaining(List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : otherPlayers) {
            ret.add(record.averageRemainingMoves);
        }
        return ret;
    }

    private static List<Double> extractScoreToTarget(List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : otherPlayers) {
            ret.add(record.normalizedAverageScore);
        }
        return ret;
    }

    private static int countSmallerMeans(double meanGamesWon, List<PlayerAssessmentRecord> otherPlayers) {
        int count = 0;
        for (PlayerAssessmentRecord record : otherPlayers) {
            double ratio = ((double) record.numOfGamesWon) / (record.numOfGamesWon + (double) record.numOfGamesLost);
            if (ratio > meanGamesWon)
                ++count;
        }
        return count;
    }

    private static double sanitizePercentage(double percentage) {
        if (percentage < 0.0)
            return 0.0;
        if (percentage > 1.0)
            return 1.0;
        return percentage;
    }

    private static final double PERCENTAGE_BOOST = 0.05;

    public static double getScorePlayerAssessment(PlayerAssessmentRecord playerRecordsToAssess,
            List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> movesMeans = extractAverageMovesRemaining(otherPlayers);
        List<Double> scoreMeans = extractScoreToTarget(otherPlayers);

        double movesMean = getAverage(movesMeans);
        double scoreMean = getAverage(scoreMeans);
        double movesSD = getStandardDeviationEstimate(movesMeans);
        double scoreSD = getStandardDeviationEstimate(scoreMeans);

        double movesDelta = (playerRecordsToAssess.averageRemainingMoves - movesMean) / movesSD;
        double scoreDelta = (scoreMean - playerRecordsToAssess.normalizedAverageScore) / scoreSD;

        double averageGamesWon = playerRecordsToAssess.getAverageGamesWon();
        double percentage = countSmallerMeans(averageGamesWon, otherPlayers);

        return sanitizePercentage(
                percentage + PERCENTAGE_BOOST * (averageGamesWon * movesDelta + scoreDelta * (1.0 - averageGamesWon)));
    }

    public static PlayerAssessmentRecord getScorePlayerAssessmentRecord(List<GameStateProgress> progressRecords,
            Design design) {
        int sumMovesRemaining = 0;
        int numOfGamesWon = 0;
        int numOfGamesLost = 0;
        double scoreToTarget = 0;

        double averageMovesPerScore = design.getNumberOfMovesAvailable() / design.getObjectiveTarget();

        for (GameStateProgress progress : progressRecords) {
            if (progress.isGameWon(design)) {
                sumMovesRemaining += progress.getMovesRemaining();
                ++numOfGamesWon;
            } else {
                scoreToTarget += averageMovesPerScore * (design.getObjectiveTarget() - progress.getScore());
                ++numOfGamesLost;
            }
        }

        final double normalizedAverageScore = numOfGamesLost == 0 ? 0.0 : scoreToTarget / ((double) numOfGamesLost);
        final double averageRemainingMoves = numOfGamesWon == 0 ? 0.0 : sumMovesRemaining / ((double) numOfGamesWon);

        return new PlayerAssessmentRecord(numOfGamesWon, numOfGamesLost, normalizedAverageScore, averageRemainingMoves);
    }

}
