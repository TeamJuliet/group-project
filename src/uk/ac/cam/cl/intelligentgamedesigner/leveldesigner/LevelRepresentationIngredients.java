package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class LevelRepresentationIngredients extends GeneralLevelRepresentation {

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

    public LevelRepresentationIngredients(Random random) {
        super(random);

        // Number of ingredients is initialised in the range: 1-5
        this.parameters.add(random.nextInt(5) + 1);
    }

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation parent) {
		LevelRepresentationIngredients child1 = new LevelRepresentationIngredients(new Random());
		LevelRepresentationIngredients child2 = new LevelRepresentationIngredients(new Random());

		super.crossoverWith((GeneralLevelRepresentation) parent, child1, child2);

		ArrayList<LevelRepresentation> children = new ArrayList<>();
		children.add(child1);
		children.add(child2);

		return children;
	}

	@Override
	public Design getDesign() {
		// TODO Auto-generated method stub
		return null;
	}
}
