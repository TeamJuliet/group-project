package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Dimension;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

public class DisplayTester {	
	
	public static Design getSampleDesign() {
		Design design = new Design();
		Cell[][] boardLayout = new Cell[10][10];
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				boardLayout[i][j] = new Cell(CellType.EMPTY);
			}
		}
		// Add level two jelly to top right box.
		for (int i = 0; i < 3; ++i) {
			for (int j = 8; j < 10; ++j) {
				boardLayout[j][i].setJellyLevel(2);
			}
		}
		// Add level one jelly around that.
		for (int i = 0; i < 3; ++i) {
			boardLayout[7][i].setJellyLevel(1);
		}
		for (int j = 7; j < 10; ++j) {
			boardLayout[j][3].setJellyLevel(1);
		}
		// Set the top left corner to unused cell type.
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				boardLayout[i][j].setCellType(CellType.UNUSABLE);
			}
		}
		// Set the cells in the middle to be locked.
		for (int i = 4; i < 7; ++i) {
			for (int j = 4; j < 7; ++j) {
				boardLayout[i][j].setCellType(CellType.LIQUORICE);
			}
		}
		// Add icing to the cells at the bottom right corner.
		for (int i = 8; i < 10; ++i) {
			for (int j = 8; j < 10; ++j) {
				boardLayout[i][j].setCellType(CellType.ICING);
			}
		}
		design.setBoard(boardLayout);
		return design;
	}
	
	public static void main(String[] args) {
		JPanel generalPanel = new JPanel();
		JFrame app = new JFrame();
		GameState game = new GameState(getSampleDesign());
		// game.changeCandyGenerator(new UnmoveableCandyGenerator(null));
		CellChooser.game = game;
		GameDisplay gamePanel = new GameDisplay(game.getWidth(), game.getHeight(), 50);
		CellChooser.display = gamePanel;
		
		gamePanel.setBoard(game.getBoard());
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

		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Score Panel"));
		JLabel scoreLabel = new JLabel("Score: " + game.getScore());
		scoreLabel.setPreferredSize(new Dimension(100, 22));
		scorePanel.add(scoreLabel);
		generalPanel.add(scorePanel);

		CellChooser.scoreLabel = scoreLabel;
		
		app.add(generalPanel);
		app.setSize(new Dimension(600, 700));
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
