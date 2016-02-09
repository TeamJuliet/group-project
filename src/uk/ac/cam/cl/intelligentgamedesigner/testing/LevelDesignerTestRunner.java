package uk.ac.cam.cl.intelligentgamedesigner.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sun.security.krb5.internal.crypto.Des;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.ArrayLevelRepresentationJelly;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.ArrayLevelRepresentationScore;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.LevelRepresentation;

import java.util.Random;

public class LevelDesignerTestRunner {

    /*
    INSERT UNIT TESTS FOR THE LEVEL DESIGNER BELOW BY DOING THE FOLLOWING:
    ----------------------------------------------------------------------

    1. Write the test in a public void method.
    2. Annotate the method with @Test

    I've added an example below.
     */

    @Test
    public void crossoverTest1 () {
        LevelRepresentation mother = new ArrayLevelRepresentationScore(new Random());
        LevelRepresentation father = new ArrayLevelRepresentationScore(new Random());

        LevelRepresentation child1 = mother.clone();
        LevelRepresentation child2 = father.clone();

        child1.crossoverWith(child2);

        boolean correct = true;

        Cell[][] motherDesignBoard = mother.getDesign().getBoard();
        Cell[][] fatherDesignBoard = father.getDesign().getBoard();
        Cell[][] child1DesignBoard = child1.getDesign().getBoard();
        Cell[][] child2DesignBoard = child2.getDesign().getBoard();

        for (int x = 0; x < child1.getDesign().getWidth(); x++) {
            for (int y = 0; y < child1.getDesign().getHeight(); y++) {
                if (child1DesignBoard[x][y].equals(motherDesignBoard[x][y])) {
                    correct &= child2DesignBoard[x][y].equals(fatherDesignBoard[x][y]);
                } else {
                    if (child1DesignBoard[x][y].equals(fatherDesignBoard[x][y])) {
                        correct &= child2DesignBoard[x][y].equals(motherDesignBoard[x][y]);
                    } else {
                        correct &= false;
                    }
                }
            }
        }

        assertTrue(correct);
    }
}
