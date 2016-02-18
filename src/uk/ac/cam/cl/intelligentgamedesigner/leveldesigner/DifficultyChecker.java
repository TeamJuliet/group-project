package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

public class DifficultyChecker {

    public static double estimateDifficulty (GameState gameState, Design design) {
        switch (design.getMode()) {
            case HIGHSCORE:
                return evaluateScoreLevelPerformance(gameState, design);
            case JELLY:
                return evaluateJellyLevelPerformance(gameState, design);
            default:
                return evaluateIngredientsLevelPerformance(gameState, design);
        }
    }

    /**
     * Currently, this will return a fitness between 0 (trivial) and 1 (impossible).
     *
     * The simulated players play until they have no more moves remaining, thus the only values that can be used to
     * judge performance are the score and the target score. The values will be returned following this scale:
     *
     * 0 -------------------------------------- 0.5 -------------------------------------- 1
     * EASY                                     OK                                      HARD
     * score - target score >> 0         score - target = 0              score - target << 0
     *
     * For this I will use 1 minus the sigmoid function centered on the target score and scaled appropriately.
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private static double evaluateScoreLevelPerformance (GameState gameState, Design design) {
        double center = design.getObjectiveTarget();
        double x = gameState.getGameProgress().score;

        return 1 - (1 / (1 + (Math.exp(-(x - center) / center))));
    }

    /**
     * For jelly levels, the players will either manage to clear all of the jellies, or they will not. Thus, the only
     * value that can be used to judge performance is the number of cells remaining with jelly layers above them.
     * Other metrics such as how scattered the remaining jellies are may also be taken into account at a later date.
     *
     * Currently this returns difficulty as 1 - e^(- (num jellies remaining / c)) for some constant c (= 10 for now)
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private static double evaluateJellyLevelPerformance (GameState gameState, Design design) {

        Cell[][] gameBoard = gameState.getBoard();
        double numberOfJelliesRemaining = 0;

        for (int x = 0; x < gameState.width; x++) {
            for (int y = 0; y < gameState.height; y++) {
                numberOfJelliesRemaining += gameBoard[x][y].getJellyLevel();
            }
        }

        return 1 - Math.exp(-(numberOfJelliesRemaining / 10.0));
    }

    /**
     * For ingredients levels, the players will either manage to clear all of the ingredients, or they will not.
     * Thus, the only value that can be used to judge performance is the number of ingredients remaining (and perhaps
     * the initial number to start out with).
     *
     * Currently this returns difficulty as 1 - e^(- (num ingredients remaining / c)) for some constant c (= 2 for now)
     *
     * @param gameState     The game on which the simulated player has played
     * @param design        The design of the game level
     * @return              The estimated difficulty of the level
     */
    private static double evaluateIngredientsLevelPerformance (GameState gameState, Design design) {

        return 1 - Math.exp(-(gameState.getGameProgress().ingredientsRemaining / 2.0));
    }
}
