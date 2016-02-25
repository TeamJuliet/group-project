package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ConnectedArea extends BinaryBoard {
	
	public ConnectedArea(int width, int height) {
		super(width, height);
	}

	public void addConnected(int x, int y) {
		set(x,y,true);
	}
	    
	public static void main(String [] args)
	{
		BinaryBoard board = new BinaryBoard(9,9);

		for(int i = 0; i < board.width(); i++)
		{
			for(int j = 0; j < board.height(); j++)
			{
				if(Math.random() < 0.4)
				{
					board.set(i, j, true);
				}
			}
		}
		
		board.print();
		
		System.out.println('\n');
		
		FourConnectivity fourConnect = new FourConnectivity();
		
		ArrayList<ConnectedArea> areas = ConnectedArea.getAreas(board, fourConnect);
		
		for(ConnectedArea area : areas)
		{
			area.print();
			//System.out.println(area.cellStats.getMin(0) + " <-> " + area.cellStats.getMax(0) + " : " + area.cellStats.getAverage(0) + " : " + area.cellStats.getVariance(0));
			//System.out.println(area.cellStats.getMin(1) + " <-> " + area.cellStats.getMax(1) + " : " + area.cellStats.getAverage(1) + " : " + area.cellStats.getVariance(1));
			System.out.println("\n");
		}
	
	}
	
	public static ArrayList<ConnectedArea> getAreas(BaseBinaryBoard board, ConnectivityStrategy connectivity)
	{
		LabelBoard labelBoard = new LabelBoard(board.width(),board.height());
		
		Label currentLabel = new Label();
		
    	ArrayList<Label> minEquivalences = new ArrayList<Label>();
    	ArrayList<ConnectedArea> areas = new ArrayList<ConnectedArea>();

    	for(int i = 0; i < board.width(); i++)
    	{
    		for(int j = 0; j < board.height(); j++)
    		{
    			if(board.get(i, j).booleanValue())
    			{
    				PriorityQueue<Neighbour> queue = connectivity.getConnected(i, j, board, labelBoard);
    				
    				if(queue.isEmpty())
    				{
    					areas.add(new ConnectedArea(board.width(),board.height())); //???
    					minEquivalences.add(new Label(currentLabel));
    					labelBoard.set(i, j, currentLabel);
    					currentLabel.next();
    				}else{
    					Label minLabel = queue.peek().get();
    					labelBoard.set(i, j, minLabel);
    					for(Neighbour neighbour : queue)
    					{
    						if(minEquivalences.get(neighbour.get().value()).compareTo(minLabel) > 0)
    						{
    							minEquivalences.set(neighbour.get().value(),new Label(minLabel));
    						}
    					}
    				}
    			}
    		}
    	}

    	for(int i = 0; i < board.width(); i++)
    	{
    		for(int j = 0; j < board.height(); j++)
    		{
    			if(labelBoard.isSet(i, j))
    			{
    				labelBoard.set(i, j, new Label(minEquivalences.get(labelBoard.get(i, j).value())));
    			}
    		}
    	}
    	
    	for(int i = 0; i < board.width(); i++)
    	{
    		for(int j = 0; j < board.height(); j++)
    		{
    			if(labelBoard.isSet(i, j))
    			{
    				areas.get(labelBoard.get(i, j).value()).addConnected(i, j);
    			}
    		}
    	}
    	
    	for(int i = 0; i < areas.size(); i++)
    	{
    		if(areas.get(i).getCount() == 0) {
    			areas.remove(i);
    		}
    	}
    	
    	return areas;
	}
}
