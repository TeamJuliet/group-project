package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class LevelRepresentationIngredients extends LevelRepresentation {

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours
        [2] - number of ingredients

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    @Override
    public void initialise() {
        super.initialise();

        Random random = new Random();
        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(random.nextInt(5) + 1);
    }

    @Override
    public void mutate() {

    }
}
