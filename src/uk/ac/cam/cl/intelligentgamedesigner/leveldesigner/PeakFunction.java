package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class PeakFunction extends DoubleFunction {

	double peakPoint;
	double peakValue;
	
	PeakFunction(double peakPoint, double peakValue, double minX, double maxX, double minDefaultY, double maxDefaultY) {
		super(minX, maxX, minDefaultY, maxDefaultY);
		
		this.peakPoint = (peakPoint) / (maxX - minX);
		this.peakValue = peakValue;
		
		
	}

	@Override
	public double getInternalValue(double x) {
		if(x < peakPoint) {
			return minDefaultY + (x/peakPoint)*(peakValue - minDefaultY);
		}else{
			return maxDefaultY + ((1 - x) / (1 - peakPoint))*(peakValue - maxDefaultY);
		}
	}

}
