package uk.ac.cam.cl.intelligentgamedesigner.testing;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Candy;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateProgress;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Scoring;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;
import uk.ac.cam.cl.intelligentgamedesigner.experimental.GameDisplay;

import static org.junit.Assert.*;
import static uk.ac.cam.cl.intelligentgamedesigner.testing.GameStateTestHelpers.*;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class that tests that the basic matches, combinations, ingredient and blockers behaviour works as expected.
 * 
 * To write a test for GameState you must create an initial layout of the candies in place.
 * e.g.
 *  Candy[][] board = { 
 *       { RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
 *       { BLUE_CANDY, RED_CANDY, RED_CANDY },      
 *       { GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
 *  };
 *  
 *  Create a game state with some initial conditions:
 *  e.g.
 *     GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
 *         new UnmoveableCandyGenerator());
 *  
 *  Try the move you want on the board (and fail test if move is not accepted).
 *  e.g.
 *     try {
 *        gameState.makeFullMove(new Move(new Position(0,1), new Position(0, 0)));
 *     } catch (InvalidMoveException e) {
 *        assertTrue(false);
 *     }
 *     
 *  Check that the board is as expected and the progress is correct.
 *  
 *  Candy[][] correctBoard = {
 *           { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY }, 
 *           { BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
 *          { GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
 *   };
 *   
 *   assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
 *   assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_4, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
 *   
 *   Tests remaining:
 *   	
 */
public class GameStateTest {

	@Test
	public void matchesHorizontal3() {

		Candy[][] board = { 
				{ RED_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
				{ BLUE_CANDY, RED_CANDY, RED_CANDY },      
				{ GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
		};
		
		GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
				new UnmovableCandyGenerator());
				
		try {
			gameState.makeFullMove(new Move(new Position(0,1), new Position(0, 0)));
		} catch (InvalidMoveException e) {
			assertTrue(false);
		} 

		Candy[][] correctBoard = {
				{ UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY }, 
				{ BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
				{ GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
		};
		
		// Checks whether it forms a horizontal move.
	       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
	       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
	}
	
   @Test
    public void matchesVertical3() {

        Candy[][] board = { 
                { RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
                { RED_CANDY, YELLOW_CANDY, GREEN_CANDY },      
                { GREEN_CANDY, RED_CANDY, YELLOW_CANDY } 
        };
        
        GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
                new UnmovableCandyGenerator());
                
        try {
            gameState.makeFullMove(new Move(new Position(0,2), new Position(1, 2)));
        } catch (InvalidMoveException e) {
            assertTrue(false);
        } 

        Candy[][] correctBoard = { 
                { UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY },  
                { UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY },      
                { UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY } 
        };
        
        assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
        assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
        
    }
   
   @Test
   public void matchesVertical4() {

       Candy[][] board = { 
               { RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
               { RED_CANDY, YELLOW_CANDY, GREEN_CANDY },      
               { GREEN_CANDY, RED_CANDY, YELLOW_CANDY },
               { RED_CANDY, YELLOW_CANDY, GREEN_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0,2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 

       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY },  
               { UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY },      
               { UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY },
               { HORIZONTAL_RED, YELLOW_CANDY, GREEN_CANDY }
       };
       
       
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_4, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
       
   }
   
   @Test
   public void matchesHorizontal4() {

       Candy[][] board = { 
               { YELLOW_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY },  
               { RED_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY },      
               { GREEN_CANDY, RED_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, YELLOW_CANDY, GREEN_CANDY, GREEN_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 0), new Position(2, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 

       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, UNMOVEABLE_CANDY },  
               { YELLOW_CANDY, GREEN_CANDY, VERTICAL_RED, YELLOW_CANDY },      
               { GREEN_CANDY, RED_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, YELLOW_CANDY, GREEN_CANDY, GREEN_CANDY }
       };
       
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_4, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
       
   }
   
   @Test
   public void matchesTShape() {

       Candy[][] board = { 
               { YELLOW_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
               { RED_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY },      
               { GREEN_CANDY, RED_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, RED_CANDY, GREEN_CANDY, GREEN_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 0), new Position(1, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 

       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY },  
               { YELLOW_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY },      
               { GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, WRAPPED_RED, GREEN_CANDY, GREEN_CANDY }
       };
       
       // displayCellBoard(gameState.getBoard());
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_WRAPPED_CANDY, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesRotatedTShape() {

       Candy[][] board = { 
               { YELLOW_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY },  
               { RED_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY },      
               { RED_CANDY, RED_CANDY, YELLOW_CANDY, RED_CANDY },
               { GREEN_CANDY, GREEN_CANDY, RED_CANDY, GREEN_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 2), new Position(3, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 

       Candy[][] correctBoard = { 
    		   { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY },
               { YELLOW_CANDY, RED_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY },  
               { RED_CANDY, GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY },      
               { GREEN_CANDY, GREEN_CANDY, WRAPPED_RED, GREEN_CANDY }
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_WRAPPED_CANDY, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesLShape() {

       Candy[][] board = { 
               { YELLOW_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
               { BLUE_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY },      
               { GREEN_CANDY, RED_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, RED_CANDY, GREEN_CANDY, GREEN_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 0), new Position(1, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 

       Candy[][] correctBoard = { 
               { YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY },  
               { BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY },      
               { GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY },
               { RED_CANDY, WRAPPED_RED, GREEN_CANDY, GREEN_CANDY }
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_WRAPPED_CANDY, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesRotatedLShape() {

       Candy[][] board = { 
               { YELLOW_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
               { BLUE_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY },      
               { GREEN_CANDY, YELLOW_CANDY, RED_CANDY, BLUE_CANDY },
               { RED_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY }
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(3, 3), new Position(2, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY },  
               { YELLOW_CANDY, RED_CANDY, UNMOVEABLE_CANDY, RED_CANDY },      
               { BLUE_CANDY, GREEN_CANDY, GREEN_CANDY, BLUE_CANDY },
               { GREEN_CANDY, YELLOW_CANDY, WRAPPED_RED, GREEN_CANDY }
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_WRAPPED_CANDY, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesHorizontal5() {

       Candy[][] board = { 
               { RED_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY },  
               { BLUE_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY, YELLOW_CANDY },
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 0), new Position(2, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, COLOR_BOMB, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY },  
               { BLUE_CANDY, GREEN_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY },
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_BOMB, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesVertical5() {

       Candy[][] board = { 
    		   {RED_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, RED_CANDY},
    		   {RED_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, BLUE_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {UNMOVEABLE_CANDY, GREEN_CANDY},
    		   {UNMOVEABLE_CANDY, GREEN_CANDY},
    		   {UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {COLOR_BOMB, BLUE_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_BOMB, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesLongerT() {

       Candy[][] board = { 
    		   { GREEN_CANDY, BLUE_CANDY, RED_CANDY, BLUE_CANDY, GREEN_CANDY },
    		   { RED_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY },  
               { BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY },
               { BLUE_CANDY, YELLOW_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY },
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 0), new Position(2, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
               { UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY },
    		   { GREEN_CANDY, BLUE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, GREEN_CANDY },
               { BLUE_CANDY, GREEN_CANDY, GREEN_CANDY, BLUE_CANDY, YELLOW_CANDY },
               { BLUE_CANDY, YELLOW_CANDY, COLOR_BOMB, GREEN_CANDY, YELLOW_CANDY },
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_BOMB, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesRotatedLongerT() {

       Candy[][] board = { 
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {RED_CANDY, GREEN_CANDY, RED_CANDY, RED_CANDY},
    		   {BLUE_CANDY, RED_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, COLOR_BOMB, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MADE_BOMB, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void movesBombBomb() {

       Candy[][] board = { 
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {COLOR_BOMB, COLOR_BOMB, RED_CANDY, RED_CANDY},
    		   {BLUE_CANDY, RED_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_BOMB * 2 + (20 - 2) * Scoring.BOMB_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void movesBombBombWithIngredients() {

       Candy[][] board = { 
               {GREEN_CANDY, RED_CANDY, GREEN_CANDY, INGREDIENT},
               {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
               {COLOR_BOMB, COLOR_BOMB, RED_CANDY, RED_CANDY},
               {BLUE_CANDY, RED_CANDY, YELLOW_CANDY, GREEN_CANDY},
               {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       Cell[][] cellBoard = cellBoardFromCandies(board);
       cellBoard[3][4].setIngredientSink();
       
       GameState gameState = new GameState(cellBoard, ONE_INRGEDIENT_PROGRESS,
               new UnmovableCandyGenerator());

       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
       };

       Cell[][] correctCellBoard = cellBoardFromCandies(correctBoard);
       correctCellBoard[3][4].setIngredientSink();
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, correctCellBoard));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_BOMB * 2 + (20 - 3) * Scoring.BOMB_INDIVIDUAL + Scoring.BROUGHT_INGREDIENT_DOWN, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void movesStrippedStripped() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {VERTICAL_GREEN, HORIZONTAL_RED, BLUE_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_STRIPPED_CANDY * 2 + 6 * Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesHorizontallyStrippedHorizontally() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, HORIZONTAL_RED, RED_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, RED_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(3, 2), new Position(3, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_STRIPPED_CANDY + Scoring.MATCHED_3 + Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesHorizontallyStrippedVertically() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, HORIZONTAL_RED, YELLOW_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 3), new Position(2, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, YELLOW_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_STRIPPED_CANDY + Scoring.MATCHED_3 + 3 * Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesVerticallyStrippedHorizontally() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, VERTICAL_RED, RED_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, RED_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(3, 2), new Position(3, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_STRIPPED_CANDY + Scoring.MATCHED_3 + 4 * Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesVerticallyStrippedVertically() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, VERTICAL_RED, YELLOW_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 3), new Position(2, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
         
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_STRIPPED_CANDY + Scoring.MATCHED_3 + 2 * Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void matchesBombNormal() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, COLOR_BOMB, YELLOW_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(0, 2), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {UNMOVEABLE_CANDY, YELLOW_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, RED_CANDY, RED_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_BOMB + 6 * Scoring.BOMB_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
	@Test
	public void breaksLayerOfJelly() {

		Candy[][] board = { 
				{ RED_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
				{ BLUE_CANDY, RED_CANDY, RED_CANDY },      
				{ GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
		};
		
		Cell[][] cellBoard = cellBoardFromCandies(board);
		cellBoard[1][1] = new Cell(CellType.NORMAL, new Candy(RED_CANDY), 2, false);
		
		GameState gameState = new GameState(cellBoard, INITIAL_PROGRESS_WITH_2_JELLIES,
				new UnmovableCandyGenerator());
				
		try {
			gameState.makeFullMove(new Move(new Position(0,1), new Position(0, 0)));
		} catch (InvalidMoveException e) {
			assertTrue(false);
		} 

		Candy[][] correctBoard = {
				{ UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY }, 
				{ BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
				{ GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
		};
		
		Cell[][] correctCellBoard = cellBoardFromCandies(correctBoard);
		correctCellBoard[1][1] = new Cell(CellType.NORMAL, new Candy(GREEN_CANDY), 1, false);
		
		// Checks whether it forms a horizontal move.
	    assertTrue(haveSameBoard(gameState, correctCellBoard));
	    assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3 + Scoring.MATCHED_A_JELLY, ONE_JELLY, NO_INGREDIENTS, TWO_MOVES_LEFT)));
	}
   
   @Test
   public void detonatesWrapped() {

       Candy[][] board = { 
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {RED_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, YELLOW_CANDY, WRAPPED_RED, BLUE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 4), new Position(2, 5)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {RED_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, UNMOVEABLE_CANDY, RED_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, GREEN_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
       };
         
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_WRAPPED_CANDY * 2 + Scoring.MATCHED_3 + 14 * Scoring.WRAPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void detonatesStrippedWrapped() {

       Candy[][] board = { 
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {RED_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, RED_CANDY, VERTICAL_GREEN, RED_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, YELLOW_CANDY, WRAPPED_RED, BLUE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
       
       try {
           gameState.makeFullMove(new Move(new Position(2, 5), new Position(2, 4)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {RED_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
       };
         
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_WRAPPED_CANDY + Scoring.DETONATE_STRIPPED_CANDY + (3 * 5 + 3 * 7 - 2) * Scoring.WRAPPED_STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void detonatesBombStripped() {

       Candy[][] board = { 
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {RED_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, PURPLE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY, YELLOW_CANDY},
    		   {RED_CANDY, YELLOW_CANDY, RED_CANDY, BLUE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, PURPLE_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {HORIZONTAL_PURPLE, COLOR_BOMB, GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
       
       try {
           gameState.makeFullMove(new Move(new Position(0, 9), new Position(1, 9)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {RED_CANDY, GREEN_CANDY, YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {RED_CANDY, YELLOW_CANDY, RED_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, YELLOW_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, RED_CANDY, BLUE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {YELLOW_CANDY, GREEN_CANDY, RED_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
       };
         
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(3 * Scoring.DETONATE_STRIPPED_CANDY + 2 * Scoring.MADE_STRIPPED_CANDY + Scoring.DETONATE_BOMB + (22 - 3) * Scoring.STRIPPED_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }

   @Test
   public void bringsDownIngredients() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, INGREDIENT},
    		   {YELLOW_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, INGREDIENT, GREEN_CANDY},
    		   {BLUE_CANDY, BLUE_CANDY, INGREDIENT, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, BLUE_CANDY},
       };
       
       Cell[][] cellBoard = cellBoardFromCandies(board);
       // Set an ingredient sink at (2, 4)
       cellBoard[2][4] = new Cell(CellType.NORMAL, cellBoard[2][4].getCandy(), ZERO_JELLY_LEVEL, ACCEPT_INGREDIENTS);
       
       GameState gameState = new GameState(cellBoard, THREE_INRGEDIENTS_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 4), new Position(1, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
       // displayCellBoard(gameState.getBoard());
       Candy[][] correctBoardCandies = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {YELLOW_CANDY, YELLOW_CANDY, UNMOVEABLE_CANDY, INGREDIENT},
    		   {GREEN_CANDY, RED_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY},
    		   {BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, GREEN_CANDY, GREEN_CANDY},
       };
       
       Cell[][] correctBoard = cellBoardFromCandies(correctBoardCandies);
       correctBoard[2][4] = new Cell(CellType.NORMAL, correctBoard[2][4].getCandy(), ZERO_JELLY_LEVEL, ACCEPT_INGREDIENTS);
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, correctBoard));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3 + 2 * Scoring.BROUGHT_INGREDIENT_DOWN, NO_JELLIES, ONE_INGREDIENT_REMAINING, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void swapsIngredient() {

       Candy[][] board = { 
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, RED_CANDY},
    		   {YELLOW_CANDY, RED_CANDY, GREEN_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, BLUE_CANDY, INGREDIENT, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, BLUE_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), ONE_INRGEDIENT_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(2, 2), new Position(2, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
       
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, RED_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, BLUE_CANDY},
    		   {YELLOW_CANDY, RED_CANDY, GREEN_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, INGREDIENT, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, BLUE_CANDY},
       };
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, ONE_INGREDIENT_REMAINING, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void countsComboScoring() {

       Candy[][] board = { 
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY},
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(3, 2), new Position(3, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3 * 3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void removesLiquoriceNormally() {

       Candy[][] board = { 
    		   {GREEN_CANDY, BLUE_CANDY, YELLOW_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, BLUE_CANDY, RED_CANDY},
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, GREEN_CANDY},
       };
       
       Cell[][] cellBoard = cellBoardFromCandies(board);
       cellBoard[0][1] = new Cell(CellType.LIQUORICE, cellBoard[0][1].getCandy());
       
       GameState gameState = new GameState(cellBoard, NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
       
       try {
           gameState.makeFullMove(new Move(new Position(1, 0), new Position(1, 1)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, YELLOW_CANDY, RED_CANDY},
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, GREEN_CANDY},
       };
       // displayCellBoard(gameState.getBoard());
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
   @Test
   public void removesLiquoriceLockOnCombo() {

       Candy[][] board = { 
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, YELLOW_CANDY},
    		   {BLUE_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY},
    		   {GREEN_CANDY, BLUE_CANDY, BLUE_CANDY, GREEN_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       Cell[][] cellBoard = cellBoardFromCandies(board);
       cellBoard[0][1] = new Cell(CellType.LIQUORICE, cellBoard[0][1].getCandy());
       
       GameState gameState = new GameState(cellBoard, NORMAL_INITIAL_PROGRESS,
               new UnmovableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(3, 2), new Position(3, 3)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
     
       Candy[][] correctBoard = { 
    		   {GREEN_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
    		   {BLUE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, YELLOW_CANDY},
    		   {GREEN_CANDY, RED_CANDY, GREEN_CANDY, RED_CANDY},
    		   {BLUE_CANDY, GREEN_CANDY, RED_CANDY, GREEN_CANDY},
    		   {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
             
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.MATCHED_3 * 3 , NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   }
   
  /* @Test
   public void edgeCaseSpecialCandyFormation() {

       Candy[][] board = { 
               {GREEN_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
               {BLUE_CANDY, VERTICAL_GREEN, YELLOW_CANDY, BLUE_CANDY},
               {GREEN_CANDY, BLUE_CANDY, GREEN_CANDY, GREEN_CANDY},
               {BLUE_CANDY, GREEN_CANDY, RED_CANDY, BLUE_CANDY},
               {YELLOW_CANDY, GREEN_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
               new UnmoveableCandyGenerator());
               
       try {
           gameState.makeFullMove(new Move(new Position(1, 1), new Position(1, 2)));
       } catch (InvalidMoveException e) {
           assertTrue(false);
       } 
       displayCellBoard(gameState.getBoard());
       Candy[][] correctBoard = { 
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, UNMOVEABLE_CANDY},
               {UNMOVEABLE_CANDY, UNMOVEABLE_CANDY, BLUE_CANDY, YELLOW_CANDY},
               {UNMOVEABLE_CANDY, YELLOW_CANDY, YELLOW_CANDY, BLUE_CANDY},
               {BLUE_CANDY, RED_CANDY, RED_CANDY, BLUE_CANDY},
               {BLUE_CANDY, YELLOW_CANDY, BLUE_CANDY, YELLOW_CANDY},
       };
       
       // Checks whether it forms a vertical move.
       assertTrue(haveSameBoard(gameState, cellBoardFromCandies(correctBoard)));
       assertTrue(haveSameProgress(gameState, new GameStateProgress(Scoring.DETONATE_BOMB + 6 * Scoring.BOMB_INDIVIDUAL, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT)));
   } */
   
   public  Cell[][] cellBoardFromCandies(Candy[][] candies) {
       Cell[][] cellBoard = new Cell[candies[0].length][candies.length];
       for (int i = 0; i < candies.length; ++i) {
           for (int j = 0; j < candies[0].length; ++j) {
               cellBoard[j][i] = new Cell(CellType.NORMAL, new Candy(candies[i][j]));
           }
       }
       return cellBoard;
   }
   
   // Function to assist checking what is wrong with the board. 
   public void displayCellBoard(Cell[][] board) {
       board = copyBoard(board);
       JPanel generalPanel = new JPanel();
       JFrame app = new JFrame();
       GameDisplay gamePanel = new GameDisplay(board.length, board[0].length, 50);
       gamePanel.setBoard(board);
       generalPanel.add(gamePanel);
       app.add(generalPanel);
       app.setSize(new Dimension(600, 700));
       app.setVisible(true);
       app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       try {
           Thread.sleep(50000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

   public boolean haveSameBoard(GameState gameState, Cell[][] board) {
       boolean areEqual = true;
       for (int i = 0; i < board.length; ++i) {
           for (int j = 0; j < board[0].length; ++j) {
        	   if (!board[i][j].equals(gameState.getCell(i, j)))
        		   System.err.println("Problem at " + i + " " + j);
               areEqual = areEqual && board[i][j].equals(gameState.getCell(i, j));
               
           }
       }
       return areEqual ; 
   }
   
   public boolean haveSameProgress(GameState gameState, GameStateProgress progress) {
	   System.out.println(progress);
	   System.out.println(gameState.getGameProgress());
       return progress.equals(gameState.getGameProgress());
   }

   public  Cell[][] copyBoard(Cell[][] board) {
       Cell[][] ret = new Cell[board.length][board[0].length];
       for (int i = 0; i < board.length; ++i) {
           for (int j = 0; j < board[0].length; ++j) {
               ret[i][j] = new Cell(board[i][j]);
           }
       }
       return ret;
   }
   
}
