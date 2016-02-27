package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class DesignCell {

    private DesignCellType designCellType;
    private int jellyLevel;

    // Constructor for creating a randomly initialised design cell type
    public DesignCell (LevelRepresentationParameters parameters, int jellyLevel) {
        // Create a cell with random cell type
    	
    	double r = parameters.random.nextDouble();
    	
    	if(r < 0.75) {
    		this.designCellType = DesignCellType.EMPTY;
    	}else if(r < 0.85){
    		this.designCellType = DesignCellType.UNUSABLE;
    	}else if(r < 0.95){
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
