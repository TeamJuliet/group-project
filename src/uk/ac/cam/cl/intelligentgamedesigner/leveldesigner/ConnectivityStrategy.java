package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.PriorityQueue;

public interface ConnectivityStrategy {	
	public PriorityQueue<Neighbour> getConnected(int x, int y, BaseBinaryBoard board, LabelBoard labelBoard);
}
