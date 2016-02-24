package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Cell;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.CellType;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class SimulatedPlayersTestHelpers {

    public static final int FIVE_MOVES_AVAILABLE = 5;
    public static final int TWENTY_MOVES_AVAILABLE = 20;
    
    public static final int MINIMUM_TARGET_SCORE = 1;
    public static final int INFINITE_TARGET_SCORE = Integer.MAX_VALUE;
    public static final int SIX_CANDY_COLOURS = 6;
    
    public static Design getPlainBoardDesign() {
        int sizeX = 10, sizeY = 10;
        Design design = new Design();
        Cell[][] boardLayout = new Cell[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                boardLayout[i][j] = new Cell(CellType.EMPTY);
            }
        }
        design.setBoard(boardLayout);
        return design;
    }
    
    public static Design getBoardWithBlockersDesign() {
        int sizeX = 10, sizeY = 10;
        Design design = new Design();
        Cell[][] boardLayout = new Cell[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                boardLayout[i][j] = new Cell(CellType.EMPTY);
            }
        }
        
        // Add three cells with icing.
        boardLayout[0][0] = new Cell(CellType.ICING);
        boardLayout[0][1] = new Cell(CellType.ICING);
        boardLayout[0][2] = new Cell(CellType.ICING);
        
        // Add three cells with liquorice locks.
        boardLayout[9][2] = new Cell(CellType.ICING);
        boardLayout[8][2] = new Cell(CellType.ICING);
        boardLayout[7][2] = new Cell(CellType.ICING);
        
        design.setBoard(boardLayout);
        return design;
    }
    
    public static Design getBoardWithBlockersAndJelliesDesign() {
        int sizeX = 10, sizeY = 10;
        Design design = new Design();
        Cell[][] boardLayout = new Cell[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                boardLayout[i][j] = new Cell(CellType.EMPTY);
            }
        }
        
        for (int i = 4; i < 7; ++i) {
            for (int j = 4; j < 7; ++j) {
                boardLayout[i][j].setJellyLevel((i + j) % 2 + 1);
            }
        }
        
        // Add three cells with icing.
        boardLayout[0][0] = new Cell(CellType.ICING);
        boardLayout[0][1] = new Cell(CellType.ICING);
        boardLayout[0][2] = new Cell(CellType.ICING);
        
        // Add three cells with liquorice locks.
        boardLayout[9][2] = new Cell(CellType.ICING);
        boardLayout[8][2] = new Cell(CellType.ICING);
        boardLayout[7][2] = new Cell(CellType.ICING);
        
        design.setBoard(boardLayout);
        return design;
    }

}
