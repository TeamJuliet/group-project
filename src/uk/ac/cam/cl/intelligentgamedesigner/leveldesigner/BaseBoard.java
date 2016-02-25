package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public interface BaseBoard<T> {
	public int width();
	public int height();
	public T get(int i, int j);
	public void set(int i, int j, T obj);
	public IntegerBoard convolutionFilter(IntegerBoard board, ConvolutionStrategy<T> strategy);
}
