package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Comparator;

public class BinaryBoardComparator implements Comparator<BaseBinaryBoard> {

	@Override
	public int compare(BaseBinaryBoard arg0, BaseBinaryBoard arg1) {
		return arg0.getCount() - arg1.getCount(); //???
	}
	
}
