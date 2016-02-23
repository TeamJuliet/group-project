package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Comparator;

public class BoardComparator<T> implements Comparator<InfiniteBoard<T>> {

	@Override
	public int compare(InfiniteBoard<T> o1, InfiniteBoard<T> o2) {
		return o1.getCount() - o2.getCount(); //???
	}
	
}
