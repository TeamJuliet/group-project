package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameState implements Cloneable, Serializable {

    private Cell[][]       board;
    public final Design    levelDesign;
    public final int       width;
    public final int       height;
    int                    movesRemaining;
    int                    score;
    int                    ingredientsRemaining;
    CandyGenerator         candyGenerator;

    private List<Position> detonated    = new ArrayList<Position>();
    private List<Position> popped       = new ArrayList<Position>();
    private Move           lastMove;

    private int            proceedState = 0;

    public GameState(Design design) {
        this.levelDesign = design;
        this.width = levelDesign.getWidth();
        this.height = levelDesign.getHeight();
        this.board = new Cell[width][height];
        this.movesRemaining = levelDesign.getNumberOfMovesAvailable();
        if (design.getMode() == GameMode.INGREDIENTS) {
            this.ingredientsRemaining = design.getObjectiveTarget();
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cellToCopy = design.getCell(x, y);
                board[x][y] = new Cell(cellToCopy);
            }
        }
        recordIngredientSinks();
        candyGenerator = new PseudoRandomCandyGenerator(new DesignParameters(design.getNumberOfCandyColours(),
                                                        design.getMode(),
                                                        design.getObjectiveTarget()));
        fillBoard();

        // Make sure the board is in a stable state
        while (makeSmallMove()) {

        }
    }

    // Copy constructor
    public GameState(GameState original) {
        this.board = new Cell[original.width][original.height];
        for (int x = 0; x < original.width; x++) {
            for (int y = 0; y < original.height; y++) {
                this.board[x][y] = original.board[x][y];
            }
        }
        this.levelDesign = original.levelDesign;
        this.width = original.width;
        this.height = original.height;
        this.movesRemaining = original.movesRemaining;
        this.score = original.score;
        this.ingredientsRemaining = original.ingredientsRemaining;
        this.candyGenerator = original.candyGenerator;
        for (Position p : original.detonated)
            this.detonated.add(new Position(p));
        for (Position p : original.popped)
            this.popped.add(new Position(p));
        this.lastMove = original.lastMove;
        this.proceedState = original.proceedState;
        recordIngredientSinks();
    }

    public GameState(GameState original, CandyGenerator candyGenerator) {
    	this(original);
    	this.candyGenerator = candyGenerator;
    }

    // This constructor is for testing purposes
    public GameState(Cell[][] board, int score, CandyGenerator candyGenerator) {
        this.width = board.length;
        this.height = board[0].length;
        this.movesRemaining = 100;
        this.levelDesign = new Design();
        this.board = board;
        this.candyGenerator = candyGenerator;

        // Make sure the board is in a stable state
        while (makeSmallMove()) {

        }

        this.score = score;
    }

    // Get methods
    public int getHeight() {
        return levelDesign.getHeight();
    }

    public int getWidth() {
        return levelDesign.getHeight();
    }

    public Cell[][] getBoard() {
        return board;
    }

    public CandyGenerator getCandyGenerator() {
        return candyGenerator;
    }

    public int getScore() {
        return score;
    }

    public int getIngredientsRemaining () {
        return ingredientsRemaining;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }
    
    private void incrementScore(int addedScore) {
    	this.score += addedScore;
    }

    @Override
    public boolean equals(Object toCompare) {
        GameState gameStateToCompare = (GameState) toCompare;
        boolean isEqual = true;

        // Check the basic parameters match
        isEqual &= (this.height == gameStateToCompare.height) && (this.width == gameStateToCompare.width)
                && (this.movesRemaining == gameStateToCompare.movesRemaining)
                && (this.score == gameStateToCompare.score);
                // NOTE:
                // I have left out comparison of the CandyGenerator, since this
                // isn't
                // needed in
                // the unit testing.

        // If so, then check the candies on the board match
        if (isEqual) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    isEqual &= (board[row][col].equals(gameStateToCompare.board[row][col]));
                }
            }
        }

        return isEqual;
    }

    @Override
    public GameState clone() {

        GameState clone = new GameState(levelDesign);

        // Copy the basic parameters
        clone.movesRemaining = this.movesRemaining;
        clone.score = this.score;

        // TODO: Sort out cloning of CandyGenerator instance if necessary

        // Copy the candies on the board
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                clone.board[row][col] = (Cell) this.board[row][col].clone();
            }
        }
        return clone;
    }

    private class SingleTileAnalysis {
        // the start and end of the match on x-axis.
        public final int start_x, end_x;
        // the start and end of the match on y-axis.
        public final int start_y, end_y;
        // the coordinates of the central tile.
        public final int x, y;

        public SingleTileAnalysis(int x, int y, int start_x, int end_x, int start_y, int end_y) {
            this.start_x = start_x;
            this.end_x = end_x;
            this.start_y = start_y;
            this.end_y = end_y;
            this.x = x;
            this.y = y;
        }

        // Returns the size of the horizontal match containing the point.
        public int getLengthX() {
            return end_x - start_x + 1;
        }

        // Returns the size of the vertical match containing the point.
        public int getLengthY() {
            return end_y - start_y + 1;
        }
    }

    // Returns the axial analysis for a single tile.
    // (i.e. the intervals on the x and y-axis that match the tile.
    private SingleTileAnalysis analyzeTile(Position pos) {
        int x = pos.x, y = pos.y;
        CandyColour cellColour = board[x][y].getCandy().getColour();
        int start_x = x - 1, end_x = x + 1, start_y = y - 1, end_y = y + 1;
        while (start_x >= 0 && sameColourWithCell(board[start_x][y], cellColour))
            --start_x;
        while (end_x < width && sameColourWithCell(board[end_x][y], cellColour))
            ++end_x;
        while (start_y >= 0 && sameColourWithCell(board[x][start_y], cellColour))
            --start_y;
        while (end_y < height && sameColourWithCell(board[x][end_y], cellColour))
            ++end_y;
        // Normalisation.
        start_x++;
        start_y++;
        end_x--;
        end_y--;
        return new SingleTileAnalysis(x, y, start_x, end_x, start_y, end_y);
    }

    // Function that returns whether the tile at position forms a match.
    private boolean tileFormsMatch(Position position) {
        SingleTileAnalysis analysis = analyzeTile(position);
        return analysis.getLengthX() > 2 || analysis.getLengthY() > 2;
    }

    private CandyType getSpecialCandyFormed(SingleTileAnalysis analysis) {
        if (analysis.getLengthX() < 3 && analysis.getLengthY() < 3)
            return null;
        else if (analysis.getLengthX() < 3 && analysis.getLengthY() == 4)
            return CandyType.VERTICALLY_STRIPPED;
        else if (analysis.getLengthY() < 3 && analysis.getLengthX() == 4)
            return CandyType.HORIZONTALLY_STRIPPED;
        else if (analysis.getLengthX() == 5 || analysis.getLengthY() == 5)
            return CandyType.BOMB;
        else
            return CandyType.WRAPPED;
    }

    private MatchAnalysis getSingleMatchAnalysis(Position pos) {
        SingleTileAnalysis analysis = analyzeTile(pos);
        if (analysis.getLengthX() < 3 && analysis.getLengthY() < 3)
            return null;
        List<Position> positions = new LinkedList<Position>();
        List<CandyType> specials = new LinkedList<CandyType>();
        if (analysis.getLengthX() > 2) {
            for (int x = analysis.start_x; x <= analysis.end_x; ++x) {
                if (x == pos.x)
                    continue;
                Position currentPosition = new Position(x, pos.y);
                positions.add(currentPosition);
                if (hasSpecial(currentPosition))
                    specials.add(getCell(currentPosition).getCandy().getCandyType());
            }
        }
        if (analysis.getLengthY() > 2) {
            for (int y = analysis.start_y; y <= analysis.end_y; ++y) {
                if (y == pos.y)
                    continue;
                Position currentPosition = new Position(pos.x, y);
                positions.add(currentPosition);
                if (hasSpecial(currentPosition))
                    specials.add(getCell(currentPosition).getCandy().getCandyType());
            }
        }
        return new MatchAnalysis(positions, specials, getSpecialCandyFormed(analysis));
    }

    // Function that returns the matches formed by a move.
    // (Note: that when the board is in a final state then this is at most two).
    public List<MatchAnalysis> getMatchAnalysis(Move move) {
        // Make move in order to get information.
        swapCandies(move);
        List<MatchAnalysis> ret = new ArrayList<MatchAnalysis>();
        MatchAnalysis analysis1 = getSingleMatchAnalysis(move.p1);
        MatchAnalysis analysis2 = getSingleMatchAnalysis(move.p2);
        if (analysis1 != null)
            ret.add(analysis1);
        if (analysis2 != null)
            ret.add(analysis2);
        // reverse the operation.
        swapCandies(move);
        return ret;
    }

    private boolean wasSomethingPopped = false;

    // Function that adds the tile to detonated (the ones that are going to
    // break on the
    // next state). Note that bombs should have two levels of detonation.
    // Should also update score.
    // Consider making this i, j.
    // TODO: Need to add scoring.
    private void trigger(int x, int y, int score) {
        if (!inBoard(new Position(x, y)))
            return;
        Cell current = board[x][y];
        if (current.getCellType().equals(CellType.UNUSABLE)) return;
        
        touchNeighbours(x, y);
        // There is no additional score for LIQUORICE lock.
        if (current.getCellType().equals(CellType.LIQUORICE)) {
        	current.setCellType(CellType.NORMAL);
        	// Should not remove any jelly layer if the cell type is liquorice.
        	return;
        }
        if (current.removeJellyLayer()) {
        	incrementScore(Scoring.MATCHED_A_JELLY);
        }
        if (current.hasCandy() && current.getCandy().isDetonated())
            return;
        if (current.hasCandy() && current.getCandy().getCandyType().isSpecial()) {
            // TODO: Check how this reacts.
            if (current.getCandy().getCandyType().equals(CandyType.BOMB))
                return;
            if (!current.getCandy().isDetonated()) {
                detonated.add(new Position(x, y));
                current.getCandy().setDetonated();
                wasSomethingPopped = true;
            }
        } else if (current.hasCandy()) {
            current.removeCandy();
            incrementScore(score);
            wasSomethingPopped = true;
        }
    }
    
    private void touch(int x, int y) {
    	if (!inBoard(new Position(x, y))) return;
    	Cell current = board[x][y];
    	if (current.getCellType().equals(CellType.ICING)) {
    		//  TODO: Score here is where an icing is removed.
    		current.setCellType(CellType.EMPTY);
    	}
    }    
    // Function that touches the neighbours of a triggered cell.
    private void touchNeighbours(int x, int y) {
    	if (!inBoard(new Position(x, y))) return;
    	touch(x + 1, y);
    	touch(x - 1, y);
    	touch(x, y + 1);
    	touch(x, y - 1);
    }

    // Check if any of the two positions is in the horizontal range.
    private boolean moveInHorizontalRange(Move move, int startX, int endX) {
        return inRange(lastMove.p1.x, startX, endX) || inRange(lastMove.p2.x, startX, endX);
    }

    // Check if any of the two positions is in the vertical range.
    private boolean moveInVerticalRange(Move move, int startY, int endY) {
        return inRange(lastMove.p1.y, startY, endY) || inRange(lastMove.p2.y, startY, endY);
    }
    
    private void makeCellBomb(int x, int y) {
    	incrementScore(Scoring.MADE_BOMB);
    	board[x][y].setCandy(new Candy(null, CandyType.BOMB));
    }
    
    private void makeWrapped(int x, int y, CandyColour clr) {
    	incrementScore(Scoring.MADE_WRAPPED_CANDY);
    	board[x][y].setCandy(new Candy(clr, CandyType.WRAPPED));
    }
    
    private void makeStripped(int x, int y, CandyColour clr, boolean isVertical) {
    	incrementScore(Scoring.MADE_STRIPPED_CANDY);
    	board[x][y].setCandy(new Candy(clr, isVertical ? CandyType.VERTICALLY_STRIPPED : CandyType.HORIZONTALLY_STRIPPED));
    }
    
    private static boolean VERTICAL = true;
    private static boolean HORIZONTAL = false;
    
    // Function that replaces all the matched tiles with their respective
    // Candy (either empty or special is some cases).
    private void markAndReplaceMatchingTiles() {
        // TODO(Dimitrios): make this more concise.
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // Do not consider cells with no candy.
                if (!board[x][y].hasCandy()) continue;
                CandyColour colour = board[x][y].getCandy().getColour();
                SingleTileAnalysis analysis = analyzeTile(new Position(x, y));
                // In case there is a horizontal match.
                if (analysis.getLengthX() > 2) {
                    boolean foundVertical = false;
                    // If it forms a bomb there is no need to place wrapped.
                    if (analysis.getLengthX() >= 5)
                        foundVertical = true;
                    for (int k = analysis.start_x; k <= analysis.end_x; ++k) {
                        SingleTileAnalysis childAnalysis = analyzeTile(new Position(k, y));
                        if (childAnalysis.getLengthY() > 2) {
                            // This candy will be replaced by a wrapped one if
                            // it is the first junction.
                            if (!foundVertical) {
                                foundVertical = true;
                                makeWrapped(k, y, colour);
                            } else {
                                // Otherwise just empty that candy.
                                trigger(k, y, Scoring.NO_ADDITIONAL_SCORE);
                            }
                            // Iterate through the vertical column and trigger
                            // them.
                            for (int yy = childAnalysis.start_y; yy <= childAnalysis.end_y; ++yy) {
                                if (yy == y)
                                    continue;
                                trigger(k, yy, Scoring.NO_ADDITIONAL_SCORE);
                            }
                        } else {
                            // If there is no vertical match formed then just
                            // trigger the cell.
                            trigger(k, y, Scoring.NO_ADDITIONAL_SCORE);
                        }
                    }
                    if (analysis.getLengthX() == 3) {
                    	incrementScore(Scoring.MATCHED_3);
                    	continue;
                    }
                        
                    else if (!foundVertical) {

                        // If last move is in the range we went through,
                        // then make that one the stripped candy.
                        if (lastMove == null || !moveInHorizontalRange(lastMove, analysis.start_x, analysis.end_x)) {
                            // Make the middle candy vertically stripped.
                        	makeStripped(x+1, y, colour, VERTICAL);
                        } else {
                            int coordinate;
                            // If one of the positions has the same
                            // y-coordinate.
                            if (lastMove.p1.y == y)
                                coordinate = lastMove.p1.x;
                            else
                                coordinate = lastMove.p2.x;
                            makeStripped(coordinate, y, colour, VERTICAL);
                        }

                    } else {
                        // The bomb created will be aligned with the first move since it will always be the middle one.
                        if (analysis.getLengthX() >= 5) {
                        	makeCellBomb(x+2, y);
                        }
                    }

                }
                /* Symmetric case for the vertical matches. */
                else if (analysis.getLengthY() > 2) {
                    boolean foundHorizontal = false;
                    // If it forms a bomb there is no need to place wrapped.
                    if (analysis.getLengthY() >= 5)
                        foundHorizontal = true;
                    for (int k = analysis.start_y; k <= analysis.end_y; ++k) {
                        SingleTileAnalysis childAnalysis = analyzeTile(new Position(x, k));
                        if (childAnalysis.getLengthX() > 2) {
                            // This candy will be replaced by a wrapped one if
                            // it is the first junction.
                            if (!foundHorizontal) {
                                foundHorizontal = true;
                                makeWrapped(x, k, colour);
                            } else {
                                // Otherwise just empty that candy.
                                trigger(x, k, Scoring.NO_ADDITIONAL_SCORE);
                            }
                            // Iterate through the vertical column and trigger
                            // them.
                            for (int xx = childAnalysis.start_x; xx <= childAnalysis.end_x; ++xx) {
                                if (xx == x)
                                    continue;
                                trigger(xx, k, Scoring.NO_ADDITIONAL_SCORE);
                            }
                        } else {
                            // If there is no vertical match formed then just
                            // trigger the cell.
                            trigger(x, k, Scoring.NO_ADDITIONAL_SCORE);
                        }
                    }
                    if (analysis.getLengthY() == 3){
                    	incrementScore(Scoring.MATCHED_3);
                        continue;
                    }
                    else if (!foundHorizontal) {
                        // If last move is in the range we went through,
                        // then make that one the stripped candy.
                        if (lastMove == null || !moveInVerticalRange(lastMove, analysis.start_y, analysis.end_y)) {
                            // Make the middle candy vertically stripped.
                        	makeStripped(x, y+1, colour, HORIZONTAL);
                        } else {
                            int coordinate;
                            // If one of the positions has the same
                            // x-coordinate.
                            if (lastMove.p1.x == x)
                                coordinate = lastMove.p1.y;
                            else
                                coordinate = lastMove.p2.y;
                            makeStripped(x, coordinate, colour, HORIZONTAL);
                        }
                    } else {
                        if (analysis.getLengthY() >= 5) {
                        	makeCellBomb(x, y+2);
                        }
                    }
                }

            }
        }
    }

    // Function that triggers all cells in the cross around position.
    private void detonateWrappedWrapped(Position pos) {
    	incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
    	incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        int yWindow;
        for (int x = pos.x - 3; x < pos.x + 3; ++x) {
            if (inRange(x, pos.x - 1, pos.x))
                yWindow = 3;
            else
                yWindow = 2;
            for (int y = pos.y - yWindow; y < pos.y + yWindow; ++y) {
                trigger(x, y, Scoring.WRAPPED_INDIVIDUAL);
            }
        }
    }

    // Function that performs the operation for combining two bombs.
    private void detonateBombBomb() {
    	incrementScore(Scoring.DETONATE_BOMB);
    	incrementScore(Scoring.DETONATE_BOMB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
            	// TODO: Check if this is the case
                trigger(x, y, Scoring.BOMB_INDIVIDUAL);
            }
        }
    }
    
    // Function that will detonate a wrapped candy.
    private void detonateWrapped(Position wrapped) {
    	incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        for (int i = wrapped.x - 1; i <= wrapped.x + 1; ++i) {
            for (int j = wrapped.y - 1; j <= wrapped.y + 1; ++j) {
                if (i == wrapped.x && j == wrapped.y) {
                    continue;
                }
                trigger(i, j, Scoring.WRAPPED_INDIVIDUAL);
            }
        }
    }

    // Function that performs the clearing of the vertical line for the stripped
    // candy.
    private void detonateVerticallyStripped(Position vStripped) {
    	incrementScore(Scoring.DETONATE_STRIPPED_CANDY);
        for (int y = 0; y < height; ++y) {
            trigger(vStripped.x, y, Scoring.STRIPPED_INDIVIDUAL);
        }
    }

    // Function that performs the clearing of the horizontal line for the
    // stripped candy.
    private void detonateHorizontallyStripped(Position hStripped) {
        for (int x = 0; x < width; ++x) {
            trigger(x, hStripped.y,Scoring.STRIPPED_INDIVIDUAL);
        }
    }

    // Function that creates a cross of width 3 around the locations that were swapped.
    private void detonateWrappedStripped(Position pos) {
        for (int x = pos.x - 1; x <= pos.x + 1; ++x) {
            for (int y = 0; y < height; ++y) {
                trigger(x, y, Scoring.WRAPPED_STRIPPED_INDIVIDUAL);
            }
        }

        for (int y = pos.y - 1; y <= pos.y + 1; ++y) {
            for (int x = 0; x < width; ++x) {
                trigger(x, y, Scoring.WRAPPED_STRIPPED_INDIVIDUAL);
            }
        }
    }

    // Function that detonates all the elements that have to explode
    private void detonateAllPending() {
        List<Position> oldDetonated = detonated;
        detonated = new ArrayList<Position>();
        for (Position d : oldDetonated) {
            Cell detonatingCell = getCell(d);
            switch (detonatingCell.getCandy().getCandyType()) {
            case WRAPPED:
                detonateWrapped(d);
                break;
            case HORIZONTALLY_STRIPPED:
                detonateHorizontallyStripped(d);
                break;
            case VERTICALLY_STRIPPED:
                detonateVerticallyStripped(d);
                break;
            default:
            }
            if (detonatingCell.getCandy().getDetonationsRemaining() > 1) {
                detonatingCell.getCandy().decreaseDetonations();
                // detonated.add(d);
            } else {
                detonatingCell.removeCandy();
            }
        }
    }


    private List<Position> ingredientSinkPositions = new ArrayList<Position>();
    
    private boolean hasIngredient(Position pos) {
    	Cell cell = getCell(pos);
    	return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.INGREDIENT); 
    }
    
    private void recordIngredientSinks() {
    	for (int x = 0; x < width; ++x) {
    		for (int y = 0; y < height; ++y) {
    			Position current = new Position(x, y);
    			if (getCell(current).isIngredientSink) ingredientSinkPositions.add(current);  
    		}
    	}
    }
    
    private void decreaseRemainingIngredients() {
    	incrementScore(Scoring.BROUGHT_INGREDIENT_DOWN);
    	--this.ingredientsRemaining;
    }
    
    // Function that passes ingredients through the sink.
    private boolean passIngredients() {
    	boolean passedIngredient = false;
    	for (Position ingredientSink : ingredientSinkPositions) {
    		// System.err.println("Hello " + ingredientSink.x + ingredientSink.y);
    		if (hasIngredient(ingredientSink)) {
    			System.err.println("Enter");
    			Cell cell = getCell(ingredientSink);
    			cell.setCellType(CellType.EMPTY);
    			decreaseRemainingIngredients();
    			passedIngredient = true;
    		}
    	}
    	return passedIngredient;
    }
    
    // Brings candies down.
    private void bringDownCandies() {
    	passIngredients();
        detonated = new ArrayList<Position>();
        for (int i = 0; i < width; ++i) {
            for (int j = height - 1; j >= 1; --j) {
                if (board[i][j].getCellType().equals(CellType.EMPTY)) {
                    int y = j - 1;
                    // TODO: can be optimized.
                    while (y >= 0 && !board[i][y].canDropCandy())
                        y--;
                    // Replacement was found.
                    if (y >= 0) {
                        board[i][j].setCandy(board[i][y].getCandy());
                        board[i][y].removeCandy();
                    }
                }
                if (board[i][j].hasCandy() && board[i][j].getCandy().isDetonated()) {
                    // System.out.println("Moved Detonating Candy.");
                    detonated.add(new Position(i, j));
                }
            }
        }
        
    }

    // Function that fills the board by requesting candies from the
    // candyGenerator.
    private void fillBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = board[x][y];
                if (cell.isFillable()) {
                    cell.setCandy(candyGenerator.generateCandy(x));
                }
            }
        }
    }

    Cell getCell(int x, int y) {
        return board[x][y];
    }

    // Auxiliary function to check a <= x <= b
    private boolean inRange(int x, int a, int b) {
        return x >= a && x <= b;
    }

    private boolean inBoard(Position pos) {
        return inRange(pos.x, 0, width - 1) && inRange(pos.y, 0, height - 1);
    }

    private boolean isPositionValidAndMoveable(Position pos) {
        return inBoard(pos) && board[pos.x][pos.y].isMoveable();
    }

    private Cell getCell(Position pos) {
        return board[pos.x][pos.y];
    }

    protected void swapCandies(Move move) {
        Cell cell1 = getCell(move.p1), cell2 = getCell(move.p2);
        // Swap values and check if the tiles form a match.
        Candy tmp = cell1.getCandy();
        cell1.setCandy(cell2.getCandy());
        cell2.setCandy(tmp);
    }

    public boolean isMoveValid(Move move) {
        if (!isPositionValidAndMoveable(move.p1) || !isPositionValidAndMoveable(move.p2))
            return false;
        // Check move is for adjacent positions.
        if (Math.abs(move.p1.x - move.p2.x) + Math.abs(move.p1.y - move.p2.y) != 1)
            return false;
        Cell cell1 = getCell(move.p1), cell2 = getCell(move.p2);
        if (cell1.getCandy().getCandyType().isSpecial() && cell2.getCandy().getCandyType().isSpecial())
            return true;
        // Exchanging a Bomb with a cell that has a moveable item is a valid
        // move.
        else if (cell1.getCandy().getCandyType().equals(CandyType.BOMB)
                || cell2.getCandy().getCandyType().equals(CandyType.BOMB))
            return true;

        swapCandies(move);
        boolean isValid = tileFormsMatch(move.p1) || tileFormsMatch(move.p2);
        // Place candies as they were initially.
        swapCandies(move);
        return isValid;
    }

    private boolean sameColourWithCell(Cell c, CandyColour colour) {
        if (c.getCandy() == null || c.getCandy().getColour() == null)
            return false;
        return c.getCandy().getColour().equals(colour);
    }

    // Function that performs the combination of a bomb and a Normal Candy.
    private void breakAllOf(CandyColour colour) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (sameColourWithCell(board[i][j], colour))
                    trigger(i, j, Scoring.BOMB_INDIVIDUAL);
            }
        }
    }

    // Function that performs the combination of a bomb and a Special Candy.
    private void replaceWithSpecialAllOf(CandyColour colourMatched, CandyType typeToReplace) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (sameColourWithCell(board[i][j], colourMatched)) {
                    board[i][j].setCandy(new Candy(colourMatched, typeToReplace));
                    // TODO: Check if this is the case.
                    trigger(i, j, Scoring.BOMB_INDIVIDUAL);
                }
            }
        }
    }

    // TODO: Consider moving these function is more appropriate place.

    // Function that checks whether the position contains a special candy.
    private boolean hasSpecial(Position pos) {
        return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().isSpecial();
    }

    // Function that checks whether the position contains a bomb.
    private boolean hasBomb(Position pos) {
        return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().equals(CandyType.BOMB);
    }

    // Function that checks whether the position contains a vertically stripped.
    private boolean hasVerticallyStripped(Position pos) {
        return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().equals(CandyType.VERTICALLY_STRIPPED);
    }

    // Function that checks whether the position contains a horizontally
    // stripped.
    private boolean hasHorizontallyStripped(Position pos) {
        return getCell(pos).hasCandy()
                && getCell(pos).getCandy().getCandyType().equals(CandyType.HORIZONTALLY_STRIPPED);
    }

    // Function that checks whether the position contains a wrapped candy.
    private boolean hasWrapped(Position pos) {
        return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().equals(CandyType.WRAPPED);
    }

    private boolean hasStripped(Position pos) {
        return hasVerticallyStripped(pos) || hasHorizontallyStripped(pos);
    }

    public void makeMove(Move move) throws InvalidMoveException {
        if (!isMoveValid(move))
            throw new InvalidMoveException(move);
        // Record the last move.
        lastMove = move;
        swapCandies(move);

        // Reduce the number of remaining moves available
        movesRemaining--;

        Position p1 = move.p1, p2 = move.p2;
        if (hasBomb(p1) && hasBomb(p2)) {
            detonateBombBomb();
        } else if (hasBomb(p1) && hasSpecial(p2)) {
            getCell(p1).removeCandy();
            replaceWithSpecialAllOf(getCell(p2).getCandy().getColour(), getCell(p2).getCandy().getCandyType());
        } else if (hasBomb(p2) && hasSpecial(p1)) {
            getCell(p2).removeCandy();
            replaceWithSpecialAllOf(getCell(p1).getCandy().getColour(), getCell(p1).getCandy().getCandyType());
        } else if (hasBomb(p1)) {
            getCell(p1).removeCandy();
            breakAllOf(getCell(p2).getCandy().getColour());
        } else if (hasBomb(p2)) {
            getCell(p2).removeCandy();
            breakAllOf(getCell(p1).getCandy().getColour());
        } else if (hasHorizontallyStripped(p1) && hasVerticallyStripped(p2)) {
            getCell(p1).removeCandy();
            getCell(p2).removeCandy();
            detonateHorizontallyStripped(p1);
            detonateVerticallyStripped(p2);
        } else if (hasVerticallyStripped(p1) && hasHorizontallyStripped(p2)) {
            getCell(p1).removeCandy();
            getCell(p2).removeCandy();
            detonateVerticallyStripped(p1);
            detonateHorizontallyStripped(p2);
        }
        // TODO: Check if this is the right thing to do.
        else if ((hasVerticallyStripped(p1) && hasVerticallyStripped(p2))
                || (hasHorizontallyStripped(p1) && hasHorizontallyStripped(p2))) {
            getCell(p1).removeCandy();
            getCell(p2).removeCandy();
            detonateVerticallyStripped(p1);
            detonateHorizontallyStripped(p2);
        } else if (hasWrapped(p1) && hasStripped(p2)) {
            getCell(p1).removeCandy();
            getCell(p2).removeCandy();
            detonateWrappedStripped(p1);
        } else if (hasWrapped(p2) && hasStripped(p1)) {
            getCell(p1).removeCandy();
            getCell(p2).removeCandy();
            detonateWrappedStripped(p2);
        } else if (hasWrapped(p1) && hasWrapped(p2)) {
            detonateWrappedWrapped(p1);
        } else {
            makeSmallMove();
        }
    }

    // Performs the corresponding action on each step (will be used by the
    // DisplayBoard extension).
    public boolean makeSmallMove() {
        if (proceedState == 0) {
            System.out.println("1: Mark and replace tiles on");
            // debugBoard();
            markAndReplaceMatchingTiles();
            lastMove = null;
            if (!wasSomethingPopped && !candiesNeededShuffling())
                return false;
        } else if (proceedState == 1) {
            detonateAllPending();
            System.out.println("2: Detonating all pending.");
        } else if (proceedState == 2) {
            System.out.println("3: Bringing down some candies (and filling board).");
            bringDownCandies();
            if (passIngredients()) {
            	proceedState = 1;
            } else {
	            fillBoard();
	            if (detonated.isEmpty()) {
	                wasSomethingPopped = false;
	            } else {
	                proceedState = 0;
	            }
            }
        }
        proceedState = (proceedState + 1) % 3;
        return true;
    }

    // TODO: Handle case in which no amount of shuffling can introduce a possible move - i.e. we have need some
    // concept of "GAME OVER"
    private boolean candiesNeededShuffling () {
        boolean didShuffle = false;

        // It is quite complicated (and expensive to compute) whether there exists a shuffle which introduces a
        // possible move, so for now I think we should just shuffle up to some limit, at which point we declare that
        // the game is over
        int movesAvailable;
        int shuffleLimit = 5;
        int shuffleCount = 0;

        // While there are no available moves, we need to shuffle the normal (non-special) candies on the board
        while ((movesAvailable = getValidMoves().size()) == 0 && shuffleCount < shuffleLimit) {

            System.out.println("No moves available: Shuffling candies...");

            // Collect all of the colours of the normal candies
            LinkedList<CandyColour> normalCandyColours = new LinkedList<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Candy cellCandy = board[x][y].getCandy();
                    if (cellCandy != null && cellCandy.getCandyType() == CandyType.NORMAL) {
                        normalCandyColours.add(cellCandy.getColour());
                    }
                }
            }

            // Shuffle the colours - the reason I'm shuffling colours and not candies is so that jelly blocks aren't
            // moved around
            Collections.shuffle(normalCandyColours);

            // Redistribute the shuffled colours
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Candy cellCandy = board[x][y].getCandy();
                    if (cellCandy != null && cellCandy.getCandyType() == CandyType.NORMAL) {
                        cellCandy.setColour(normalCandyColours.poll());
                    }
                }
            }

            didShuffle = true;
            shuffleCount++;
        }

        if (movesAvailable == 0) {
            // TODO: Handle the case where we give up shuffling and declare the game over
            // Perhaps we could throw an exception at this point, such as NoAvailableMovesException or GameOverException
        }

        return didShuffle;
    }

    // TODO: Do we even need this function? If not - we should remove it
    private void proceed() {
        wasSomethingPopped = false;
        markAndReplaceMatchingTiles();
        detonateAllPending();
        bringDownCandies();
        lastMove = null;
        if (wasSomethingPopped)
            proceed();
    }

    // Returns a list of all valid moves.
    public List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                Move move1 = new Move(new Position(i, j), new Position(i + 1, j));
                if (isMoveValid(move1))
                    moves.add(move1);
                Move move2 = new Move(new Position(i, j), new Position(i, j + 1));
                if (isMoveValid(move2))
                    moves.add(move2);
            }
        }
        return moves;
    }

    private String candyColorToString(CandyColour candy) {
        if (candy == null)
            return "B";
        switch (candy) {
        case RED:
            return "R";
        case GREEN:
            return "G";
        case YELLOW:
            return "Y";
        case ORANGE:
            return "O";
        case BLUE:
            return "B";
        case PURPLE:
            return "P";
        }
        return "";
    }

    private String candyTypeToString(CandyType candy) {
        switch (candy) {
        case NORMAL:
            return "N";
        case VERTICALLY_STRIPPED:
            return "V";
        case HORIZONTALLY_STRIPPED:
            return "H";
        case WRAPPED:
            return "W";
        case BOMB:
            return "B";
        }
        return "";
    }

    private String cellToString(int i, int j) {
        Cell cell = getCell(i, j);
        if (cell.hasCandy()) {
            return candyTypeToString(cell.getCandy().getCandyType()) + candyColorToString(cell.getCandy().getColour());
        }
        return "EE";
    }

    public void debugBoard() {
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < width; ++i)
            System.out.print(" " + i + " ");
        System.out.println();
        for (int i = 0; i < height; ++i) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < width; ++j) {
                System.out.print(cellToString(j, i) + " ");
            }
            System.out.println();
        }
    }
}