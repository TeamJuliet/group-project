package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateProgressView;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.ProcessState;

//Defines functionality shared by the human and simulated player games
//Displays the game, score and how to handle animations etc.
public abstract class GameDisplayScreen extends DisplayScreen implements PropertyChangeListener{
	
	protected JButton quit_button;
	protected GameBoard board;
	protected JLabel objective_text;
	protected JLabel moves_left_text;
	protected JLabel game_mode_text;
	protected JLabel score_text;
	
	//internal state/info
	protected boolean playing_move;
	protected Cell[][] theBoard;
	protected int objective;
	protected int moves_left;
	protected GameMode game_mode;
	protected int score;
	protected Design level;
	protected static final int wait_time = 400;
	
	//game stuff
	GameState theGame;
	AnimationThread animation;
	
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
			objective_text.setText("Clear "+objective+" more jelly layers!");
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
		animation = new AnimationThread();
        animation.addPropertyChangeListener(this);
		update();
	}
	
	protected void update(){
		theBoard = theGame.getBoard();
		GameStateProgressView progress = theGame.getGameProgress();
		score = progress.score;
		moves_left = progress.movesRemaining;
		if(game_mode == GameMode.INGREDIENTS)objective = progress.ingredientsRemaining;
		if(game_mode == GameMode.JELLY)objective = progress.jelliesRemaining;
		setInfo();
	}
	
	public void playMove(Move move){
		playing_move = true;
		
		animation.initialise(theGame, move, board);
        animation.execute();	
	}
	
	protected void endGameCheck(){
		if(theGame.isGameOver()){
			stopGame();
			
			//make a box with all the end game info
			JPanel end_game_panel = new JPanel();
			end_game_panel.setLayout(new BoxLayout(end_game_panel,BoxLayout.Y_AXIS));
			GameStateProgressView progress = theGame.getGameProgress();
			if(theGame.isGameWon()){
				end_game_panel.add(new JLabel("Congratulations!"),SwingConstants.CENTER);
				//if(theGame.getGameMode() != GameMode.HIGHSCORE){
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("You finished with " + progress.movesRemaining+ " moves remaining."));					
				//}
			}
			else {
				if(theGame.didFailShuffle()){
					stopGame();

					end_game_panel.add(new JLabel("Sorry, game over..."),SwingConstants.CENTER);
					end_game_panel.add(Box.createRigidArea(new Dimension(0, 10)));
					end_game_panel.add(new JLabel("The board could not be reshuffled to make any matches"));
				}
				else {
					end_game_panel.add(new JLabel("Better luck next time..."),SwingConstants.CENTER);
					switch(theGame.getLevelDesign().getMode()){
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
						end_game_panel.add(new JLabel("You had another " + progress.ingredientsRemaining + " left to collect."));
						break;
					}
				}
			}
			end_game_panel.add(Box.createRigidArea(new Dimension(0, 20)));
			end_game_panel.add(new JLabel("You finished with a score of " + progress.score));
			end_game_panel.add(Box.createRigidArea(new Dimension(0, 20)));
			end_game_panel.add(new JLabel("Taking you back to the level display menu."));
			
			JOptionPane.showMessageDialog(this,end_game_panel,"Game Over",JOptionPane.INFORMATION_MESSAGE);
			
			//after the message, quit the game
			InterfaceManager.switchScreen(Windows.DISPLAY);
		}
	}
	protected void stopGame(){
		animation = null;
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
			playing_move = false;
			InterfaceManager.switchScreen(Windows.DISPLAY);
			break;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {	
		if(playing_move){
			switch(evt.getPropertyName()){
			case AnimationThread.NEW_STATE:
				theGame = (GameState)evt.getNewValue();
				update();
				break;
			case AnimationThread.FINISHED:
				playing_move = false;
				theGame = (GameState)evt.getNewValue();
				update();
				endGameCheck();
				break;
			}
		}
	}
}

