package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class CandyBoard extends BaseBinaryBoard {

	public CandyBoard(DesignBoard board)
	{
		super(board.width,board.height,false);
		
		this.board = board;
	}

	@Override
	public final int width() {
		return board.width;
	}

	@Override
	public final int height() {
		return board.height;
	}

	@Override
	protected Boolean validGet(int i, int j) {
		return (board.get(i, j).getDesignCellType() == DesignCellType.EMPTY);
	}
	
	@Override
	protected final void validStatSet(int i, int j, Boolean obj) {		
		//This should never be called (Otherwise there is an error in the code)
		assert(false);
		
	}
	
	public DesignCellType getCellType(int i, int j) {
		if(validCoordinate(i,j)) {
			return board.get(i, j).getDesignCellType();
		}else{
			return DesignCellType.UNUSABLE;
		}
	}

	@Override
	protected final int getConvolutionValue(int i, int j, int si, int sj) {
		return get(i, j) ? 1 : 0;
	}

	@Override
	public final int getCount() {
		return 0;
	}
	
	protected DesignBoard board;
}
