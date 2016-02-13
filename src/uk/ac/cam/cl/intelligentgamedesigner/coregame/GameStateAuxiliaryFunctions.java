package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.analyzeTile;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.getSpecialCandyFormed;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasHorizontallyStripped;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasSpecial;
import static uk.ac.cam.cl.intelligentgamedesigner.coregame.GameStateAuxiliaryFunctions.hasVerticallyStripped;

import java.util.LinkedList;
import java.util.List;

public class GameStateAuxiliaryFunctions {
    public static final boolean VERTICAL   = true;
    public static final boolean HORIZONTAL = false;
    
    
    /**
     * Function that is used to determine what special candy will be formed by a certain pattern.
     * @param analysis the single tile analysis of a cell
     * @return the candyType that should be formed by this match.
     */
    public static CandyType getSpecialCandyFormed(SingleTileAnalysis analysis) {
        if (analysis.getLengthX() <= 3 && analysis.getLengthY() < 3
                || analysis.getLengthX() < 3 && analysis.getLengthY() <= 3)
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
    
    public static boolean sameColourWithCell(Cell c, CandyColour colour) {
        if (c.getCandy() == null || c.getCandy().getColour() == null)
            return false;
        return c.getCandy().getColour().equals(colour);
    }
    
    public static SingleTileAnalysis analyzeTile(Position pos, Cell[][] board) {
        int x = pos.x, y = pos.y;
        CandyColour cellColour = board[x][y].getCandy().getColour();
        int start_x = x - 1, end_x = x + 1, start_y = y - 1, end_y = y + 1;
        while (start_x >= 0 && sameColourWithCell(board[start_x][y], cellColour))
            --start_x;
        while (end_x < board.length && sameColourWithCell(board[end_x][y], cellColour))
            ++end_x;
        while (start_y >= 0 && sameColourWithCell(board[x][start_y], cellColour))
            --start_y;
        while (end_y < board[0].length && sameColourWithCell(board[x][end_y], cellColour))
            ++end_y;
        return new SingleTileAnalysis(start_x + 1, end_x - 1, start_y + 1, end_y - 1);
    }
    
    // Check if any of the two positions is in the horizontal range.
    public static boolean moveInHorizontalRange(Move move, int startX, int endX) {
        return inRange(move.p1.x, startX, endX) || inRange(move.p2.x, startX, endX);
    }

    // Check if any of the two positions is in the vertical range.
    public static boolean moveInVerticalRange(Move move, int startY, int endY) {
        return inRange(move.p1.y, startY, endY) || inRange(move.p2.y, startY, endY);
    }
    
    // Auxiliary function to check a <= x <= b
    public static boolean inRange(int x, int a, int b) {
        return x >= a && x <= b;
    }
    
    public static boolean hasIngredient(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.INGREDIENT);
    }
    
    // Function that checks whether the position contains a special candy.
    public static boolean hasSpecial(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().isSpecial();
    }

    // Function that checks whether the position contains a bomb.
    public static boolean hasBomb(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.BOMB);
    }

    // Function that checks whether the position contains a vertically stripped.
    public static boolean hasVerticallyStripped(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.VERTICALLY_STRIPPED);
    }

    // Function that checks whether the position contains a horizontally
    // stripped.
    public static boolean hasHorizontallyStripped(Cell cell) {
        return cell.hasCandy()
                && cell.getCandy().getCandyType().equals(CandyType.HORIZONTALLY_STRIPPED);
    }
    
    // Function that checks whether the position contains a wrapped candy.
    public static boolean hasWrapped(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.WRAPPED);
    }

    public static boolean hasStripped(Cell cell) {
        return hasVerticallyStripped(cell) || hasHorizontallyStripped(cell);
    }
    
    public static boolean hasNormal(Cell cell) {
        return cell.hasCandy() && cell.getCandy().getCandyType().equals(CandyType.NORMAL);
    }
    
    // Function that returns whether the tile at position forms a match.
    public static boolean tileFormsMatch(Cell[][] board, Position position) {
        SingleTileAnalysis analysis = analyzeTile(position, board);
        return analysis.getLengthX() > 2 || analysis.getLengthY() > 2;
    }
    
    // Function that creates the match analysis for a single tile.
    public static MatchAnalysis getSingleMatchAnalysis(Cell[][] board, Position pos) {
        if (tileFormsMatch(board, pos))
            return null;
        SingleTileAnalysis analysis = analyzeTile(pos, board);
        List<Position> positions = new LinkedList<Position>();
        List<CandyType> specials = new LinkedList<CandyType>();
        if (analysis.getLengthX() > 2) {
            for (int x = analysis.start_x; x <= analysis.end_x; ++x) {
                if (x == pos.x)
                    continue;
                Position currentPosition = new Position(x, pos.y);
                positions.add(currentPosition);
                if (hasSpecial(board[x][pos.y]))
                    specials.add(board[x][pos.y].getCandy().getCandyType());
            }
        }
        if (analysis.getLengthY() > 2) {
            for (int y = analysis.start_y; y <= analysis.end_y; ++y) {
                if (y == pos.y)
                    continue;
                Position currentPosition = new Position(pos.x, y);
                positions.add(currentPosition);
                if (hasSpecial(board[pos.x][y]))
                    specials.add(board[pos.x][y].getCandy().getCandyType());
            }
        }
        return new MatchAnalysis(positions, specials, getSpecialCandyFormed(analysis));
    }
    
}
