package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class IntegerBoard extends InfiniteBoard<Integer>{

	public IntegerBoard(int width, int height) {
		super(width, height, 0);
		
		board = new int[width][];
		
		for(int i = 0; i < width; i++)
		{
			board[i] = new int[height];
		}
	}

	@Override
	protected String getChar(int i, int j) {
		return (new Integer(board[i][j])).toString();
	}

	@Override
	protected Integer validGet(int i, int j) {
		return board[i][j];
	}

	@Override
	protected void validSet(int i, int j, Integer obj) {
		board[i][j] = obj;
	}
	
	public BinaryBoard binaryThreshold(int threshold) {
		BinaryBoard board = new BinaryBoard(width,height);
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				if(get(i,j) >= threshold)
				{
					board.set(i, j, true);
				}
			}
		}
		
		return board;
	}
	
	@Override
	protected int getConvolutionValue(int i, int j) {
		return get(i, j);
	}
	
	private int[][] board;
}
