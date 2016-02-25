package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class ILBoard extends CandyBoard {

	public ILBoard(DesignBoard board) {
		super(board);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean validGet(int i, int j) {
		DesignCellType dct = board.get(i, j).getDesignCellType();
		
		return (dct == DesignCellType.ICING || dct == DesignCellType.LIQUORICE);
	}
}
