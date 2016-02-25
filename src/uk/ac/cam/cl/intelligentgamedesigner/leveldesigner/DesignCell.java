package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class DesignCell {

    private DesignCellType designCellType;
    private int jellyLevel;

    // Constructor for creating a randomly initialised design cell type
    public DesignCell (Random random, int jellyLevel) {
        // Create a cell with random cell type
    	
    	double r = random.nextDouble();
    	
    	if(r < 0.5) {
    		this.designCellType = DesignCellType.EMPTY;
    	}else if(r < 0.55){
    		this.designCellType = DesignCellType.UNUSABLE;
    	}else{
    		this.designCellType = DesignCellType.values()[random.nextInt(DesignCellType.values().length)];
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

    @Override
    public boolean equals (Object object) {
        if (!(object instanceof DesignCell)) return false;

        DesignCell designCell = (DesignCell) object;
        return this.designCellType == designCell.getDesignCellType()
                && this.jellyLevel == designCell.getJellyLevel();
    }
}
