package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;

/**
 * A class for checking how playable a level is.
 */
public class ConstraintChecker {	
	
	public static IntegerBoard getPossibleMatches(BaseBinaryBoard board) {
		IntegerBoard threeVertical = new IntegerBoard(1,3); threeVertical.setAll(1);
		IntegerBoard threeHorizontal = new IntegerBoard(3,1); threeHorizontal.setAll(1);
		
		IntegerBoard iBoard = board.convolutionFilter(threeVertical, new ConvolutionStrategy<Boolean>());
		
		IntegerBoard outBoard = new IntegerBoard(iBoard.width(),iBoard.height());
		
		for(int i = 0; i < iBoard.width(); i++) {
			for(int j = 0; j < iBoard.height(); j++) {
				int t = 0;
				
				if(iBoard.get(i, j + 1) == 3) {
					t++;
				}
				if(iBoard.get(i, j - 1) == 3) {
					t++;
				}
				if(iBoard.get(i, j) == 3) {
					t++;
				}
				
				
				outBoard.set(i, j, t);
			}
		}
		
		iBoard = board.convolutionFilter(threeHorizontal, new ConvolutionStrategy<Boolean>());
		
		for(int i = 0; i < iBoard.width(); i++) {
			for(int j = 0; j < iBoard.height(); j++) {
				int t = outBoard.get(i, j);
				
				if(iBoard.get(i + 1, j) == 3) {
					t++;
				}
				if(iBoard.get(i - 1, j) == 3) {
					t++;
				}
				if(iBoard.get(i, j) == 3) {
					t++;
				}
				
				
				outBoard.set(i, j, t);
			}
		}
		
		return outBoard;
	}
	
	public static IntegerBoard getPossibleFiveMatches(BaseBinaryBoard board) {
		IntegerBoard fiveVertical = new IntegerBoard(1,5); fiveVertical.setAll(1);
		IntegerBoard fiveHorizontal = new IntegerBoard(5,1); fiveHorizontal.setAll(1);
		
		IntegerBoard iBoard = board.convolutionFilter(fiveVertical, new ConvolutionStrategy<Boolean>());
		
		IntegerBoard outBoard = new IntegerBoard(iBoard.width(),iBoard.height());
		
		for(int i = 0; i < iBoard.width(); i++) {
			for(int j = 0; j < iBoard.height(); j++) {
				int t = 0;
				
				if(iBoard.get(i, j + 1) == 5) {
					t++;
				}
				if(iBoard.get(i, j - 1) == 5) {
					t++;
				}
				if(iBoard.get(i, j) == 5) {
					t++;
				}
				
				
				outBoard.set(i, j, t);
			}
		}
		
		iBoard = board.convolutionFilter(fiveHorizontal, new ConvolutionStrategy<Boolean>());
		
		for(int i = 0; i < iBoard.width(); i++) {
			for(int j = 0; j < iBoard.height(); j++) {
				int t = outBoard.get(i, j);
				
				if(iBoard.get(i + 1, j) == 5) {
					t++;
				}
				if(iBoard.get(i - 1, j) == 5) {
					t++;
				}
				if(iBoard.get(i, j) == 5) {
					t++;
				}
				
				
				outBoard.set(i, j, t);
			}
		}
		
		return outBoard;
	}

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

		double totalDropProportion = 0;
		int actualWidth = 0;
		int minDropHeight = yLower + 1;
		for (int x = 0; x < board.width; x++) {
			int y = yLower;
			nonUnusableFound = false;
			while (y < minDropHeight) {
				DesignCellType cellType = board.get(x, y).getDesignCellType();
				if (cellType != DesignCellType.UNUSABLE) nonUnusableFound = true;
				if (cellType == DesignCellType.ICING || cellType == DesignCellType.LIQUORICE) {
					break;
				} else {
					y++;
				}
			}
			if (nonUnusableFound) {
				totalDropProportion += (y / (double) minDropHeight);
				actualWidth++;
			}
		}

		return Math.exp((totalDropProportion / (double) actualWidth) + 1);
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
	
	public static double averageLiquoriceMatches(CandyBoard candy, IntegerBoard matches) {
		int totalLiquorice = 0;
		int totalMatches = 0;
		
		for(int i = 0; i < candy.width(); i++) {
			for(int j = 0; j < candy.height(); j++) {
				if(candy.getCellType(i, j) == DesignCellType.LIQUORICE)
				{
					totalLiquorice++;
					totalMatches += matches.get(i, j);
				}
			
			}
		}
		
		return totalMatches / totalLiquorice;
	}
	
	public static double averageIcingMatches(CandyBoard candy, IntegerBoard matches) {
		int totalIcing = 0;
		int totalMatches = 0;
		
		for(int i = 0; i < candy.width(); i++) {
			for(int j = 0; j < candy.height(); j++) {
				if(candy.getCellType(i, j) == DesignCellType.ICING)
				{
					totalIcing++;
					
					if(candy.getCellType(i, j) == DesignCellType.EMPTY) {
						totalMatches += matches.get(i + 1, j);
					}
					if(candy.getCellType(i, j) == DesignCellType.EMPTY) {
						totalMatches += matches.get(i - 1, j);
					}
					if(candy.getCellType(i, j) == DesignCellType.EMPTY) {
						totalMatches += matches.get(i, j + 1);
					}
					//totalMatches += matches.get(i, j - 1);
				}
			
			}
		}
		
		if(totalIcing == 0) {
			return 4;
		}
		
		return totalMatches / totalIcing;
	}
	
	public static double IcingSurroundFitness(CandyBoard board) {
		int totalSurrounded = 0;
		int total = 0;
		
		for(int i = 0; i < board.width; i++) {
			for(int j = 0; j < board.height; j++) {
								
				if(board.getCellType(i, j) == DesignCellType.ICING || board.getCellType(i, j) == DesignCellType.LIQUORICE) {
					total++;

					if(	board.getCellType(i, j + 1) == DesignCellType.UNUSABLE
							&& board.getCellType(i + 1, j) == DesignCellType.UNUSABLE
							&& board.getCellType(i - 1, j) == DesignCellType.UNUSABLE) {
						totalSurrounded++;
					}
				}
			}
		}
		
		if(total == 0){
			return 1.0;
		}else{
			return Math.pow(1.0 - ((double) totalSurrounded / (total)), 4);
		}
		
	}
	
	public static double icingMatchFitness(CandyBoard candy, IntegerBoard matches) {
		int totalErrors = 0;
		
		for(int i = 0; i < candy.width; i++) {
			for(int j = 0; j < candy.height; j++) {
				if(candy.getCellType(i, j) == DesignCellType.ICING) {
					if(matches.get(i, j) < 2) {
						totalErrors++;
					}
				}
			}
		}
		
		PeakFunction pFunIM = new PeakFunction(50,0.5,0,100,0,1);
		
		return 1.0 - pFunIM.get(totalErrors);
	}
	
	public static void main(String [] args) {
    	BinaryBoard testBoard = new BinaryBoard(5,5);
    	
    	//testBoard.set(0, 0, true);
    	testBoard.set(0, 1, true);
    	testBoard.set(0, 2, true);
    	testBoard.set(0, 3, true);
    	//testBoard.set(0, 4, true);
    	
    	
    	testBoard.set(1, 2, true);
    	
    	testBoard.set(2, 1, true);
    	testBoard.set(2, 2, true);
    	testBoard.set(2, 3, true);
    	
    	BinaryBoard columnTestBoard = new BinaryBoard(5,5);
    	
    	columnTestBoard.set(2, 0, true);
    	
      	System.out.println(boardColumnProportion(testBoard,columnTestBoard));
    	
     	getPossibleMatches(testBoard).print();
     	System.out.println("");
    	testBoard.print();
    	System.out.println("");
	}
	
	public static double edgeFitness(CandyBoard board) {
		int total = 0;
		
		int tDist = 0;
		
		for(int i = 0; i < board.width; i++) {
			for(int j = 0; j < board.height; j++) {
				if(board.getCellType(i, j) == DesignCellType.UNUSABLE) {
					total++;
					
					int id = i - (board.width/2);
					int jd = j - (board.height/2);
					
					tDist += Math.sqrt((id*id) + (jd*jd));
				}
			}
		}
		
		if(total == 0) {
			return 0.1;
		}
		
		if(tDist == 0) {
			return 0.1;
		}
		
		return Math.pow((tDist / (total * (board.width / 2))),2);
	}
	
	public static double matchFitness(DesignBoard board, CandyBoard bBoard) {
		double factor = 1.0;
		
		ILEBoard ileBoard = new ILEBoard(board);
    	
    	IntegerBoard threeMatchBoard = getPossibleMatches(ileBoard);
    	
    	IntegerBoard fiveMatchBoard = getPossibleFiveMatches(bBoard);
    	
    	if(bBoard.getCount() > 0) {
        	
        	PeakFunction pFunMatches = new PeakFunction(1.3, 1, 0, 4, 0.5, 0.8);
    		
    		factor *= pFunMatches.get(threeMatchBoard.getTotal() / bBoard.getCount());
    		
        	PeakFunction pFunFiveCount = new PeakFunction(3,1,0,10,0.1,0.6);
        	
        	factor *= pFunFiveCount.get(fiveMatchBoard.getTotal() / bBoard.getCount());
    	}
    	
    	factor *= icingMatchFitness(bBoard,threeMatchBoard);
    	
    	PeakFunction pFunIcingMatches = new PeakFunction(3, 1, 0, 6, 0.4, 0.8);
    	
    	factor *= pFunIcingMatches.get(averageIcingMatches(ileBoard,threeMatchBoard));
    	
    	return factor;
	}
	
	public static double connectedAreaFitness(DesignBoard board, CandyBoard bBoard) {
		double factor = 1.0;
		
    	ArrayList<ConnectedArea> list = ConnectedArea.getAreas(bBoard, new FourConnectivity());
    	
    	PeakFunction pFunCount = new PeakFunction(3, 1, 1, 8, 1, 0.8);
    	factor *= pFunCount.get((double) list.size()); //
    	
    	PeakFunction pFunConSizeMain = new PeakFunction(50, 1, 18, 81, 0.9, 0.95);
    	PeakFunction pFunConSize = new PeakFunction(20, 1, 6, 25, 0.95, 0.98);
    	
    	IntegerBoard convolutionFilter = new IntegerBoard(3,3); convolutionFilter.setAll(1);
    	
    	PeakFunction pFunConvolution = new PeakFunction(8, 1, 1, 10, 0.50, 0.90);
    	
    	double totalCountFactor = 0.0;
    	double totalConvFactor = 0.1;
    	
    	if(list.size() > 0)
    	{
	    	list.sort(new BinaryBoardComparator());
	    	
	    	totalCountFactor += pFunConSizeMain.get(list.get(0).getCount()); //
	    	
	    	for(int i = 1; i < list.size(); i++) {
	    		totalCountFactor += pFunConSize.get(list.get(i).getCount()); //
	    	}
	    	
	    	for(int i = 0; i < list.size(); i++) {
	    		IntegerBoard cf = list.get(i).convolutionFilter(convolutionFilter, new ANDConvolutionStrategy());
	    		if(list.get(i).getCount() != 0) {
	    		
	    			double cfactor = (double) cf.getTotal() / list.get(i).getCount();
	    		
	    			totalConvFactor += pFunConvolution.get(cfactor);
	    		}
	    	}
    	
	    	factor *= totalCountFactor / list.size();
	    	factor *= totalConvFactor / list.size();
    	}else{
    		factor = Math.max(0.05,factor);
    	}
    	
    	return factor;
	}
	
	public static double boardColumnProportion(BaseBinaryBoard board, BaseBinaryBoard columns) {
		int totalColumnUnders = 0;
		
		for(int i = 0;  i < board.width; i++) {
			int currentColumn = 0;
			
			boolean isColumn = false;
			for(int j = 0; j < board.height; j++) {
				isColumn = isColumn || columns.get(i, j);
				if(isColumn && board.get(i, j)) {
					totalColumnUnders++;
					currentColumn++;
					if(currentColumn > 1) {
						totalColumnUnders += 1;
					}
				}
				
			}
		}
		
		if(board.getCount() == 0) {
			return 0.0;
		}
		
		return (double) totalColumnUnders / (2 * board.getCount());
	}
	
	public static double columnFitness(DesignBoard board) {
		ILEBoard ileBoard = new ILEBoard(board);
		
		ILBoard ilBoard = new ILBoard(board);
		
		PeakFunction pFun = new PeakFunction(0.2,1,0,1,0.8,0.5);
		
		return pFun.get(boardColumnProportion(ileBoard,ilBoard));
	}
	
    /**
     * This method calculates will the fitness between 0 and 1 for how large the overall board is.
     * This fitness metric is based on the maximum width and height of the empty sections of the board
     *
     * @param bBoard    The design of the level
     * @return         The gameplay fitness, 0 <= d <= 1
     */
	
	public static double boardExtentFitness(CandyBoard bBoard) {
		double factor = 1.0;
		
    	PeakFunction pFunConCount = new PeakFunction(7, 1, 5, 9, 0.95, 0.95);
		
    	factor *= pFunConCount.get(bBoard.getHorizontalExtent());
    	factor *= pFunConCount.get(bBoard.getVerticalExtent());
    	
    	return factor;
	}
	
    /**
     * This method calculates will the constraints fitnesss between 0 and 1.
     * This fitness metric is based on the layout of the design.
     *
     * @param board    The design of the level
     * @return         The constraint fitness, 0 <= d <= 1
     */
	
    public static double calculateFitness(DesignBoard board) {
    	CandyBoard bBoard = new CandyBoard(board);
    	
    	double factor = IcingSurroundFitness(bBoard);
    	
    	//factor *= matchFitness(board, bBoard);
    	
    	factor *= columnFitness(board);

    	factor *= boardExtentFitness(bBoard);
    	
    	factor *= connectedAreaFitness(board,bBoard);

		factor *= calculateDropFitness(board);
    	
    	return factor;
    }
}
