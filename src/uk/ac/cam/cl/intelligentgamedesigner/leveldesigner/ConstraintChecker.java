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

	/**
	 * This function, for each column of the board, counts how far down candies can be dropped, and reduces the
	 * fitness for levels in which the candies are constantly blocked from falling in.
	 *
	 * @param board		The level board to inspect
	 * @return
     */
	public static double calculateDropFitness (DesignBoard board) {

		// Calculate actual height of board (we might have some rows with entirely UNUSABLE cells
		int yLower = 0;
		boolean nonUnusableFound = false;
		while (yLower < board.height && !nonUnusableFound) {
			nonUnusableFound = false;
			for (int x = 0; x < board.width; x++) {
				if (board.get(x, yLower).getDesignCellType() != DesignCellType.UNUSABLE) nonUnusableFound = true;
			}
			if (!nonUnusableFound) yLower++;
		}

		int yUpper = board.height;
		while (yUpper > 0 && !nonUnusableFound) {
			nonUnusableFound = false;
			for (int x = 0; x < board.width; x++) {
				if (board.get(x, yUpper - 1).getDesignCellType() != DesignCellType.UNUSABLE) nonUnusableFound = true;
			}
			if (!nonUnusableFound) yUpper--;
		}

		int actualHeight = yUpper - yLower;

		double totalDropProportion = 0;
		int actualWidth = 0;
		for (int x = 0; x < board.width; x++) {
			int y = 0;
			nonUnusableFound = false;
			while (y < board.height) {
				DesignCellType cellType = board.get(x, y).getDesignCellType();
				if (cellType != DesignCellType.UNUSABLE) nonUnusableFound = true;
				if (cellType == DesignCellType.ICING || cellType == DesignCellType.LIQUORICE) {
					break;
				} else {
					y++;
				}
			}
			if (nonUnusableFound) {
				totalDropProportion += (y / (double) actualHeight);
				actualWidth++;
				System.out.println(x + ": " + y);
			}
		}

		System.out.println(actualHeight + ", " + actualWidth);

		return (totalDropProportion / (double) actualWidth);
	}

	public static boolean hasFiveByFiveArea (DesignBoard board) {
		for (int x = 0; x < board.width - 5; x++) {
			for (int y = 0; y < board.height - 5; y++) {

				// Check for 5x5 area of empty cells
				boolean stillAllEmpty = true;
				for (int x2 = x; x2 < x + 5 && stillAllEmpty; x2++) {
					for (int y2 = y; y2 < y + 5 && stillAllEmpty; y2++) {
						if (board.get(x, y).getDesignCellType() != DesignCellType.EMPTY) stillAllEmpty = false;
					}
				}

				if (stillAllEmpty) return true;
			}
		}

		return false;
	}
	
    public static double calculateFitness(DesignBoard board) {

		CandyBoard bBoard = new CandyBoard(board);

		double factor = 1.0;

		// Reduce score if there are small isolated regions
		ArrayList<ConnectedArea> list = ConnectedArea.getAreas(bBoard, new FourConnectivity());
		for (ConnectedArea area : list) {
			if (area.getCount() < 5) factor *= 0.2;
		}

		// Reduce score if liquorice / icing are blocking the top too much
		factor *= calculateDropFitness(board);

		// Reduce score if the board doesn't have a minimum 5x5 empty cell area
		if (!hasFiveByFiveArea(board)) factor *= 0.1;

		return factor;

//    	CandyBoard bBoard = new CandyBoard(board);
//
//    	double factor = 1.0;
//
//    	long time = ( System.currentTimeMillis());
//
//    	ArrayList<ConnectedArea> list = ConnectedArea.getAreas(bBoard, new FourConnectivity());
//
//    	System.out.println("Time: " + (System.currentTimeMillis() - time));
//
//    	PeakFunction pFunCount = new PeakFunction(3, 1, 1, 8, 1, 0.2);
//    	factor *= pFunCount.get((double) list.size()); //
//
//    	PeakFunction pFunConCount = new PeakFunction(7, 1, 5, 9, 0.8, 0.8);
//
//    	factor *= pFunConCount.get(bBoard.getHorizontalExtent()); //
//    	factor *= pFunConCount.get(bBoard.getVerticalExtent()); //
//
//    	PeakFunction pFunConSizeMain = new PeakFunction(50, 1, 18, 81, 0.2, 0.8);
//    	PeakFunction pFunConSize = new PeakFunction(20, 1, 6, 25, 0.2, 0.8);
//
//    	if(list.size() > 0)
//    	{
//
//	    	list.sort(new BinaryBoardComparator());
//
//	    	factor *= pFunConSizeMain.get(list.get(0).getCount()); //
//
//	    	for(int i = 1; i < list.size(); i++) {
//	    		factor *= pFunConSize.get(list.get(i).getCount()); //
//	    	}
//
//    	}
//
//    	return factor;
    }
}
