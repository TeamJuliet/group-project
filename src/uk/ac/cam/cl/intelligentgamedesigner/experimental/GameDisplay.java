package uk.ac.cam.cl.intelligentgamedesigner.experimental;

import java.awt.Dimension;

import javax.swing.JPanel;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

public class GameDisplay extends JPanel {
	private int blockSize;
	private int sizeX, sizeY;
	
	private CellDisplay[][] displays;
	public GameDisplay(int N, int M, int blockSize) {
		sizeX = N;
		sizeY = M;
		this.blockSize = blockSize;
		displays = new CellDisplay[sizeX][sizeY];
		setLayout(null);
		setPreferredSize(new Dimension(sizeX * blockSize, sizeY * blockSize));
		for (int i = 0; i < sizeX; ++i) {
			for (int j = 0; j < sizeY; ++j) {
				displays[i][j] = new CellDisplay();
				displays[i][j].setSize(blockSize, blockSize);
				super.add(displays[i][j]);
				displays[i][j].setBounds(blockSize * i, blockSize * j, blockSize-1, blockSize-1);
				displays[i][j].setPosition(new Position(i, j));
				displays[i][j].addMouseListener(new CellChooserAdapter());
				// Choose the ColorChangeAdapter for modifying the board.
			}
		}
	}
	
	public void setBoard(Cell[][] values) {
		for (int i = 0; i < sizeX; ++i) {
			for (int j = 0; j < sizeY; ++j) {
				// TODO: Check indices.
				displays[i][j].setCell(values[i][j]);
			}
		}
	}
	
	
	
	
}
