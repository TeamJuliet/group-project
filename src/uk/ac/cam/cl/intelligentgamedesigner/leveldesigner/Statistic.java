package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public abstract class Statistic {
	public Statistic() {
		
		maxStat = 0.0;
		minStat = 0.0;
		averStat = 0.0;
		averSqStat = 0.0;
		
		//max
		//min
		//average
		//range
		//standard deviation
		//ID each statistic to identify updates and adds
	}
	
	public void addStatistic(StatisticID statID, double stat) {
		double org_pStat = getStat(statID);
		double pStat = processStat(statID,stat);
		
		if(minStat > pStat) {
			minStat = pStat;
		}
		if(maxStat < pStat) {
			maxStat = pStat;
		}
		
		if(isUpdate(statID,stat)) {
			averStat = (averStat * statCount()) - org_pStat + pStat;
			averSqStat = (averSqStat * statCount()) + (pStat*pStat);
		}else{
			averStat = (averStat * (statCount() - 1)) + pStat;
			averSqStat = (averSqStat * (statCount() - 1)) + (pStat*pStat); 
		}
	}
	
	public double getMin() {
		return minStat;
	}
	
	public double getMax() {
		return maxStat;
	}
	
	public double getAverage() {
		return averStat;
	}
	
	public double getVariance() {
		return averSqStat - (averStat*averStat);
	}
	
	protected abstract boolean isUpdate(StatisticID statID, double stat);
	protected abstract double processStat(StatisticID statID, double stat);
	protected abstract double getStat(StatisticID statID);
	protected abstract int statCount();
	
	double maxStat;
	double minStat;
	double averStat;
	double averSqStat;
}
