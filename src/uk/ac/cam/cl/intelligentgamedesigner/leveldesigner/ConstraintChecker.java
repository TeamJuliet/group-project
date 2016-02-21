package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

public class ConstraintChecker {

	public static boolean checkHorizontalSpace(DesignBoard board)
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
	{/*
		for(int i = 0; i < board.width; i++)
		{
			for(int j = 0; j < board.height; j++)
			{
				
			}
		}*/
		
		return 0;
	}
	
	public static double 
	
    public static double calculateFitness(DesignBoard board) {
    	CandyBoard bBoard = new CandyBoard(board);
    	
    	ArrayList<ConnectedArea> list = ConnectedArea.getAreas(bBoard, new FourConnectivity());
    	
    	
    	
    	return 0.0f;
    }
}
