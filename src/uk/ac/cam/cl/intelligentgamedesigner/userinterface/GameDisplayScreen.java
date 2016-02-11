package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.experimental.CellChooser;
import uk.ac.cam.cl.intelligentgamedesigner.experimental.GameDisplay;

//Defines functionality shared by the human and simulated player games
//Displays the game, score and how to handle animations etc.
public abstract class GameDisplayScreen extends DisplayScreen{
	protected JButton quit_button;
	protected GameBoard board;
	protected JLabel objective_text;
	protected JLabel moves_left_text;
	protected JLabel game_mode_text;
	protected JLabel score_text;
	
	//internal state/info
	protected Cell[][] theBoard;
	protected int objective;
	protected int moves_left;
	protected GameMode game_mode;
	protected int score;
	protected Design level;
	protected static final int wait_time = 400;
	
	//game stuff
	GameState theGame;
	
	public GameDisplayScreen(){
		super();
	}
	
	protected void giveInfo(Design level){
		this.level = level;
		this.theBoard = level.getBoard();
		this.objective = level.getObjectiveTarget();
		this.moves_left = level.getNumberOfMovesAvailable();
		this.game_mode = level.getMode();
		this.score = 0;
	}
	protected void setInfo(){
		board.setBoard(theBoard);
		switch(game_mode){
		case HIGHSCORE:
			game_mode_text.setText("Highscore Mode");
			objective_text.setText("Get "+objective+" points!");
			break;
		case JELLY:
			game_mode_text.setText("Jelly Clear Mode");
			objective_text.setText("Clear all the jellies!");
			break;
		case INGREDIENTS:
			game_mode_text.setText("Ingredients Mode");
			objective_text.setText("Clear "+objective+" more ingredients!");
			break;
		}
		moves_left_text.setText("Moves remaining: "+moves_left);
		score_text.setText("Score: "+score);
	}
	
	protected void initialiseGame(){
		theGame = new GameState(level);
		update();
	}
	
	protected void update(){
		theBoard = theGame.getBoard();
		score = theGame.getScore();
		moves_left = theGame.getMovesRemaining();
		setInfo();
	}
	
	public void playMove(Move move){
		try {
			theGame.makeMove(move);
			while(theGame.makeSmallMove()) {
				update();		
				board.paintImmediately(0,0,InterfaceManager.screenWidth(),InterfaceManager.screenHeight());
				Thread.sleep(wait_time);
			}
		} catch (InvalidMoveException ex) {
			System.err.println("Invalid move");
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		endGameCheck();
	}
	
	protected void endGameCheck(){
		if(theGame.isGameOver()){
			stopGame();
			
			//make a box with all the end game info
			JPanel end_game_panel = new JPanel();
			end_game_panel.setLayout(new BoxLayout(end_game_panel,BoxLayout.Y_AXIS));
			if(theGame.isGameWon()){
				end_game_panel.add(new JLabel("Congratulations!"),SwingConstants.CENTER);
				//if(theGame.getGameMode() != GameMode.HIGHSCORE){
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("You finished with "+theGame.getMovesRemaining()+ " moves remaining."));					
				//}
			}
			else {
				end_game_panel.add(new JLabel("Better Luck next time..."),SwingConstants.CENTER);
				switch(theGame.getGameMode()){
				case HIGHSCORE:
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("You didn't reach the required score"));
					break;
				case JELLY:
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("You didn't manage to clear the jellies fast enough."));
					break;
				case INGREDIENTS:
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("You had another "+theGame.getIngredientsRemaining()+" left to collect."));
					break;
				}
			}
			end_game_panel.add(Box.createRigidArea(new Dimension(0, 20)));
			end_game_panel.add(new JLabel("You finished with a score of "+theGame.getScore()));
			end_game_panel.add(Box.createRigidArea(new Dimension(0, 20)));
			end_game_panel.add(new JLabel("Taking you back to the level display menu."));
			
			JOptionPane.showMessageDialog(this,end_game_panel,"Game Over",JOptionPane.INFORMATION_MESSAGE);
			
			//after the message, quit the game
			InterfaceManager.switchScreen(Windows.DISPLAY);
		}
	}
	protected void stopGame(){
		
	}
	
	protected abstract GameBoard specificGameBoard();

	@Override
	protected void makeItems() {
		quit_button = new JButton("Quit Game");
		board = specificGameBoard();
		objective_text = new JLabel();
		game_mode_text = new JLabel();
		moves_left_text = new JLabel();
		score_text = new JLabel();
	}

	@Override
	protected void setUpItems() {
		quit_button.setToolTipText("Quit playing the level and go back to the level display");
		quit_button.setActionCommand("quit");
		quit_button.addActionListener(this);		
	}

	@Override
	protected void placeItems() {
		//sort out the window's layout settings:
		setLayout(null);
		
		//make a box with all the custom settings
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats,BoxLayout.Y_AXIS));
		stats.setBorder(BorderFactory.createLineBorder(Color.black));
		stats.add(Box.createRigidArea(new Dimension(0, 20)));
		stats.add(score_text);
		stats.add(Box.createRigidArea(new Dimension(0, 20)));
		stats.add(game_mode_text);
		stats.add(Box.createRigidArea(new Dimension(0, 20)));
		stats.add(objective_text);
		stats.add(Box.createRigidArea(new Dimension(0, 20)));
		stats.add(moves_left_text);
		stats.add(Box.createRigidArea(new Dimension(0, 20)));
		add(stats);
		
		add(board);
		add(quit_button);

		//set the locations
		position(stats,0.75,0.6,300,160);
		positionBoard(board,0.4,0.5);
		position(quit_button,0.1,0.9,100,30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		
		case "quit":
			InterfaceManager.switchScreen(Windows.DISPLAY);
			break;
		}
	}
}

