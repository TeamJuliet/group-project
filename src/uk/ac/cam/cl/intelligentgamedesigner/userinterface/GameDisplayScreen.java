package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

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
	
	public GameDisplayScreen(){
		super();
	}
	
	protected void giveInfo(
			Cell[][] theBoard,
			int objective,
			int moves_left,
			GameMode game_mode,
			int score
			){
		this.theBoard = theBoard;
		this.objective = objective;
		this.moves_left = moves_left;
		this.game_mode = game_mode;
		this.score = score;
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
			objective_text.setText("Clera all the jellies!");
			break;
		case INGREDIENTS:
			game_mode_text.setText("Ingredients Mode");
			objective_text.setText("Clear "+objective+" more ingredients!");
			break;
		}
		moves_left_text.setText("Moves remaining: "+moves_left);
		score_text.setText("Score: "+score);
	}

	@Override
	protected void makeItems() {
		quit_button = new JButton("Quit Game");
		board = new GameBoard(new Design());
		if(board != null)System.out.println("board made");
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
		position(stats,0.85,0.5,300,600);
		position(board,0.4,0.3,800,800);
		position(quit_button,0.1,0.9,150,40);
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

