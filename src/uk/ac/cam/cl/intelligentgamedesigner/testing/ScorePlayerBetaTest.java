package uk.ac.cam.cl.intelligentgamedesigner.testing;

import static org.junit.Assert.assertTrue;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.FIVE_MOVES_AVAILABLE;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.INFINITE_TARGET_SCORE;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.MINIMUM_TARGET_SCORE;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.SIX_CANDY_COLOURS;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.TWENTY_MOVES_AVAILABLE;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.getBoardWithBlockersAndJelliesDesign;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.getBoardWithBlockersDesign;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.SimulatedPlayersTestHelpers.getPlainBoardDesign;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.ScorePlayerBeta;

/**
 * 
 * Class that tests that the ScorePlayerBeta operates as expected for
 * boards that do not contain jellies nor ingredients).
 *
 */
public class ScorePlayerBetaTest {
    
    /**
     * Tests that the player can play and win a plain board game with simple target.
     */
    @Test
    public void playsPlainBoardSimpleTarget() {
        Design design = getPlainBoardDesign();
        design.setRules(GameMode.HIGHSCORE, FIVE_MOVES_AVAILABLE, MINIMUM_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);

        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        assertTrue (game.isGameOver());
        // Since the game is won with one move, the game state should be in won
        // state.
        assertTrue (game.isGameWon());
    }

    /**
     * Tests that the player can play a plain board with many moves.
     */
    @Test
    public void playsPlainBoardLongTarget() {
        Design design = getPlainBoardDesign();
        design.setRules(GameMode.HIGHSCORE, TWENTY_MOVES_AVAILABLE, INFINITE_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);


        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        // Simply checks that the game has ended.
        assertTrue (game.isGameOver());
    }

    /**
     * Tests that the player can play and win a board with blockers and simple target.
     */
    @Test
    public void playsBoardWithBlockersSimpleTarget() {
        Design design = getBoardWithBlockersDesign();
        design.setRules(GameMode.HIGHSCORE, FIVE_MOVES_AVAILABLE, MINIMUM_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);

        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        assertTrue (game.isGameOver());
        assertTrue (game.isGameWon());
    }

    /**
     * Tests that the player can play a board with blockers and many moves.
     */
    @Test
    public void playsBoardWithBlockersLongTarget() {
        Design design = getBoardWithBlockersDesign();
        design.setRules(GameMode.HIGHSCORE, TWENTY_MOVES_AVAILABLE, INFINITE_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);


        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
                break;
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        assertTrue (game.isGameOver());
    }
    
    /**
     * Tests that the player can play and win a board with blockers and simple target.
     */
    @Test
    public void playsBoardWithBlockersAndJelliesSimpleTarget() {
        Design design = getBoardWithBlockersAndJelliesDesign();
        design.setRules(GameMode.HIGHSCORE, FIVE_MOVES_AVAILABLE, MINIMUM_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);

        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        assertTrue (game.isGameOver());
        assertTrue (game.isGameWon());
    }

    /**
     * Tests that the player can play a board with blockers and many moves.
     */
    @Test
    public void playsBoardWithBlockersAndJelliesLongTarget() {
        Design design = getBoardWithBlockersAndJelliesDesign();
        design.setRules(GameMode.HIGHSCORE, TWENTY_MOVES_AVAILABLE, INFINITE_TARGET_SCORE, SIX_CANDY_COLOURS);
        GameState game = new GameState(design);


        ScorePlayerBeta player = new ScorePlayerBeta();

        while (!game.isGameOver()) {
            try {
                Move move = player.calculateBestMove(game);
                game.makeFullMove(move);
            } catch (NoMovesFoundException e) {
                e.printStackTrace();
                break;
            } catch (InvalidMoveException e) {
                // There shouldn't be an invalid move attempted.
                assertTrue (false);
                e.printStackTrace();
            }
        }

        assertTrue (game.isGameOver());
    }
}
