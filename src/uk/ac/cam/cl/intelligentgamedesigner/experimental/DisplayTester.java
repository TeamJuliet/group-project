package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Dimension;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;

public class DisplayTester {	
	
	public static Design getSampleDesign() {
		int sizeX = 20, sizeY = 20;
		Design design = new Design();
		Cell[][] boardLayout = new Cell[sizeX][sizeY];
		for (int i = 0; i < sizeX; ++i) {
			for (int j = 0; j < sizeY; ++j) {
				if (j == sizeY - 1) 
					boardLayout[i][j] = new Cell(CellType.EMPTY, null, 0, true);
				else boardLayout[i][j] = new Cell(CellType.EMPTY);
				
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
        design.setRules(GameMode.INGREDIENTS, 10, 5, 6);
		return design;
	}

	// This is for seeing the board do a shuffle (after making the only possible first move)
	public static Design getSampleDesign2() {
		Design design = new Design();
		int dimension = 4;
		Cell[][] boardLayout = new Cell[dimension][dimension];

		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				boardLayout[x][y] = new Cell(CellType.EMPTY);
			}
		}
		boardLayout[0][0] = new Cell(CellType.UNUSABLE);
		// Check jelly layers aren't moved by the shuffle
		boardLayout[1][0].setJellyLevel(1);
		boardLayout[2][0].setJellyLevel(1);

		// Check liquorice locks aren't moved by the shuffle
		boardLayout[2][3] = new Cell(CellType.LIQUORICE);
		boardLayout[3][3] = new Cell(CellType.LIQUORICE);

		design.setBoard(boardLayout);
		design.setRules(GameMode.HIGHSCORE, 10, 5, 6);
		return design;
	}
	
	public static void main(String[] args) {
		JPanel generalPanel = new JPanel();
		JFrame app = new JFrame();
		GameState game = new GameState(getSampleDesign());
		// game.changeCandyGenerator(new UnmoveableCandyGenerator(null));
		CellChooser.game = game;
		GameDisplay gamePanel = new GameDisplay(game.width, game.height, 50);
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
		JLabel scoreLabel = new JLabel("Score: " + game.getGameProgress().score);
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
