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
import javax.swing.JRadioButton;
import javax.swing.Timer;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.NoMovesFoundException;
import uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers.SimulatedPlayerBase;

//defines the functionality specific to the Simulated Player viewer
public class ComputerGameDisplayScreen extends GameDisplayScreen{
	private JRadioButton auto_play;
	private JButton next_move;
	private boolean auto_playing; 
	private SimulatedPlayerBase player;
	
	Timer timer;
	private static final int waitspeed = 500;
	
	public ComputerGameDisplayScreen(){
		super();
		identifier = "Simulated Player";
		auto_playing = false;
		
		timer = new Timer(waitspeed,this);
		timer.setInitialDelay(waitspeed);
		timer.addActionListener(this);
		timer.setActionCommand("trigger");
	}
	
	public void givePlayer(SimulatedPlayerBase player){
		this.player = player;
	}
	@Override
	public void initialiseGame(){
		super.initialiseGame();
		
	}

	@Override
	protected GameBoard specificGameBoard() {
		return new ComputerGameBoard(new Design());
	}

	@Override
	protected void makeItems() {
		super.makeItems();
		auto_play = new JRadioButton("Auto-play Simulated Player Moves");
		next_move = new JButton("Play Next Move");
	}

	@Override
	protected void setUpItems() {
		super.setUpItems();
		auto_play.setActionCommand("mode");
		auto_play.addActionListener(this);
		next_move.setActionCommand("next");
		next_move.addActionListener(this);	
	}

	@Override
	protected void placeItems() {
		super.placeItems();

		//make a box with all the added simulated player settings
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls,BoxLayout.Y_AXIS));
		controls.setBorder(BorderFactory.createLineBorder(Color.black));
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		controls.add(auto_play);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		controls.add(next_move);
		controls.add(Box.createRigidArea(new Dimension(0, 20)));
		add(controls);

		//set the locations
		position(controls,0.85,0.3,300,100);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		//additional button effects
		switch(e.getActionCommand()){
		
		case "mode":
			auto_playing = auto_play.isSelected();
			next_move.setEnabled(!auto_playing);
			if(auto_playing){
				timer.setDelay(waitspeed);
				timer.start();
			} else {
				timer.stop();
			}
			nextMove();
			break;
		case "next":
			nextMove();
			break;
		case "trigger":
			if(auto_playing){
				nextMove();
			}
			else System.out.println("Shouldn't be timing...");
			timer.setDelay(waitspeed);
			break;
		}
	}
	
	private void nextMove(){
		System.out.println("trying to find next move...");
		try{
			Move next = player.calculateBestMove(theGame);

			((ComputerGameBoard)board).showMove(next);
			Thread.sleep(wait_time*2);
			((ComputerGameBoard)board).hideMove();

			playMove(next);
			System.out.println("move found!");
		} catch(NoMovesFoundException e) {
			System.out.println("move not found...");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
