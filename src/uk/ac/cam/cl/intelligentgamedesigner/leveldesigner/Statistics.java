package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class Statistics {
	public Statistics(int maxEntities, int maxStatistics) {
		this.maxStatistics = maxStatistics;
		this.maxEntities = maxEntities;
		
		currentStats = 0;
		
		maxStat = new ArrayList<Double>(maxStatistics);
		minStat = new ArrayList<Double>(maxStatistics);
		averStat = new ArrayList<Double>(maxStatistics);
		averSqStat = new ArrayList<Double>(maxStatistics);
		
		for(int i = 0; i < maxStatistics; i++) {
			maxStat.add(-Double.MAX_VALUE);
			minStat.add(Double.MAX_VALUE);
			averStat.add(0.0);
			averSqStat.add(0.0);
		}
		
		statList = new ArrayList<EntityID>(maxEntities);
		
		for(int i = 0; i < maxEntities; i++) {
			statList.add(null);
		}
	}
	
	public void addStatistic(EntityID statID) {
		ArrayList<Double> pStat = statID.get();
		
		if(isUpdate(statID)) {
			ArrayList<Double> org_pStat = getStat(statID);
			
			for(int stat = 0; stat < maxStatistics; stat++)
			{
				setValue(stat, averStat, ( ( getAverage(stat) * statCount() ) -getValue(stat,org_pStat)	+getValue(stat,pStat) ) / statCount());
				setValue(stat, averSqStat, ((getValue(stat,averSqStat) * statCount()) - Math.pow(getValue(stat,org_pStat),2) + Math.pow(getValue(stat,pStat),2))/statCount());
			}
		}else{
			for(int stat = 0; stat < maxStatistics; stat++)
			{
				averStat.set(stat, ( ( getAverage(stat) * ( statCount() ) ) +getValue(stat,pStat) ) / ( statCount() + 1 ) );
				averSqStat.set(stat, ( ( getValue(stat,averSqStat) * ( statCount() ) ) +Math.pow(getValue(stat,pStat),2) ) / ( statCount() + 1 ) );
			}
			
			currentStats++;
		}
		
		statList.set(statID.getHash(), statID);
		
		for(int stat = 0; stat < maxStatistics; stat++)
		{
			if(getMin(stat) > getValue(stat,pStat)) {
				setValue(stat, minStat, getValue(stat,pStat));
			}
			if(getMax(stat) < getValue(stat,pStat)) {
				setValue(stat, maxStat, getValue(stat,pStat));
			}
		}
		
	}
	
	public double getMin(int stat) {
		return getValue(stat,minStat);
	}
	
	public double getMax(int stat) {
		return getValue(stat,maxStat);
	}
	
	public double getAverage(int stat) {
		return getValue(stat,averStat);
	}
	
	public double getVariance(int stat) {
		return getValue(stat,averSqStat) - Math.pow(getValue(stat,averStat),2);
	}
	
	protected boolean isUpdate(EntityID statID) {
		EntityID e = statList.get(statID.getHash());
		
		if(e == null)
		{
			return false;
		}else{
			return e.equals(statID);
		}
	}
	
	protected ArrayList<Double> getStat(EntityID statID) {
		EntityID stat = statList.get(statID.getHash());
		
		if(stat != null) {
			return stat.get(); 
		}
		return null;
		
	}
	
	protected int statCount() {
		return currentStats;
	}
	
	private static double getValue(Double v) {
		if(v == null) {
			return 0.0;
		}else{
			return v;
		}
	}
	
	private static double getValue(int stat, ArrayList<Double> stats) {
		if(stat >= stats.size()) {
			return 0.0;
		}else{
			return getValue(stats.get(stat));
		}
	}
	
	private static void setValue(int stat, ArrayList<Double> stats, double value) {
		if(stat < stats.size()) {
			stats.set(stat, value);
		}
	}
	
	ArrayList<Double> maxStat;
	ArrayList<Double> minStat;
	ArrayList<Double> averStat;
	ArrayList<Double> averSqStat;
	
	ArrayList<EntityID> statList;
	
	int currentStats;
	int maxStatistics;
	int maxEntities;
}
