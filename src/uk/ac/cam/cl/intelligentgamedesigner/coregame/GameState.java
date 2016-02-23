package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.*;

public class GameState implements Serializable {
	
	// The current state of the board.
    private Cell[][]           board;
    
    // The design used to create the game state.
    public final Design        levelDesign;
    
    // The dimensions of the board.
    public final int           width, height;
    
    // The generator used for filling in the empty positions.
    CandyGenerator             candyGenerator;

    // The candies that detonated on the current state cycle.
    private List<Position>     detonated               = new ArrayList<Position>();
    
    // The last move that was made (should be null if in the current state cycle, there was no move made).
    private Move               lastMove;
    
    // The game progress made in the game.
    private GameStateProgress  progress;
    
    // Boolean that indicates whether something has been popped in the current state diagram cycle.
    private boolean            wasSomethingPopped      = false;
    
    // The positions that are ingredient sinks on the board.
    private List<Position>     ingredientSinkPositions = new ArrayList<Position>();
    
    // Predicate that determines whether shuffles should occur.
    // Note: this is set to false only for testing.
    private boolean            shouldShuffle           = true;
    
    // The current state in the process state diagram.
    private ProcessState       currentProcessState     = ProcessState.AWAITING_MOVE;

    // Statistics accumulators.
    private CandiesAccumulator statCandiesRemoved      = new CandiesAccumulator();
    private CandiesAccumulator statCandiesFormed       = new CandiesAccumulator();
    private ProcessStateStats  statProcess             = new ProcessStateStats();

    // This number will be used to get the scoring due to multiple matches in
    // a single round.
    private int                numberOfMatchedInRound  = 0;
    
    // The design used when the game state was constructed.
    public final Design        design;

    /**
     * GameState creation using the design specification.
     * 
     * @param design
     *            The design given by the level generator.
     */
    public GameState(Design design) {
        this.levelDesign = design;
        this.width = levelDesign.getWidth();
        this.height = levelDesign.getHeight();
        this.board = new Cell[width][height];
        this.progress = new GameStateProgress(design);

        // Initially create an empty board, see (***) for why.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = new Cell(CellType.EMPTY);
            }
        }

        candyGenerator = new PseudoRandomCandyGenerator(design, this.progress);
        // candyGenerator = design.getCandyGenerator(this.progress);
        fillBoard();

        // Make sure the board is in a stable state
        while (makeSmallMove())
            ;

        // *** Copy the design elements AFTER filling the board with normal
        // candies. The reason for this is that if we
        // add the design elements (icing, liquorice, etc) BEFORE the normal
        // candies, then the normal candies may
        // introduce an unstable state and destroy some of the design elements,
        // which means the game will start with
        // some of the desired design elements missing (e.g. an icing block
        // missing).
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cellToCopy = design.getCell(x, y);
                CellType cellType = cellToCopy.getCellType();
                Candy cellCandy = cellToCopy.getCandy();

                // For ICING and UNUSABLEs, we can just replace the cell with
                // the design element
                if (cellType == CellType.ICING || cellType == CellType.UNUSABLE
                        || (cellCandy != null && cellCandy.getCandyType() == CandyType.INGREDIENT)) {
                    board[x][y] = new Cell(cellToCopy);
                }
                // For LIQUORICE and EMPTY cells, we want to replace everything
                // except for the
                // normal underlying candy
                else if (cellType == CellType.LIQUORICE || cellType == CellType.EMPTY) {
                    board[x][y] = new Cell(
                            (cellType.equals(CellType.EMPTY) & board[x][y].hasCandy()) ? CellType.NORMAL : cellType,
                            board[x][y].getCandy(), cellToCopy.getJellyLevel(), cellToCopy.isIngredientSink());
                }
            }
        }

        recordIngredientSinks();

        // The score may have been prematurely increased from initial
        // reductions, so we reset it to 0
        this.progress.resetScore();
        this.design = design;

        // For really shit levels, there won't even be a possible move from the
        // start - we need to check for this
        if (getValidMoves().size() == 0) {
            progress.setDidFailShuffle();
        }
    }

    /**
     * Copy constructor for the game state.
     * @param original The original game state to be copied.
     */
    public GameState(GameState original) {
        this.board = new Cell[original.width][original.height];
        for (int x = 0; x < original.width; x++) {
            for (int y = 0; y < original.height; y++) {
                this.board[x][y] = new Cell(original.board[x][y]);
            }
        }
        this.levelDesign = original.levelDesign;
        this.width = original.width;
        this.height = original.height;
        this.progress = new GameStateProgress(original.progress);
        this.candyGenerator = original.candyGenerator;
        for (Position p : original.detonated)
            this.detonated.add(new Position(p));
        this.lastMove = original.lastMove;
        this.design = original.design;
        recordIngredientSinks();
    }

    /**
     * Constructor that is used to change the candy generator and allow for
     * checking the future board states.
     * @param original
     * @param candyGenerator
     */
    public GameState(GameState original, CandyGenerator candyGenerator) {
        this(original);
        this.candyGenerator = candyGenerator;
    }

    /**
     * This constructor is for testing purposes
     * @param board The board that should be copied (and is assumed to be reduced.
     * @param progress The current progress for the game.
     * @param candyGenerator The generator that will be used.
     */
    public GameState(Cell[][] board, GameStateProgress progress, CandyGenerator candyGenerator) {
        this.width = board.length;
        this.height = board[0].length;
        this.progress = new GameStateProgress(progress);
        this.levelDesign = new Design();
        this.board = board;
        this.candyGenerator = candyGenerator;
        // We do not want shuffles in testing.
        this.shouldShuffle = false;
        // Make sure the board is in a stable state
        while (makeSmallMove())
            ;
        recordIngredientSinks();
        this.design = null;
    }

    // **** GETTER FUNCTIONS START *****

    // TODO: Consider returning a copy of the board.
    public Cell[][] getBoard() {
        return board;
    }

    /**
     * Get the current process state that the game state is in.
     * @return The current process state.
     */
    public ProcessState getCurrentProcessState() {
        return this.currentProcessState;
    }

    /**
     * Get a copy of the cell at that position.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return A copy of the cell at that position.
     */
    public Cell getCell(int x, int y) {
        return new Cell(board[x][y]);
    }

    /** 
     * Get the progress view of the current state of the board. 
     * @return the progress view of the current state of the board.
     */
    public GameStateProgressView getGameProgress() {
        return new GameStateProgressView(progress);
    }

    /**
     * Get the statistics for the round. Note: these will be valid on after the move has been completed.
     * @return The round statistics for the one that just terminated.
     */
    public RoundStatistics getRoundStatistics() {
        return new RoundStatistics(this.progress, this.statCandiesRemoved, this.statCandiesFormed, this.statProcess);
    }

    /**
     * Function that checks whether the game has ended. Note: this can only be called if a level design
     * was specified when constructing the game.
     * @return
     */
    public boolean isGameOver() {
        return progress.isGameOver(design);
    }

    /**
     * Function that checks whether the game was won. Note: this can only be called if a level design
     * was specified when constructing the game.
     * @return
     */
    public boolean isGameWon() {
        return progress.isGameWon(design);
    }

    public boolean didFailShuffle() {
        return progress.didFailShuffle();
    }

    // **** GETTER METHODS END ****

    // TODO: Remove this function.
    public Design getLevelDesign() {
        return levelDesign;
    }

    /**
     * If the move is valid, it performs all steps until completion.
     * 
     * @param move
     *            the move to be performed.
     * @throws InvalidMoveException
     *             in case it is not a legitimate move.
     */
    public void makeFullMove(Move move) throws InvalidMoveException {
        makeInitialMove(move);
        while (makeSmallMove())
            ;
    }

    /**
     * Function that initiates a move, meaning that it makes the swap and
     * transitions to the first state.
     * 
     * @param move
     *            Move that has to be made.
     * @throws InvalidMoveException
     *             in case it is not a legitimate move.
     */
    public void makeInitialMove(Move move) throws InvalidMoveException {
        if (!isMoveValid(move))
            throw new InvalidMoveException(move);
        // Record the last move.

        resetRound();
        lastMove = move;

        this.statProcess.setCandiesSwapped(getCell(move.p1).getCandy(), getCell(move.p2).getCandy());

        swapCandies(move);

        // Reduce the number of remaining moves available
        progress.decreaseMovesRemaining();

        Position p1 = move.p1, p2 = move.p2;
        Cell cell1 = getCell(p1), cell2 = getCell(p2);

        // Just enumerating possible combinations.
        if (hasColourBomb(cell1) && hasColourBomb(cell2)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateBombBomb();
        } else if (hasColourBomb(cell1) && hasSpecial(cell2)) {
            cell1.removeCandy();
            replaceWithSpecialAllOf(cell2.getCandy().getColour(), cell2.getCandy().getCandyType());
        } else if (hasColourBomb(cell2) && hasSpecial(cell1)) {
            cell2.removeCandy();
            replaceWithSpecialAllOf(cell1.getCandy().getColour(), cell1.getCandy().getCandyType());
        } else if (hasColourBomb(cell1) && hasNormal(cell2)) {
            cell1.removeCandy();
            breakAllOf(cell2.getCandy().getColour());
        } else if (hasColourBomb(cell2) && hasNormal(cell1)) {
            cell2.removeCandy();
            breakAllOf(cell1.getCandy().getColour());
        } else if (hasHorizontallyStripped(cell1) && hasVerticallyStripped(cell2)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateHorizontallyStripped(p1);
            detonateVerticallyStripped(p2);
        } else if (hasVerticallyStripped(cell1) && hasHorizontallyStripped(cell2)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateVerticallyStripped(p1);
            detonateHorizontallyStripped(p2);
        } else if (hasStripped(cell1) && hasStripped(cell2)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateVerticallyStripped(p1);
            detonateHorizontallyStripped(p2);
        } else if (hasWrapped(cell1) && hasStripped(cell2)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateWrappedStripped(p1);
        } else if (hasWrapped(cell2) && hasStripped(cell1)) {
            cell1.removeCandy();
            cell2.removeCandy();
            detonateWrappedStripped(p2);
        } else if (hasWrapped(cell1) && hasWrapped(cell2)) {
            detonateWrappedWrapped(p1, p2);
        } else {
            makeSmallMove();
        }
    }

    /**
     * Once the makeInitialMove has been called this takes care of making the
     * small steps in the boards.
     * 
     * @return whether there is no other step in to be made.
     */
    public boolean makeSmallMove() {
        this.statProcess.incrementTransitions();
        DebugFilter.println("Current state is " + this.currentProcessState, DebugFilterKey.GAME_IMPLEMENTATION);
        switch (currentProcessState) {
        case AWAITING_MOVE:
            currentProcessState = ProcessState.MATCH_AND_REPLACE;
            break;
        case MATCH_AND_REPLACE:
            markAndReplaceMatchingTiles();
            lastMove = null;
            if (!wasSomethingPopped)
                currentProcessState = ProcessState.SHUFFLE;
            else
                currentProcessState = ProcessState.DETONATE_PENDING;
            break;
        case SHUFFLE:
            if (!candiesNeededShuffling()) {
                currentProcessState = ProcessState.AWAITING_MOVE;
                // Move has ended.
                this.statProcess.setValidMoves(this.getValidMoves().size());
                return false;
            } else {
                wasSomethingPopped = false;
                currentProcessState = ProcessState.MATCH_AND_REPLACE;
            }
            break;
        case DETONATE_PENDING:
            findDetonated();
            detonateAllPending();
            currentProcessState = ProcessState.BRING_DOWN_CANDIES;
            break;
        case BRING_DOWN_CANDIES:
            bringDownCandies();
            currentProcessState = ProcessState.PASSING_INGREDIENTS;
            break;
        case PASSING_INGREDIENTS:
            if (passIngredients())
                currentProcessState = ProcessState.BRING_DOWN_CANDIES;
            else
                currentProcessState = ProcessState.FILL_BOARD;
            break;
        case FILL_BOARD:
            fillBoard();
            if (hasDetonated(board))
                currentProcessState = ProcessState.DETONATE_PENDING;
            else {
                wasSomethingPopped = false;
                currentProcessState = ProcessState.MATCH_AND_REPLACE;
            }
            break;
        }
        return true;
    }

    /**
     * Function that returns the detailed MatchAnalysis of making a move for
     * each match that is immediately formed. Note: there are at most two such
     * MatchAnalysis if the game state was in normal state.
     * 
     * @param move
     *            the move to be performed.
     * @return a list of the MatchAnalysis.
     */
    public List<MatchAnalysis> getMatchAnalysis(Move move) {
        // Make move in order to get information.
        swapCandies(move);
        List<MatchAnalysis> ret = new ArrayList<MatchAnalysis>();
        MatchAnalysis analysis1 = getSingleMatchAnalysis(board, move.p1);
        MatchAnalysis analysis2 = getSingleMatchAnalysis(board, move.p2);
        // Add only the analyses that produced an outcome.
        if (analysis1 != null)
            ret.add(analysis1);
        if (analysis2 != null)
            ret.add(analysis2);
        // reverse the operation.
        swapCandies(move);
        return ret;
    }

    /**
     * Function that checks whether a certain move is valid.
     * 
     * @param move
     *            the move that you want to perform
     * @return whether the move is valid.
     */
    public boolean isMoveValid(Move move) {
        if (move == null) return false;
        if (!isPositionValidAndMoveable(move.p1) || !isPositionValidAndMoveable(move.p2))
            return false;
        // Check move is for adjacent positions.
        if (Math.abs(move.p1.x - move.p2.x) + Math.abs(move.p1.y - move.p2.y) != 1)
            return false;
        Cell cell1 = getCell(move.p1), cell2 = getCell(move.p2);

        if (hasSpecial(cell1) && hasSpecial(cell2))
            return true;

        // Exchanging a Bomb with a cell that has a movable item is a valid
        // move (i.e. it is either special or normal candy type).
        else if (hasColourBomb(cell1) && (hasSpecial(cell2) || hasNormal(cell2) || hasColourBomb(cell2))
                || hasColourBomb(cell2) && (hasSpecial(cell1) || hasNormal(cell1) || hasColourBomb(cell1)))
            return true;

        swapCandies(move);
        boolean isValid = cellFormsMatch(board, move.p1) || cellFormsMatch(board, move.p2);
        // Place candies as they were initially.
        swapCandies(move);
        return isValid;
    }

    /**
     * Function that returns the valid moves in this turn. Note that the game
     * state should be in stable mode in order to get moves that can actually be
     * played.
     * 
     * @return list of unique valid moves on the current state of the board.
     */
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

    // **** FUNCIONS THAT MONITOR PROGRESS ****
    private void incrementScore(int addedScore) {
        progress.incrementScore(addedScore);
    }

    private void decreaseRemainingIngredients() {
        incrementScore(Scoring.BROUGHT_INGREDIENT_DOWN);
        progress.decreaseIngredientsRemaining();
        this.statProcess.incrementPassedIngredients();
    }

    @Override
    public boolean equals(Object toCompare) {
        GameState gameStateToCompare = (GameState) toCompare;
        boolean isEqual = true;

        // Check the basic parameters match
        isEqual &= (this.height == gameStateToCompare.height) && (this.width == gameStateToCompare.width)
                && (this.progress == gameStateToCompare.progress);
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

    // Function that adds the tile to detonated (the ones that are going to
    // break on the next state).
    // If the cell contains a normal candy then amount will be added to score.
    private void trigger(int x, int y, int amount) {
        if (!inBoard(new Position(x, y)))
            return;
        Cell current = board[x][y];
        if (current.getCellType().equals(CellType.UNUSABLE))
            return;

        touchNeighbours(x, y);
        this.statCandiesRemoved.candyProcessed(current.getCandy());
        // There is no additional score for LIQUORICE lock.
        if (current.getCellType().equals(CellType.LIQUORICE)) {
            current.setCellType(CellType.NORMAL);
            // Should not remove any jelly layer if the cell type is liquorice.
            return;
        }
        if (current.removeJellyLayer()) {
            incrementScore(Scoring.MATCHED_A_JELLY);
            progress.decreaseJelliesRemaining();
        }

        if (current.hasCandy() && current.getCandy().getCandyType().equals(CandyType.INGREDIENT))
            return;

        if (current.hasCandy() && current.getCandy().isDetonated())
            return;
        if (current.hasCandy() && current.getCandy().getCandyType().isSpecial()) {
            if (current.getCandy().getCandyType().equals(CandyType.BOMB)) {
                current.removeCandy();
                wasSomethingPopped = true;
                return;
            }
            if (!current.getCandy().isDetonated()) {
                detonated.add(new Position(x, y));
                current.getCandy().setDetonated();
                if (current.getCandy().getCandyType().equals(CandyType.WRAPPED)) {
                    current.getCandy().setWrappedRadius(GameConstants.SINGLE_WRAPPED_RADIUS);
                }
                wasSomethingPopped = true;
            }
        } else if (current.hasCandy()) {
            current.removeCandy();
            incrementScore(amount);
            wasSomethingPopped = true;
        }
    }

    // Function that executes what should happen to board[x][y] when a
    // neighbouring
    // cell was triggered.

    private void touch(int x, int y) {
        if (!inBoard(new Position(x, y)))
            return;
        Cell current = board[x][y];
        if (current.getCellType().equals(CellType.ICING)) {
            incrementScore(Scoring.ICING_CLEARED);
            this.statProcess.incrementIcing();
            current.setCellType(CellType.EMPTY);
        }
    }

    // Function that touches the neighbours of a triggered cell.
    private void touchNeighbours(int x, int y) {
        if (!inBoard(new Position(x, y)))
            return;
        touch(x + 1, y);
        touch(x - 1, y);
        touch(x, y + 1);
        touch(x, y - 1);
    }

    // Functions for monitoring formation of candies on the board.
    private void matched3() {
        incrementScore(++numberOfMatchedInRound * Scoring.MATCHED_3);
    }

    private void matchedStripped(int x, int y, CandyColour clr, boolean isVertical) {
        incrementScore((++numberOfMatchedInRound - 1) * Scoring.MATCHED_4);
        makeStripped(x, y, clr, isVertical);
    }

    private void matchedWrapped(int x, int y, CandyColour clr) {
        incrementScore((++numberOfMatchedInRound - 1) * Scoring.MADE_WRAPPED_CANDY);
        makeWrapped(x, y, clr);
    }

    private void matchedBomb(int x, int y) {
        incrementScore((++numberOfMatchedInRound - 1) * Scoring.MADE_BOMB);
        makeCellBomb(x, y);
    }

    private void makeCellBomb(int x, int y) {
        incrementScore(Scoring.MADE_BOMB);
        board[x][y].setCandy(new Candy(null, CandyType.BOMB));
        this.statCandiesFormed.candyProcessed(board[x][y].getCandy());
    }

    private void makeWrapped(int x, int y, CandyColour clr) {
        incrementScore(Scoring.MADE_WRAPPED_CANDY);
        board[x][y].setCandy(new Candy(clr, CandyType.WRAPPED));
        this.statCandiesFormed.candyProcessed(board[x][y].getCandy());
    }

    private void makeStripped(int x, int y, CandyColour clr, boolean isVertical) {
        incrementScore(Scoring.MADE_STRIPPED_CANDY);
        board[x][y]
                .setCandy(new Candy(clr, isVertical ? CandyType.VERTICALLY_STRIPPED : CandyType.HORIZONTALLY_STRIPPED));
        this.statCandiesFormed.candyProcessed(board[x][y].getCandy());
    }

    // **** MARK AND REPLACE STAGE ****

    // Function that replaces all the matched tiles with their respective
    // Candy (either empty or special is some cases).
    private void markAndReplaceMatchingTiles() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // Do not consider cells with no candy.
                if (!board[x][y].hasCandy())
                    continue;
                markAndReplaceForTile(x, y);
            }
        }
    }

    private void markAndReplaceForTileHorizontally(SingleTileAnalysis analysis, CandyColour colour, int x, int y) {
        boolean foundVertical = false;
        // If it forms a bomb there is no need to place wrapped candy.
        if (analysis.getLengthX() >= 5)
            foundVertical = true;

        // Iterate over all candies that match horizontally.
        for (int k = analysis.startX; k <= analysis.endX; ++k) {
            SingleTileAnalysis childAnalysis = analyzeTile(new Position(k, y), board);
            if (childAnalysis.getLengthY() > 2) {
                // This candy will be replaced by a wrapped one if
                // it is the first junction.
                if (!foundVertical) {
                    foundVertical = true;
                    matchedWrapped(k, y, colour);
                } else {
                    // Otherwise just empty that candy.
                    trigger(k, y, Scoring.NO_ADDITIONAL_SCORE);
                }
                // Iterate through the vertical column and trigger
                // them.
                for (int yy = childAnalysis.startY; yy <= childAnalysis.endY; ++yy) {
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
        if (analysis.getLengthX() == 3 && !foundVertical) {
            matched3();
            return;
        }

        else if (!foundVertical) {

            // If last move is in the range we went through,
            // then make that one the stripped candy.
            if (lastMove == null || !moveInHorizontalRange(lastMove, analysis.startX, analysis.endX)) {
                // Make the middle candy vertically stripped.
                matchedStripped(x + 1, y, colour, VERTICAL);
            } else {
                int coordinate;
                // If one of the positions has the same
                // y-coordinate.
                if (lastMove.p1.y == y)
                    coordinate = lastMove.p1.x;
                else
                    coordinate = lastMove.p2.x;
                matchedStripped(coordinate, y, colour, VERTICAL);
            }

        } else {
            // The bomb created will be aligned with the first move
            // since it will always be the middle one.
            if (analysis.getLengthX() >= 5) {
                matchedBomb(x + 2, y);
            }
        }
    }

    private void markAndReplaceForTileVertically(SingleTileAnalysis analysis, CandyColour colour, int x, int y) {
        boolean foundHorizontal = false;
        // If it forms a bomb there is no need to place wrapped.
        if (analysis.getLengthY() >= 5)
            foundHorizontal = true;
        for (int k = analysis.startY; k <= analysis.endY; ++k) {
            SingleTileAnalysis childAnalysis = analyzeTile(new Position(x, k), board);
            if (childAnalysis.getLengthX() > 2) {
                // This candy will be replaced by a wrapped one if
                // it is the first junction.
                if (!foundHorizontal) {
                    foundHorizontal = true;
                    matchedWrapped(x, k, colour);
                } else {
                    // Otherwise just empty that candy.
                    trigger(x, k, Scoring.NO_ADDITIONAL_SCORE);
                }
                // Iterate through the vertical column and trigger
                // them.
                for (int xx = childAnalysis.startX; xx <= childAnalysis.endX; ++xx) {
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
        if (analysis.getLengthY() == 3 && !foundHorizontal) {
            matched3();
            return;
        } else if (!foundHorizontal) {
            // If last move is in the range we went through,
            // then make that one the stripped candy.
            if (lastMove == null || !moveInVerticalRange(lastMove, analysis.startY, analysis.endY)) {
                // Make the middle candy vertically stripped.
                matchedStripped(x, y + 1, colour, HORIZONTAL);
            } else {
                int coordinate;
                // If one of the positions has the same
                // x-coordinate.
                if (lastMove.p1.x == x)
                    coordinate = lastMove.p1.y;
                else
                    coordinate = lastMove.p2.y;
                matchedStripped(x, coordinate, colour, HORIZONTAL);
            }
        } else {
            if (analysis.getLengthY() >= 5) {
                matchedBomb(x, y + 2);
            }
        }
    }

    private void markAndReplaceForTile(int x, int y) {
        CandyColour colour = board[x][y].getCandy().getColour();
        SingleTileAnalysis analysis = analyzeTile(new Position(x, y), board);

        // In case there is a horizontal match.
        if (analysis.formsHorizontalMatch()) {
            markAndReplaceForTileHorizontally(analysis, colour, x, y);
        }
        // In case there
        else if (analysis.formsVerticalMatch()) {
            markAndReplaceForTileVertically(analysis, colour, x, y);
        }

    }

    // **** DETONATION FUNCTIONS ****

    // Function that triggers all cells in the cross around position.
    private void detonateWrappedWrapped(Position p1, Position p2) {
        incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        Cell cell1 = getCell(p1), cell2 = getCell(p2);
        cell1.getCandy().setWrappedRadius(GameConstants.DOUBLE_WRAPPED_RADIUS_A);
        cell2.getCandy().setWrappedRadius(GameConstants.DOUBLE_WRAPPED_RADIUS_B);
        cell1.getCandy().setDetonated();
        cell2.getCandy().setDetonated();
        cell1.getCandy().decreaseDetonations();
        cell2.getCandy().decreaseDetonations();
        detonateWrapped(p1);
        detonateWrapped(p2);
    }

    // Function that performs the operation for combining two bombs.
    // This is triggering the whole board, but without clearing any icing.
    private void detonateBombBomb() {
        incrementScore(Scoring.DETONATE_BOMB);
        incrementScore(Scoring.DETONATE_BOMB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                trigger(x, y, Scoring.BOMB_INDIVIDUAL);
            }
        }
    }

    // Function that will detonate a wrapped candy.
    private void detonateWrapped(Position wrapped) {
        incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        int rad = getCell(wrapped).getCandy().getWrappedRadius();
        for (int i = wrapped.x - rad; i <= wrapped.x + rad; ++i) {
            for (int j = wrapped.y - rad; j <= wrapped.y + rad; ++j) {
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
            Cell current = getCell(new Position(vStripped.x, y));
            if (hasVerticallyStripped(current) && !current.getCandy().isDetonated()) {
                current.removeCandy();
                continue;
            }
            trigger(vStripped.x, y, Scoring.STRIPPED_INDIVIDUAL);
        }
    }

    // Function that performs the clearing of the horizontal line for the
    // stripped candy.
    private void detonateHorizontallyStripped(Position hStripped) {
        incrementScore(Scoring.DETONATE_STRIPPED_CANDY);
        for (int x = 0; x < width; ++x) {
            Cell current = getCell(new Position(x, hStripped.y));
            if (hasHorizontallyStripped(current) && !current.getCandy().isDetonated()) {
                current.removeCandy();
                continue;
            }
            trigger(x, hStripped.y, Scoring.STRIPPED_INDIVIDUAL);
        }
    }

    // Function that creates a cross of width 3 around the locations that were
    // swapped and triggers all cells inside there.
    private void detonateWrappedStripped(Position pos) {
        incrementScore(Scoring.DETONATE_WRAPPED_CANDY);
        incrementScore(Scoring.DETONATE_STRIPPED_CANDY);
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
            if (!detonatingCell.hasCandy())
                continue;
            if (detonatingCell.getCandy().getCandyType() == null)
                detonatingCell.removeCandy();
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
            } else {
                detonatingCell.removeCandy();
            }
        }
    }

    // Brings candies down.
    private void bringDownCandies() {
        passIngredients();
        detonated = new ArrayList<Position>();
        int[] prev = new int[width];
        for (int i = 0; i < width; ++i)
            prev[i] = height;

        for (int i = 0; i < width; ++i) {
            for (int j = height - 1; j >= 1; --j) {
                if (board[i][j].getCellType().equals(CellType.EMPTY) && !board[i][j].blocksCandies()) {
                    int y = Math.min(prev[i], j - 1);
                    while (y >= 0 && !board[i][y].canDropCandy()) {
                        if (board[i][y].blocksCandies()) {
                            prev[i] = y;
                            break;
                        }
                        y--;
                    }

                    if (y >= 0 && board[i][y].blocksCandies())
                        break;

                    // Replacement was found.
                    if (y >= 0) {
                        board[i][j].setCandy(board[i][y].getCandy());
                        board[i][y].removeCandy();
                    }

                }
            }
        }
    }

    // Function that fills the board by requesting candies from the
    // candyGenerator.
    private void fillBoard() {
        int[] prev = new int[width];
        for (int i = 0; i < width; ++i) prev[i] = height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = board[x][y];
                if (cell.isFillable()) {

                    int cur_y = Math.min(y - 1, prev[x]);
                    while (cur_y >= 0 && !board[x][cur_y].blocksCandies()) {
                        cur_y--;
                    }
                    prev[x] = cur_y;
                    if (cur_y < 0)
                        cell.setCandy(candyGenerator.generateCandy(x));
                }
            }
        }
    }

    // **** FUNCTIONS THAT CONCERN INGREDIENTS ****

    // Function that records all cells that are ingredient sinks.
    private void recordIngredientSinks() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (board[x][y].isIngredientSink())
                    ingredientSinkPositions.add(new Position(x, y));
            }
        }
    }

    // Function that passes ingredients through the sink.
    private boolean passIngredients() {
        boolean passedIngredient = false;
        for (Position ingredientSink : ingredientSinkPositions) {
            if (hasIngredient(getCell(ingredientSink))) {
                Cell cell = getCell(ingredientSink);
                cell.setCellType(CellType.EMPTY);
                decreaseRemainingIngredients();
                passedIngredient = true;
            }
        }
        return passedIngredient;
    }

    private boolean inBoard(Position pos) {
        return inRange(pos.x, 0, width - 1) && inRange(pos.y, 0, height - 1);
    }

    private boolean isPositionValidAndMoveable(Position pos) {
        return inBoard(pos) && board[pos.x][pos.y].isMovable();
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

    // Function that performs the combination of a bomb and a Normal Candy.
    private void breakAllOf(CandyColour colour) {
    	incrementScore(Scoring.DETONATE_BOMB);
    	this.statCandiesRemoved.candyProcessed(COLOUR_BOMB);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (sameColourWithCell(board[i][j], colour))
                    trigger(i, j, Scoring.BOMB_INDIVIDUAL);
            }
        }
    }

    // Function that performs the combination of a bomb and a Special Candy.
    private void replaceWithSpecialAllOf(CandyColour colourMatched, CandyType typeToReplace) {
        incrementScore(Scoring.DETONATE_BOMB);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (sameColourWithCell(board[i][j], colourMatched)) {
                    if (!board[i][j].getCandy().getCandyType().isSpecial()) {
                        if (typeToReplace.equals(CandyType.HORIZONTALLY_STRIPPED)
                                || typeToReplace.equals(CandyType.VERTICALLY_STRIPPED)) {
                            if ((i % 2) != (j % 2))
                                makeStripped(i, j, colourMatched, HORIZONTAL);
                            else
                                makeStripped(i, j, colourMatched, VERTICAL);
                        } else if (typeToReplace.equals(CandyType.WRAPPED)) {
                            makeWrapped(i, j, colourMatched);
                        }
                    }
                    trigger(i, j, Scoring.NO_ADDITIONAL_SCORE);
                }
            }
        }
    }

    public boolean hasMoves() {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (isMoveValid(new Move(new Position(i, j), new Position(i+1, j)))) return true;
                if (isMoveValid(new Move(new Position(i, j), new Position(i, j+1)))) return true;
            }
        }
        return false;
    }
    
    // TODO: Handle case in which no amount of shuffling can introduce a
    // possible move - i.e. we have need some
    // concept of "GAME OVER"
    private boolean candiesNeededShuffling() {
        if (!this.shouldShuffle)
            return false;
        boolean didShuffle = false;

        // It is quite complicated (and expensive to compute) whether there
        // exists a shuffle which introduces a possible move, so for now I think
        // we should just shuffle up to some limit, at which point we declare
        // that the game is over
        boolean hasMoves;
        int shuffleLimit = 5;
        int shuffleCount = 0;

        // While there are no available moves, we need to shuffle the normal
        // (non-special) candies on the board
        while (!(hasMoves = this.hasMoves()) && shuffleCount < shuffleLimit) {

            DebugFilter.println("No moves available: Shuffling candies...", DebugFilterKey.GAME_IMPLEMENTATION);

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

            // Shuffle the colours - the reason I'm shuffling colours and not
            // candies is so that jelly blocks aren't
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
            this.statProcess.setShuffled(true);
            shuffleCount++;
        }

        if (!hasMoves) {
            progress.setDidFailShuffle();
            return false;
        }

        return didShuffle;
    }
    
    private void findDetonated() {
        detonated = new ArrayList<Position>();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (board[i][j].hasCandy() && board[i][j].getCandy().isDetonated())
                    this.detonated.add(new Position(i, j));
            }
        }
    }

    private void resetRound() {
        // Reset the combo multiplier.
        this.numberOfMatchedInRound = 0;
        this.statCandiesFormed.reset();
        this.statCandiesRemoved.reset();
        this.statProcess.reset();
    }

    @Override
    public String toString() {
    	return boardToString(this.board);
    }
}
