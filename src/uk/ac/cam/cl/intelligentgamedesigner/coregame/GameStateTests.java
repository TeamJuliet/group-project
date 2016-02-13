package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import org.junit.Test;

import uk.ac.cam.cl.intelligentgamedesigner.experimental.GameDisplay;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.TestHelpers.*;

import static org.junit.Assert.*;


import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
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
 *   // Checks whether it forms a horizontal move.
 *   assertTrue(areSame(gameState,
 *           new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT),
 *           cellBoardFromCandies(correctBoard)));
 */
public class GameStateTests {

	@Test
	public void matchesHorizontal3() {

		Candy[][] board = { 
				{ RED_CANDY, GREEN_CANDY, YELLOW_CANDY }, 
				{ BLUE_CANDY, RED_CANDY, RED_CANDY },      
				{ GREEN_CANDY, GREEN_CANDY, YELLOW_CANDY } 
		};
		
		GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
				new UnmoveableCandyGenerator());
				
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
		assertTrue(areSame(gameState,
				new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT),
				cellBoardFromCandies(correctBoard)));
		
	}
	
   @Test
    public void matchesVertical3() {

        Candy[][] board = { 
                { RED_CANDY, GREEN_CANDY, YELLOW_CANDY },  
                { RED_CANDY, YELLOW_CANDY, GREEN_CANDY },      
                { GREEN_CANDY, RED_CANDY, YELLOW_CANDY } 
        };
        
        GameState gameState = new GameState(cellBoardFromCandies(board), NORMAL_INITIAL_PROGRESS,
                new UnmoveableCandyGenerator());
                
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
        
        // Checks whether it forms a vertical move.
        assertTrue(areSame(gameState,
                new GameStateProgress(Scoring.MATCHED_3, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT),
                cellBoardFromCandies(correctBoard)));
        
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
               new UnmoveableCandyGenerator());
               
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
       
       
       // Checks whether it forms a vertical move.
       assertTrue(areSame(gameState,
               new GameStateProgress(Scoring.MATCHED_4, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT),
               cellBoardFromCandies(correctBoard)));
       
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
               new UnmoveableCandyGenerator());
               
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
       assertTrue(areSame(gameState,
               new GameStateProgress(Scoring.MATCHED_4, NO_JELLIES, NO_INGREDIENTS, TWO_MOVES_LEFT),
               cellBoardFromCandies(correctBoard)));
       
   }
   
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

   public boolean areSame(GameState gameState, GameStateProgress progress, Cell[][] board) {
       boolean areEqual = true;
       for (int i = 0; i < board.length; ++i) {
           for (int j = 0; j < board[0].length; ++j) {
               areEqual = areEqual && board[i][j].equals(gameState.getCell(i, j));
           }
       }
       return areEqual && progress.equals(gameState.getGameProgress());
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
