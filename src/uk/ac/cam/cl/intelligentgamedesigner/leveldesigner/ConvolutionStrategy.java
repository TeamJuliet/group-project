package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class ConvolutionStrategy<T> {
	public int getConvolutionIndividual(int i, int j, IntegerBoard board, InfiniteBoard<T> infBoard) {
		int value = 0;
		
		for(int k = 0; k < board.width(); k++)
		{
			for(int l = 0; l < board.height(); l++)
			{
				int cv = infBoard.getConvolutionValue(i + k - (board.width() / 2), j + l - (board.height / 2), i, j);
				
				if(cv != 0)
				{
					value += cv * board.get(k, l);
				}
			}
		}
		
		return value;
	}
}
