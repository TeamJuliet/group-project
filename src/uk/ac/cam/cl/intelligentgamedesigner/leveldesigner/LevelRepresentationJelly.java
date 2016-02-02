package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class LevelRepresentationJelly extends GeneralLevelRepresentation {

    protected int[][] jellyLevels;

    /*
        Parameters list:
        ----------------
        [0] - number of moves available
        [1] - number of candy colours

        Board cell types:
        -----------------
        0 => Empty
        1 => Unusable
        2 => Icing
        3 => Liquorice
     */

    public LevelRepresentationJelly(Random random)
    {
        super(random);

        // Initialise the jelly levels
        this.jellyLevels = new int[GeneralLevelRepresentation.maxWidth][GeneralLevelRepresentation.maxHeight];
        for (int x = 0; x < GeneralLevelRepresentation.maxWidth; x++) {
            for (int y = 0; y < GeneralLevelRepresentation.maxHeight; y++) {
                // Jelly levels initialised in the range: 0-2
                jellyLevels[x][y] = random.nextInt(3);
            }
        }
    }

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LevelRepresentation> crossoverWith(LevelRepresentation parent) {
        LevelRepresentationJelly child1 = new LevelRepresentationJelly(new Random());
        LevelRepresentationJelly child2 = new LevelRepresentationJelly(new Random());

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
