package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class ConstraintChecker {

	public static boolean checkHorizontalSpace(RandomBoard<DesignCellType> board)
	{

        for(int i = 0; i < board.height; i++)
        {
        	int lenght = 0;
        	
        	for(int j = 0; j < board.width; j++)
        	{
        		if(board.get(j,i) == DesignCellType.EMPTY)
        		{
        			lenght++;
        		}else{
        			lenght = 0;
        		}
        	}
        	
        	if(lenght > 3)
        	{
        		return true;
        	}
        }
        
        return false;
	}
	
	public static boolean checkVerticalSpace(RandomBoard<DesignCellType> board)
	{

        for(int i = 0; i < board.width; i++)
        {
        	int lenght = 0;
        	
        	for(int j = 0; j < board.height; j++)
        	{
        		if(board.get(i,j) == DesignCellType.EMPTY)
        		{
        			lenght++;
        		}else{
        			lenght = 0;
        		}
        	}
        	
        	if(lenght > 3)
        	{
        		return true;
        	}
        }
        
        return false;
	}
	
	public static int checkConnected(int i, int j, RandomBoard<DesignCellType> board)
	{/*
		for(int i = 0; i < board.width; i++)
		{
			for(int j = 0; j < board.height; j++)
			{
				
			}
		}*/
		
		return 0;
	}
	
    public static double calculateFitness(RandomBoard<DesignCellType> board) {
     
    	if(checkVerticalSpace(board) && checkHorizontalSpace(board))
    	{
    		return 1.0f;
    	}
    	return 0.0f;
    }
}
