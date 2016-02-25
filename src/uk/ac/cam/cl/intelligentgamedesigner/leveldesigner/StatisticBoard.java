package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public abstract class StatisticBoard<T> extends InfiniteBoard<T> {

	private Statistics verticalLineCellStats[];
	private Statistics horizontalLineCellStats[];
	private Statistics cellStats;
	
	public StatisticBoard(int width, int height, T defaultValue) {
		super(width, height, defaultValue);
		
		verticalLineCellStats = new Statistics[width];
		for(int i = 0; i < width; i++) {
			verticalLineCellStats[i] = new Statistics(height,1);
		}
		horizontalLineCellStats = new Statistics[height];
		for(int i = 0; i < height; i++) {
			horizontalLineCellStats[i] = new Statistics(width,1);
		}
		cellStats = new Statistics(width*height,2);
	}
	
	public int getHorizontalExtent() {
		return (int) (cellStats.getMax(0) - cellStats.getMin(0));
	}
	
	public int getVerticalExtent() {
		return (int) (cellStats.getMax(1) - cellStats.getMin(1));
	}
	
	private void updateStatistics(int i, int j) {
		
		ArrayList<Double> verticalLineStatArray = new ArrayList<Double>(1);
		verticalLineStatArray.add((double) j);
		verticalLineCellStats[i].addStatistic(new LineID(j,verticalLineStatArray));
		
		ArrayList<Double> horizontalLineStatArray = new ArrayList<Double>(1);
		horizontalLineStatArray.add((double) i);
		horizontalLineCellStats[j].addStatistic(new LineID(i,horizontalLineStatArray));
		
		ArrayList<Double> cellStatArray = new ArrayList<Double>();
		cellStatArray.add((double) i);
		cellStatArray.add((double) j);
		cellStats.addStatistic(new CoordinateID(i,j,cellStatArray));	
	}

	@Override
	protected final void validSet(int i, int j, T obj) {
		validStatSet(i, j, obj);
		updateStatistics(i, j);
	}
	
	protected abstract void validStatSet(int i, int j, T obj);

}
