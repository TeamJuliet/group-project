package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public abstract class BaseBinaryBoard extends InfiniteBoard<Boolean> {

	public BaseBinaryBoard(int width, int height, boolean defaultValue) {
		super(width, height, defaultValue);
	}
	
	@Override
	protected String getChar(int i, int j) {
		if (get(i, j))
		{
			return "1";
		}else{
			return "0";
		}
	}
	
	public abstract int getCount();
}
