package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ConnectedArea extends BinaryBoard {
	
	public ConnectedArea(int width, int height) {
		super(width, height);
		
		totalCount = 0;
		averageX = averageY = -1;
	}

	public void addConnected(int x, int y) {
		set(x,y,true);
	}

	@Override
	protected void validSet(int i, int j, Boolean obj) {
		
		if(board[i][j] && !obj)
		{
			updateAverageCoord(i,j,true);
			totalCount--;
		}else if(!board[i][j] && obj)
		{
			updateAverageCoord(i,j,false);
			totalCount++;
		}

		board[i][j] = obj;
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
			
			System.out.println("\n");
		}
	
	}
	
	/*
	public int getLongestLine() {
		return 0;
	}
	public int getlongestPossibleMatch() {
		return 0;
	}
	public boolean isConnectedTo() {
		return false;
	}
	public boolean matchPossibleIncluding(int x, int y) {
		return false;
	}
	public int maxWidth() {
		return 0;
	}
	public int maxHeight() {
		return 0;
	}
	public int highestX() {
		return 0;
	}
	public int lowestX() {
		return 0;
	}
	public int highestY() {
		return 0;
	}
	public int lowestY() {
		return 0;
	}
	
	private boolean checkPossibleMatch () {
		return false;
	}*/

	
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
    					areas.add(new ConnectedArea(board.width(),board.height()));
    					minEquivalences.add(new Label(currentLabel));
    					labelBoard.set(i, j, currentLabel);
    					currentLabel.next();
    				}else{
    					Label minLabel = queue.peek().get();
    					labelBoard.set(i, j, minLabel);
    					for(Neighbour neighbour = queue.remove(); !queue.isEmpty(); neighbour = queue.remove())
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
    	
    	return areas;
	}
	
	private int totalCount;
	private int averageX, averageY;
	private int maxWidth;
	private int maxHeight;
	private
	
}
