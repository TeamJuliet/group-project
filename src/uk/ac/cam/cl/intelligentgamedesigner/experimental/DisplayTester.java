package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Dimension;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.CandyGenerator;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

public class DisplayTester {
	
	
	public static void main(String[] args) {
		JPanel generalPanel = new JPanel();
		JFrame app = new JFrame();
		GameState game = new GameState(new Design());
		CellChooser.game = game;
		GameDisplay gamePanel = new GameDisplay(game.getWidth(), game.getHeight(), 50);
		CellChooser.display = gamePanel;
		
		gamePanel.setBoard(game.board);
		generalPanel.add(gamePanel);
		
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Save Panel"));
		TextField text = new TextField();
		text.setColumns(20);
		controlPanel.add(text);
		JButton saveButton = new JButton("Save");
		controlPanel.add(saveButton);
		generalPanel.add(controlPanel);
		
		JPanel trackPanel = new JPanel();
		trackPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Track Panel"));
		TextField moves = new TextField();
		moves.setColumns(40);
		trackPanel.add(moves);
		generalPanel.add(trackPanel);
		
		CellChooser.movesRecord = moves;
		
		app.add(generalPanel);
		app.setSize(new Dimension(600, 700));
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
