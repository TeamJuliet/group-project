package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelDesignerManager {
    private long seed = 1;
    private Specification specification;
    private Random originalRandom;
    protected LevelDesigner levelDesigner;

    public LevelDesignerManager (Specification specification) {
        this.specification = specification;

        this.originalRandom = new Random(seed);
        this.levelDesigner = new LevelDesigner(this, this.originalRandom);
    }

    public void run() {
        this.levelDesigner.run();
    }

    public List<LevelRepresentation> getPopulation(int size) {
        List<LevelRepresentation> population = new ArrayList<>();

        switch (specification.getGameMode()) {
            case HIGHSCORE:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationScore(originalRandom));
                }
                break;
            case JELLY:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationJelly(originalRandom));
                }
                break;
            default:
                for (int i = 0; i < size; i++) {
                    population.add(new ArrayLevelRepresentationIngredients(originalRandom));
                }
        }

        return population;
    }

    public double getDifficultyFitness (Design design) {

        // TODO: Select appropriate simulated players to evaluate the design

        // TODO: Run the simulated players on the design

        // TODO: Evaluate the results and convert it to a scalar fitness value

        // TODO: Return the scalar fitness value
        return 0;
    }

}
