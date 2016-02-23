package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class BinaryBoard extends BaseBinaryBoard {
	private int count;
	
	public BinaryBoard(int width, int height) {
		super(width, height, false);
		
		board = new boolean[width][];
		
		for(int i = 0; i < width; i++)
		{
			board[i] = new boolean[height];
		}
		
		count = 0;
	}
	
	@Override
	public int getCount() {
		return count;
	}

	@Override
	protected Boolean validGet(int i, int j) {
		return board[i][j];
	}

	@Override
	protected void validSet(int i, int j, Boolean obj) {
		if(!board[i][j] && obj)
		{
			count++;
		}else if(board[i][j] && !obj){
			count--;
		}
		
		board[i][j] = obj;
	}

	boolean[][] board;

	@Override
	protected int getConvolutionValue(int i, int j) {
		return get(i, j) ? 1 : 0;
	}
}
