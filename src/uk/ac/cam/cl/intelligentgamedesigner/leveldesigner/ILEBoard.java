package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class ILEBoard extends CandyBoard {
	
	public ILEBoard(DesignBoard board) {
		super(board);
	}

	@Override
	protected Boolean validGet(int i, int j) {
		return (board.get(i, j).getDesignCellType() != DesignCellType.UNUSABLE);
	}
	
	

}