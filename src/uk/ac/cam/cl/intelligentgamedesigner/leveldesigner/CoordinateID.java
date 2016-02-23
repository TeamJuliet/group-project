package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class CoordinateID extends EntityID {
	public CoordinateID(int xPosition, int yPosition, ArrayList<Double> statistics) {
		super(statistics);
		
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	@Override
	public boolean util_equals(EntityID statID) {
		return (xPosition == ((CoordinateID)statID).xPosition) && ((yPosition == ((CoordinateID)statID).yPosition));
	}
	
	private int xPosition, yPosition;

	@Override
	public int getHash() {
		if(xPosition > yPosition)
		{
			return (xPosition * xPosition) + yPosition;
		}else{
			return (yPosition * yPosition) + ((2 * yPosition) - xPosition);
		}
	}
}
