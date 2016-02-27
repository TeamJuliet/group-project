package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateProgress;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers;

/**
 * 
 * Class that is responsible for the assessment of players based on the records
 * of several players.
 *
 */
public class SimulatedPlayerAssessment {

    /**
     * Scalar that determines by how much the level of the player should be
     * affected by the margin he /she won or lost the game.
     */
    private static final double PERCENTAGE_BOOST = 0.05;

    private static final int    INF_ATTEMPTS     = 10000;

    /**
     * Evaluates a list of score players given a design.
     * 
     * @param design
     *            The design of the level
     * @param players
     *            The players that will be evaluated.
     * @param gamesToRun
     *            The number of games that each player will try.
     * @return A list containing the respective percentage scores.
     */
    public static List<Double> evaluateScorePlayers(Design design, List<SimulatedPlayerBase> players, int gamesToRun) {

        List<PlayerAssessmentRecord> records = new LinkedList<PlayerAssessmentRecord>();
        for (SimulatedPlayerBase player : players) {
            List<GameStateProgress> progressRecords = getGameStateProgressList(player, design, gamesToRun);
            records.add(getScorePlayerAssessmentRecord(progressRecords, design));
        }

        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : records) {
            ret.add(getScorePlayerAssessment(record, records));
        }
        return ret;
    }

    /**
     * Evaluates a list of jelly players given a design.
     * 
     * @param design
     *            The design of the level
     * @param players
     *            The players that will be evaluated.
     * @param gamesToRun
     *            The number of games that each player will try.
     * @return A list containing the respective percentage scores.
     */
    public static List<Double> evaluateJellyPlayers(Design design, List<SimulatedPlayerBase> players, int gamesToRun) {

        List<PlayerAssessmentRecord> records = new LinkedList<PlayerAssessmentRecord>();
        for (SimulatedPlayerBase player : players) {
            List<GameStateProgress> progressRecords = getGameStateProgressList(player, design, gamesToRun);
            records.add(getJellyPlayerAssessmentRecord(progressRecords, design));
        }

        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : records) {
            ret.add(getJellyPlayerAssessment(record, records));
        }
        return ret;
    }
    
    /**
     * Function that returns the average of a list of values. If the list does
     * not have any elements then the mean value returned is 0.
     * 
     * @param values
     *            The list of values that the average needs to be taken.
     * @return The average of the values in the list.
     */
    public static double getAverage(List<Double> values) {
        if (values.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum / ((double) values.size());
    }

    /**
     * Function that finds an estimate for the standard deviation of the values
     * in the given list.
     * 
     * @param values
     *            The list of values for which the estimate for s.d. will be
     *            found.
     * @return The s.d. estimate
     */
    public static double getStandardDeviationEstimate(List<Double> values) {
        if (values.isEmpty() || values.size() == 1)
            return 0.0;

        double sum = 0.0;
        double mean = getAverage(values);

        for (Double value : values) {
            sum += (value - mean) * (value - mean);
        }

        return Math.sqrt(sum / ((double) values.size() - 1.0));
    }

    /**
     * Function that generates the percentage that indicates how well in the
     * given list of players the current player (of which the records have to be
     * provided) performed.
     * 
     * @param playerRecordsToAssess
     * @param otherPlayers
     *            The assessment records for the rest of the players.
     * @return A value between 0.0 and 1.0 indicating the relative position of
     *         the player in the rank.
     */
    public static double getScorePlayerAssessment(PlayerAssessmentRecord playerRecordsToAssess,
            List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> movesMeans = extractAverageMovesRemaining(otherPlayers);
        List<Double> scoreMeans = extractScoreToTarget(otherPlayers);

        double movesMean = getAverage(movesMeans);
        double scoreMean = getAverage(scoreMeans);

        double movesSD = getStandardDeviationEstimate(movesMeans);
        double scoreSD = getStandardDeviationEstimate(scoreMeans);

        double movesDelta = movesSD == 0.0 ? 0.0
                : ((playerRecordsToAssess.averageRemainingMoves - movesMean) / movesSD);
        double scoreDelta = scoreSD == 0.0 ? 0.0
                : ((scoreMean - playerRecordsToAssess.normalizedAverageScore) / scoreSD);

        double averageGamesWon = playerRecordsToAssess.getAverageGamesWon();
        double percentage = countSmallerMeans(averageGamesWon, otherPlayers) / ((double) otherPlayers.size());

        return sanitizePercentage(
                percentage + PERCENTAGE_BOOST * (averageGamesWon * movesDelta + scoreDelta * (1.0 - averageGamesWon)));
    }

    /**
     * Function that returns the assessment for a player that played a game with
     * highscore objective.
     * 
     * @param progressRecords
     *            The records for each of the last rounds of the game.
     * @param design
     *            The design for the level that was created.
     * @return The assessment record for the player.
     */
    public static PlayerAssessmentRecord getScorePlayerAssessmentRecord(List<GameStateProgress> progressRecords,
            Design design) {
        int sumMovesRemaining = 0;
        int numOfGamesWon = 0;
        int numOfGamesLost = 0;
        double scoreToTarget = 0;

        // Calculate the conversion rate for how many moves correspond to the
        // score remaining.
        final double averageMovesPerScore = design.getNumberOfMovesAvailable() / design.getObjectiveTarget();

        // Calculate the number of games won / lost and the sumof moves
        // remaining for the games that were won and the total normalised score
        // that was not reached.
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

    /**
     * Function that returns the deviation of attempts required to win the game.
     * 
     * @param assessment
     *            The assessment of the player.
     * @param stats
     *            The statistics for the general population.
     * @return The number of attempts difference in completing the level between
     *         these two.
     */
    public static int compareToPopulation(PlayerAssessmentRecord assessment, GroupPlayerStats stats) {
        if (assessment.numOfGamesWon == 0)
            return INF_ATTEMPTS;
        int attemptsNeededForWin = (int) (1.0 / assessment.getAverageGamesWon());
        int expectedAttempts = stats.getAverageAttemptsRequired();
        return expectedAttempts - attemptsNeededForWin;
    }

    /**
     * Function that returns the game progress at the end of the level.
     * 
     * @param player
     *            The player that will be used to run.
     * @param gameState
     *            The game that it should play.
     * @return The game progress at the last state.
     */
    public static GameStateProgress getGameStateProgress(SimulatedPlayerBase player, GameState gameState) {
        while (!gameState.isGameOver()) {
            try {
                Move move = player.calculateBestMove(gameState);
                gameState.makeFullMove(move);
            } catch (NoMovesFoundException | InvalidMoveException e) {
                return null;
            }
        }
        return new GameStateProgress(gameState.getGameProgress());
    }

    /**
     * Function that returns a progress list for a player playing a particular
     * level.
     * 
     * @param player
     *            The player to be run.
     * @param design
     *            The design used to create the level.
     * @param gamesToRun
     *            The game to be run.
     * @return
     */
    public static List<GameStateProgress> getGameStateProgressList(SimulatedPlayerBase player, Design design,
            int gamesToRun) {
        List<GameStateProgress> progressList = new ArrayList<GameStateProgress>();
        for (int i = 0; i < gamesToRun; ++i) {
            progressList.add(getGameStateProgress(player, new GameState(design)));
        }
        return progressList;
    }

    /**
     * Function that generates the percentage that indicates how well in the
     * given list of players the current player (of which the records have to be
     * provided) performed.
     * 
     * @param playerRecordsToAssess
     * @param otherPlayers
     *            The assessment records for the rest of the players.
     * @return A value between 0.0 and 1.0 indicating the relative position of
     *         the player in the rank.
     */
    public static double getJellyPlayerAssessment(PlayerAssessmentRecord playerRecordsToAssess,
            List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> movesMeans = extractAverageMovesRemaining(otherPlayers);
        List<Double> scoreMeans = extractScoreToTarget(otherPlayers);

        double movesMean = getAverage(movesMeans);
        double scoreMean = getAverage(scoreMeans);

        double movesSD = getStandardDeviationEstimate(movesMeans);
        double scoreSD = getStandardDeviationEstimate(scoreMeans);

        double movesDelta = movesSD == 0.0 ? 0.0
                : ((playerRecordsToAssess.averageRemainingMoves - movesMean) / movesSD);
        double scoreDelta = scoreSD == 0.0 ? 0.0
                : ((scoreMean - playerRecordsToAssess.normalizedAverageScore) / scoreSD);

        double averageGamesWon = playerRecordsToAssess.getAverageGamesWon();
        double percentage = countSmallerMeans(averageGamesWon, otherPlayers) / ((double) otherPlayers.size());

        return sanitizePercentage(
                percentage + PERCENTAGE_BOOST * (averageGamesWon * movesDelta + scoreDelta * (1.0 - averageGamesWon)));
    }

    /**
     * Function that returns the assessment for a player that played a game with
     * highscore objective.
     * 
     * @param progressRecords
     *            The records for each of the last rounds of the game.
     * @param design
     *            The design for the level that was created.
     * @return The assessment record for the player.
     */
    public static PlayerAssessmentRecord getJellyPlayerAssessmentRecord(List<GameStateProgress> progressRecords,
            Design design) {
        int sumMovesRemaining = 0;
        int numOfGamesWon = 0;
        int numOfGamesLost = 0;
        double scoreToTarget = 0;

        // Calculate the conversion rate for how many moves correspond to the
        // score remaining.
        final double averageMovesPerJelly = design.getNumberOfMovesAvailable() / design.getObjectiveTarget();

        // Calculate the number of games won / lost and the sumof moves
        // remaining for the games that were won and the total normalised score
        // that was not reached.
        for (GameStateProgress progress : progressRecords) {
            if (progress.isGameWon(design)) {
                sumMovesRemaining += progress.getMovesRemaining();
                ++numOfGamesWon;
            } else {
                scoreToTarget += averageMovesPerJelly * (design.getObjectiveTarget() - progress.getJelliesRemaining());
                ++numOfGamesLost;
            }
        }
        
        final double normalizedAverageScore = numOfGamesLost == 0 ? 0.0 : scoreToTarget / ((double) numOfGamesLost);
        final double averageRemainingMoves = numOfGamesWon == 0 ? 0.0 : sumMovesRemaining / ((double) numOfGamesWon);

        return new PlayerAssessmentRecord(numOfGamesWon, numOfGamesLost, normalizedAverageScore, averageRemainingMoves);
    }
    
    // Auxiliary function that extracts a list of doubles from a list of
    // Assessment records by taking the field of averageRemainingMoves.
    private static List<Double> extractAverageMovesRemaining(List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : otherPlayers) {
            ret.add(record.averageRemainingMoves);
        }
        return ret;
    }

    // Auxiliary function that extracts a list of doubles from a list of
    // Assessment records by taking the field of normalizedAverageScore.
    private static List<Double> extractScoreToTarget(List<PlayerAssessmentRecord> otherPlayers) {
        List<Double> ret = new LinkedList<Double>();
        for (PlayerAssessmentRecord record : otherPlayers) {
            ret.add(record.normalizedAverageScore);
        }
        return ret;
    }

    // Function that counts how many players have smaller winning percentage
    // than the mean provided.
    private static int countSmallerMeans(double meanGamesWon, List<PlayerAssessmentRecord> otherPlayers) {
        int count = 0;
        for (PlayerAssessmentRecord record : otherPlayers) {
            if (record.getAverageGamesWon() <= meanGamesWon)
                ++count;
        }
        return count;
    }

    // Simple function that does not allow the percentage to be negative or
    // above 0.
    private static double sanitizePercentage(double percentage) {
        if (percentage < 0.0)
            return 0.0;
        if (percentage > 1.0)
            return 1.0;
        return percentage;
    }
    
    private static void outputResults(List<SimulatedPlayerBase> players, List<Double> scores) {
        for (int i = 0; i < scores.size(); ++i) {
            System.out.println(players.get(i).getClass().getSimpleName() + ": " + scores.get(i));
        }
    }

    private static void assessScoreBasedPlayers() {
        final int numberOfGamesToRun = 20;
        
        List<SimulatedPlayerBase> scorePlayers = new LinkedList<SimulatedPlayerBase>();
        scorePlayers.add(new ScorePlayerAlpha());
        scorePlayers.add(new ScorePlayerBeta());
        scorePlayers.add(new DepthPotentialScorePlayer(2, 10));
        scorePlayers.add(new MayanScorePlayer(2, 10));

        Design design = SimulatedPlayersTestHelpers.getBoardWithBlockersDesign();
        design.setGameMode(GameMode.HIGHSCORE);
        design.setObjectiveTarget(2400);
        design.setNumberOfMovesAvailable(12);

        outputResults(scorePlayers, evaluateScorePlayers(design, scorePlayers, numberOfGamesToRun));
    }
    
    private static void assessJellyBasedPlayers() {
        final int numberOfGamesToRun = 20;
        
        List<SimulatedPlayerBase> jellyPlayers = new LinkedList<SimulatedPlayerBase>();
        jellyPlayers.add(new ScorePlayerAlpha());
        jellyPlayers.add(new DepthPotentialJellyPlayer(2, 10));
        jellyPlayers.add(new JellyRemoverPlayerLuna(2, 10));
        jellyPlayers.add(new RuleBasedJellyPlayer());
        
        Design jellyDesign = SimulatedPlayersTestHelpers.getBoardWithBlockersAndJelliesDesign();
        jellyDesign.setGameMode(GameMode.JELLY);
        jellyDesign.setNumberOfMovesAvailable(7);


        outputResults(jellyPlayers, evaluateScorePlayers(jellyDesign, jellyPlayers, numberOfGamesToRun));
    }
    
    public static void main(String[] Args) {
        System.out.println("Score based players assessment:");
        assessScoreBasedPlayers();
        System.out.println("Jelly based players assessment:");
        assessJellyBasedPlayers();
    }
}
