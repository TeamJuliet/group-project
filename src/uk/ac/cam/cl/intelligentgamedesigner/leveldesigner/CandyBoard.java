package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class CandyBoard extends BaseBinaryBoard {

	public CandyBoard(DesignBoard board)
	{
		super(board.width,board.height,false);
		
		this.board = board;
	}

	@Override
	public int width() {
		return board.width;
	}

	@Override
	public int height() {
		return board.height;
	}

	@Override
	protected Boolean validGet(int i, int j) {
		return (board.get(i, j).getDesignCellType() == DesignCellType.EMPTY);
	}
	
	@Override
	protected void validSet(int i, int j, Boolean obj) {
		//This should never be called (Otherwise there is an error in the code)
		assert(false);
		
	}

	@Override
	protected int getConvolutionValue(int i, int j) {
		return get(i, j) ? 1 : 0;
	}
	
	private DesignBoard board;
}
