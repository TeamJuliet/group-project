package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class LabelBoard extends InfiniteBoard<Label> {
	
	public LabelBoard(int width, int height)
	{
		super(width,height,null);
		
		labelBoard = new Label[width][];
		
		for(int i = 0; i < labelBoard.length; i++)
		{
			labelBoard[i] = new Label[height];
			for(int j = 0; j < labelBoard[i].length; j++)
			{
				labelBoard[i][j] = null;
			}
		}	
	}
	
	public boolean isSet(int x, int y) {
		return (labelBoard[x][y] != null);
	}
	
	@Override
	protected Label validGet(int i, int j) {
		return new Label(labelBoard[i][j]);
	}

	@Override
	protected void validSet(int i, int j, Label obj) {
		labelBoard[i][j] = new Label(obj);
	}
	
	@Override
	protected String getChar(int i, int j) {
		return (new Integer(get(i, j).value())).toString();
	}
	
	@Override
	protected int getConvolutionValue(int i, int j, int si, int sj) {
		return 0;
	}
	
	private Label[][] labelBoard;
}
