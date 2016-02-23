package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public abstract class DoubleFunction {
	DoubleFunction(double minX, double maxX, double minDefaultY, double maxDefaultY)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minDefaultY = minDefaultY;
		this.maxDefaultY = maxDefaultY;
	}
	
	
	public abstract double getInternalValue(double x);
	
	public double get(double x) {
		if(x < minX) {
			return minDefaultY;
		}else if(x > maxX)
		{
			return maxDefaultY;
		}else{
			return getInternalValue(x / (maxX - minX));
		}
	}
	
	protected double minX, maxX;
	protected double minDefaultY, maxDefaultY;
}
