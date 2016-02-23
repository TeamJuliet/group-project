package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public abstract class EntityID {
	public EntityID(ArrayList<Double> statistic) {
		this.statistic = statistic;
	}
	
	public void set(ArrayList<Double> stat) {
		statistic = stat;
	}
	
	public void set(int stat, double value) {
		statistic.set(stat,value);
	}
	
	public double get(int stat) {
		return statistic.get(stat);
	}
	
	public ArrayList<Double> get() {
		return statistic;
	}

	public abstract boolean util_equals(EntityID statID);
	
	//Hash unique and between 0 and maxStatistics
	public abstract int getHash();
	
	@Override
	public boolean equals(Object o) {
		return util_equals((EntityID)o);
	}
	
	protected ArrayList<Double> statistic;
}
