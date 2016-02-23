package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class LineID extends EntityID {
	public LineID(int position, ArrayList<Double> statistics) {
		super(statistics);
		
		this.position = position;
	}

	@Override
	public boolean util_equals(EntityID statID) {
		return (position == ((LineID)statID).position);
	}
	
	@Override
	public int getHash() {
		return position;
	}
	
	private int position;
}
