package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.experimental.GameDisplay;

public class DepthPotentialScorePlayerTest {
	public static Design getSampleDesign() {
		int sizeX = 10, sizeY = 10;
		Design design = new Design();
		Cell[][] boardLayout = new Cell[sizeX][sizeY];
		for (int i = 0; i < sizeX; ++i) {
			for (int j = 0; j < sizeY; ++j) {
				boardLayout[i][j] = new Cell(CellType.EMPTY, null, 0, false);

			}
		}
		design.setGameMode(GameMode.HIGHSCORE);
		design.setNumberOfMovesAvailable(20);
		design.setObjectiveTarget(10000000);
		design.setBoard(boardLayout);
		return design;
	}

	public static void playGame(GameState game, GameDisplay display) {
		DepthPotentialScorePlayer player = new DepthPotentialScorePlayer(2, 4); 
		while(!game.isGameOver()) {
			try {
				System.out.println(game.getGameProgress());
				Move move = player.calculateBestMove(game);
				game.makeFullMove(move);
				display.setBoard(game.getBoard());
				display.paintImmediately(0, 0, display.getWidth(), display.getHeight());
				Thread.sleep(5);
			} catch (NoMovesFoundException | InvalidMoveException e) {
				System.err.println("No Moves Found");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("GAME IS OVER" + game.isGameOver());
	}
	
	public static void main(String[] args) {
		GameState game = new GameState(getSampleDesign());
		JPanel generalPanel = new JPanel();
		JFrame app = new JFrame();
		GameDisplay gamePanel = new GameDisplay(game.width, game.height,
				50);
		gamePanel.setBoard(game.getBoard());
		generalPanel.add(gamePanel);
		app.add(generalPanel);
		app.setSize(new Dimension(600, 700));
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playGame(game, gamePanel);
	}
}