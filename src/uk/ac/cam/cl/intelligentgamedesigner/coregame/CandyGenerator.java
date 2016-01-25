package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public abstract class CandyGenerator {
    private DesignParameters designParameters;

    public CandyGenerator (DesignParameters designParameters) {
    	this.designParameters = designParameters;
    }
    
    public abstract Candy getCandy();
}
