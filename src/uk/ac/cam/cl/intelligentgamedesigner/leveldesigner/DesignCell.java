package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;

import java.util.Random;

public class DesignCell {

    private DesignCellType designCellType;
    private int jellyLevel;

    // Constructor for creating a randomly initialised design cell type
    public DesignCell (LevelRepresentationParameters parameters, int jellyLevel) {
        // Create a cell with random cell type
    	
    	double r = parameters.random.nextDouble();

        double icingProbability     = parameters.targetIcingDensity * 0.4;
        double liquoriceProbability = parameters.targetLiquoriceDensity * 0.4;
        double remaining            = 1 - icingProbability - liquoriceProbability;
        double emptyProbability     = remaining * 0.8;

    	if (r < icingProbability) {
    		this.designCellType = DesignCellType.ICING;
    	} else if (r < icingProbability + liquoriceProbability) {
    		this.designCellType = DesignCellType.LIQUORICE;
    	} else if (r < icingProbability + liquoriceProbability + emptyProbability) {
    		this.designCellType = DesignCellType.EMPTY;
    	} else {
    		this.designCellType = DesignCellType.UNUSABLE;
    	}

        this.jellyLevel = jellyLevel;
    }

    public DesignCell (DesignCell designCellToCopy) {
        this.designCellType = designCellToCopy.getDesignCellType();
        this.jellyLevel = designCellToCopy.getJellyLevel();
    }

    public DesignCellType getDesignCellType () {
        return designCellType;
    }

    public int getJellyLevel () {
        return jellyLevel;
    }

    public void setDesignCellType (DesignCellType designCellType) {
        this.designCellType = designCellType;
        if (designCellType == DesignCellType.UNUSABLE) this.jellyLevel = 0;
    }

    public void setJellyLevel (int jellyLevel) {
        if (designCellType != DesignCellType.UNUSABLE) this.jellyLevel = jellyLevel;
    }
}
