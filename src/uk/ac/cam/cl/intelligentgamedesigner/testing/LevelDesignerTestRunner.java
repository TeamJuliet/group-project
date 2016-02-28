package uk.ac.cam.cl.intelligentgamedesigner.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.*;
import uk.ac.cam.cl.intelligentgamedesigner.leveldesigner.*;

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
    public void cloneTestScoreRepresentation () {
        assertTrue(checkClone(GameMode.HIGHSCORE));
    }

    @Test
    public void cloneTestJellyRepresentation () {
        assertTrue(checkClone(GameMode.JELLY));
    }

    @Test
    public void cloneTestIngredientsRepresentation () {
        assertTrue(checkClone(GameMode.INGREDIENTS));
    }

    private boolean checkClone (GameMode gameMode) {
        LevelRepresentation l1 = getLevelRepresentation(gameMode);
        LevelRepresentation l2 = l1.clone();

        Design l1Design1 = l1.getDesign();
        Design l2Design1 = l2.getDesign();

        boolean correct = (l1Design1.equals(l2Design1));

        l1.mutate();

        Design l1Design2 = l1.getDesign();

        correct &= !(l1Design2.equals(l2Design1));

        return correct;
    }

    @Test
    public void crossoverTest1 () {
        LevelRepresentation mother = new ArrayLevelRepresentationScore(getParameters());
        LevelRepresentation father = new ArrayLevelRepresentationScore(getParameters());

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

    @Test
    public void mutationTestScoreRepresentation () {
        boolean correct = true;
        for (int i = 0; i < 50; i++) {
            correct &= (checkNumberOfMutationChanges(GameMode.HIGHSCORE) == 1);
        }
        assertTrue(correct);
    }

    @Test
    public void mutationTestJellyRepresentation () {
        boolean correct = true;
        for (int i = 0; i < 50; i++) {
            int numChanges = checkNumberOfMutationChanges(GameMode.JELLY);
            correct &= (numChanges == 1 || numChanges == 2);
        }
        assertTrue(correct);
    }

    @Test
    public void mutationTestIngredientsRepresentation () {
        boolean correct = true;
        for (int i = 0; i < 50; i++) {
            correct &= checkNumberOfMutationChanges(GameMode.INGREDIENTS) == 1;
        }
        assertTrue(correct);
    }

    private int checkNumberOfMutationChanges (GameMode gameMode) {
        LevelRepresentation l1 = getLevelRepresentation(gameMode);
        LevelRepresentation l0 = l1.clone();
        l1.mutate();

        // Check that exactly 1 thing changed
        int numChanged = 0;

        Design l0Design = l0.getDesign();
        Design l1Design = l1.getDesign();

        // Check the board cells
        for (int x = 0; x < l0Design.getWidth(); x++) {
            for (int y = 0; y < l0Design.getHeight(); y++) {
                if (!l0Design.getCell(x, y).equals(l1Design.getCell(x, y))) {
                    numChanged++;
                }
            }
        }

        // Check the parameters
        if (l0Design.getNumberOfMovesAvailable() != l1Design.getNumberOfMovesAvailable()) numChanged++;
        if (l0Design.getNumberOfCandyColours() != l1Design.getNumberOfCandyColours())  numChanged++;
        if (l0Design.getObjectiveTarget() != l1Design.getObjectiveTarget()) numChanged++;

        return numChanged;
    }

    private LevelRepresentation getLevelRepresentation (GameMode gameMode) {
        switch (gameMode) {
            case HIGHSCORE:
                return new ArrayLevelRepresentationScore(getParameters());
            case JELLY:
                return new ArrayLevelRepresentationJelly(getParameters());
            default:
                return new ArrayLevelRepresentationIngredients(getParameters());
        }
    }

    private LevelRepresentationParameters getParameters () {
        return new LevelRepresentationParameters(new Random(), 6, 0.1, 0.1, 0.1, 0.2);
    }
}
