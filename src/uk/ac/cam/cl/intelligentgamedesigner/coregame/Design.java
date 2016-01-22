package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

public class Design implements Serializable {

    private char[][] boardLayout;       // The layout - specifying the position of special candies and blocks
    private int numberOfMoves;          // The number of moves the player has available
    // TODO: Add a level-type member
}