package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.PriorityQueue;

public class FourConnectivity implements ConnectivityStrategy {

	private void addNeighbour(PriorityQueue<Neighbour> queue, BaseBinaryBoard board, LabelBoard labelBoard, int nx, int ny)
	{
		if(board.get(nx, ny) && labelBoard.isSet(nx, ny))
		{
			queue.add(new Neighbour(nx, ny, labelBoard.get(nx, ny)));
		}
	}
	
	@Override
	public PriorityQueue<Neighbour> getConnected(int x, int y, BaseBinaryBoard board, LabelBoard labelBoard) {
		
		PriorityQueue<Neighbour> queue = new PriorityQueue<Neighbour>();
		
		addNeighbour(queue, board, labelBoard, x + 1,	y);
		addNeighbour(queue, board, labelBoard, x - 1,	y);
		addNeighbour(queue, board, labelBoard, x,		y + 1);
		addNeighbour(queue, board, labelBoard, x,		y - 1);
		
		return queue;
	}
	
}
