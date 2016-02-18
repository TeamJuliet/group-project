package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public abstract class InfiniteBoard<T> implements BaseBoard<T> {

	public InfiniteBoard(int width, int height, T defaultValue) {
		this.width = width;
		this.height = height;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}
	
	protected boolean validCoordinate(int i, int j)
	{
		return (i >= 0) && (i < width) && (j >= 0) && (j < height);
	}
	
	@Override
	public final T get(int i, int j)
	{
		if(!validCoordinate(i, j))
		{
			return defaultValue;
		}
		return validGet(i, j);
	}
	
	@Override
	public final void set(int i, int j, T obj)
	{
		if(validCoordinate(i, j))
		{
			validSet(i, j, obj);
		}
	}
	
	public void print()
	{
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				System.out.print(getChar(i,j));
				System.out.print(' ');
			}
			System.out.print('\n');
		}
	}
	
	protected<S extends InfiniteBoard<T>> void getSubSection(int x_tl, int y_tl, S subSection) {
		for(int i = x_tl; i < x_tl + subSection.width; i++)
		{
			for(int j = y_tl; j < y_tl + subSection.height; j++)
			{
				subSection.set(i - x_tl, j - y_tl, get(i,j));
			}
		}
	}
	
	protected abstract int getConvolutionValue(int i, int j);
	
	@Override
	public final IntegerBoard convolutionFilter(IntegerBoard board) {
		
		assert((board.width() % 2 == 1) && (board.height() % 2 == 1));
		
		IntegerBoard intBoard = new IntegerBoard(width, height);
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				for(int k = 0; k < board.width(); k++)
				{
					for(int l = 0; l < board.height(); l++)
					{
						if(getConvolutionValue(i + k - (board.width() / 2), j + l - (board.height / 2)) != 0)
						{
							intBoard.set(i, j, intBoard.get(i, j) + (getConvolutionValue(i + k - (board.width() / 2), j + l - (board.height / 2)) * board.get(k, l)));
						}
					}
				}
			}
		}
		
		return intBoard;

	}
	
	protected abstract String getChar(int i, int j);
	
	protected abstract T validGet(int i, int j);
	protected abstract void validSet(int i, int j, T obj);
	
	private T defaultValue;
	protected int width, height;
}
