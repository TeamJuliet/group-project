package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class LevelRepresentationScore extends GeneralLevelRepresentation {

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours
        [2] - score to reach

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public LevelRepresentationScore(Random random) {
        super(random);

        // Score to reach is initialised in the range: 1000-500000
        this.parameters.add(new Parameter(random, 1000, 500000));
    }
    
    @Override
    public Design getDesign()
    {
    	Design design = getBaseDesign();
    
    	// TODO: set other parameters
    }

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation parent) {
        LevelRepresentationScore child1 = new LevelRepresentationScore(new Random());
        LevelRepresentationScore child2 = new LevelRepresentationScore(new Random());

        super.crossoverWith((GeneralLevelRepresentation) parent, child1, child2);

        ArrayList<LevelRepresentation> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
	}
}
