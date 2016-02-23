package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class ConstraintChecker {

	/*public static boolean checkHorizontalSpace(DesignBoard board)
	{

        for(int i = 0; i < board.height; i++)
        {
        	int length = 0;
        	
        	for(int j = 0; j < board.width; j++)
        	{
        		if(board.get(j,i).getDesignCellType() == DesignCellType.EMPTY)
        		{
					length++;
        		}else{
					length = 0;
        		}
        	}
        	
        	if(length > 3)
        	{
        		return true;
        	}
        }
        
        return false;
	}
	
	public static boolean checkVerticalSpace(DesignBoard board)
	{

        for(int i = 0; i < board.width; i++)
        {
        	int length = 0;
        	
        	for(int j = 0; j < board.height; j++)
        	{
        		if(board.get(i,j).getDesignCellType() == DesignCellType.EMPTY)
        		{
					length++;
        		}else{
					length = 0;
        		}
        	}
        	
        	if(length > 3)
        	{
        		return true;
        	}
        }
        
        return false;
	}
	
	public static int checkConnected(int i, int j, DesignBoard board)
	{
		for(int i = 0; i < board.width; i++)
		{
			for(int j = 0; j < board.height; j++)
			{
				
			}
		}
		
		return 0;
	}
	
	public static double*/ 
	
	
    public static double calculateFitness(DesignBoard board) {
    	CandyBoard bBoard = new CandyBoard(board);
    	
    	double factor = 1.0;
    	
    	long time = ( System.currentTimeMillis());
    	
    	ArrayList<ConnectedArea> list = ConnectedArea.getAreas(bBoard, new FourConnectivity());
    	
    	System.out.println("Time: " + (System.currentTimeMillis() - time));
    	
    	PeakFunction pFunCount = new PeakFunction(3, 1, 1, 8, 1, 0.2);
    	factor *= pFunCount.get((double) list.size()); //
    	
    	PeakFunction pFunConCount = new PeakFunction(7, 1, 5, 9, 0.8, 0.8);
    	
    	factor *= pFunConCount.get(bBoard.getHorizontalExtent()); //
    	factor *= pFunConCount.get(bBoard.getVerticalExtent()); //
    	
    	PeakFunction pFunConSizeMain = new PeakFunction(50, 1, 18, 81, 0.2, 0.8);
    	PeakFunction pFunConSize = new PeakFunction(20, 1, 6, 25, 0.2, 0.8);
    	
    	if(list.size() > 0)
    	{
    	
	    	list.sort(new BinaryBoardComparator());
	    	
	    	factor *= pFunConSizeMain.get(list.get(0).getCount()); //
	    	
	    	for(int i = 1; i < list.size(); i++) {
	    		factor *= pFunConSize.get(list.get(i).getCount()); //
	    	}
    	
    	}
    	
    	return factor;
    }
}
