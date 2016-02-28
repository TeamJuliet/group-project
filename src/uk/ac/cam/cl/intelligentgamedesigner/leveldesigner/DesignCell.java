package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

/**
 * This class is used to encapsulate the details of a particular cell on a DesignBoard.
 */
public class DesignCell {

    private DesignCellType designCellType;
    private int jellyLevel;

    // Constructor for creating a randomly initialised design cell type
    public DesignCell (LevelRepresentationParameters parameters, int jellyLevel) {
        // Create a cell with random cell type
    	
    	double rand1 = parameters.random.nextDouble();

        double unusableProbability = parameters.targetUnusableDensity * 0.8;

        if (rand1 < unusableProbability) {
            this.designCellType = DesignCellType.UNUSABLE;
        } else {
            // Otherwise, we choose between icing, liquorice or empty
            double rand2 = parameters.random.nextDouble();

            double icingProbability     = parameters.targetIcingDensity * 0.4;
            double liquoriceProbability = parameters.targetLiquoriceDensity * 0.4;

            if (rand2 < icingProbability) {
                this.designCellType = DesignCellType.ICING;
            } else if (rand2 < icingProbability + liquoriceProbability) {
                this.designCellType = DesignCellType.LIQUORICE;
            } else {
                this.designCellType = DesignCellType.EMPTY;
            }
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
