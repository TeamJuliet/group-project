package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

//The MIN and MAX describe the inclusive
//range in which all int values are mapped to a Cell Type.

static final int MIN_DESIGN_CELL_TYPE_ENUM = 0;

public enum DesignCellType
{
    UNUSABLE = minEnum,
    EMPTY,
    ICING,
    LIQUORICE
}

static final int MAX_DESIGN_CELL_TYPE_ENUM = (int)LIQUORICE;