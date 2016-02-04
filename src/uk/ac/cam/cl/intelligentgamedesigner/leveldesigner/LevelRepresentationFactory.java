package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelRepresentationFactory {

    private GameMode gameMode;

    public LevelRepresentationFactory (GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public LevelRepresentation getLevelRepresentation () {
        switch (gameMode) {
            case HIGHSCORE:
                return new ArrayLevelRepresentationScore(new Random());
            case JELLY:
                return new ArrayLevelRepresentationJelly(new Random());
            default:
                return new ArrayLevelRepresentationIngredients(new Random());
        }
    }

    public List<LevelRepresentation> getLevelRepresentationBatch (int batchSize) {
        ArrayList<LevelRepresentation> levelReps = new ArrayList<>(batchSize);

        for (int i = 0; i < batchSize; i++) {
            levelReps.add(getLevelRepresentation());
        }

        return levelReps;
    }
}
