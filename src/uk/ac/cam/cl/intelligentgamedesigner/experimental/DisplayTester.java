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
		/* int N = 10, M = 10;
		Cell[][] values = new Cell[N][M];
		CandyGenerator generator = new CandyGenerator(null);
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < M; ++j) {
				values[i][j] = new Cell(null, generator.getCandy());
			}
		} */
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
		
		app.add(generalPanel);
		app.setSize(new Dimension(500, 600));
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
