package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.Random;

public class CandyGenerator {
    private DesignParameters designParameters;

    public CandyGenerator (DesignParameters designParameters) {
        this.designParameters = designParameters;
    }

    public Candy getCandy () {
        Random random = new Random();

        int result = random.nextInt(CandyColour.values().length);

        return new Candy(CandyColour.values()[result]);
    }
}
