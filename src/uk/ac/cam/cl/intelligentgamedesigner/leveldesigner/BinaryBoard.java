package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class BinaryBoard extends BaseBinaryBoard {

	public BinaryBoard(int width, int height) {
		super(width, height, false);
		
		board = new boolean[width][];
		
		for(int i = 0; i < width; i++)
		{
			board[i] = new boolean[height];
		}
	}

	@Override
	protected Boolean validGet(int i, int j) {
		return board[i][j];
	}

	@Override
	protected void validSet(int i, int j, Boolean obj) {
		board[i][j] = obj;
	}

	boolean[][] board;

	@Override
	protected int getConvolutionValue(int i, int j) {
		return get(i, j) ? 1 : 0;
	}
}
