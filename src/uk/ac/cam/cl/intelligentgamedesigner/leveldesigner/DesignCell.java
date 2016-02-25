package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class DesignCell {

    private DesignCellType designCellType;
    private int jellyLevel;

    // Constructor for creating a randomly initialised design cell type
    public DesignCell (Random random, int jellyLevel) {
        // Create a cell with random cell type
    	
    	double r = random.nextDouble();
    	
    	if(r < 0.4) {
    		this.designCellType = DesignCellType.EMPTY;
    	}else if(r < 0.6){
    		this.designCellType = DesignCellType.UNUSABLE;
    	}else if(r < 0.8){
    		this.designCellType = DesignCellType.ICING;
    	}else{
    		this.designCellType = DesignCellType.LIQUORICE;
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
