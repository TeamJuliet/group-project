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
	
	public ComputerGameDisplayScreen(){
		super();
		identifier = "Simulated Player";
		auto_playing = false;
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
		return new GameBoard(new Design());
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
		position(controls,0.85,0.2,300,200);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		//additional button effects
		switch(e.getActionCommand()){
		
		case "mode":
			auto_playing = auto_play.isSelected();
			next_move.setEnabled(!auto_playing);
			break;
		case "next":
			nextMove();
			break;
			
		}
	}
	
	private void nextMove(){
		try{
			Move next = player.calculateBestMove(theGame);
			playMove(next);
		} catch(NoMovesFoundException e) {
			e.printStackTrace();
		}
		
	}
}
