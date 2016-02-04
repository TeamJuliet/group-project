package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class PreFilledCandyGenerator extends CandyGenerator {

    private ArrayList<Stack<Cell>> lookahead;

    public PreFilledCandyGenerator (DesignParameters designParameters, Cell[][] lookahead) {
        super(designParameters);

        this.lookahead = new ArrayList<>(lookahead.length);

        // Populate the stacks with the candies specified in the lookahead array
        for (int x = 0; x < lookahead.length; x++) {
            Stack<Cell> column = new Stack<>();

            for (int y = 0; y < lookahead[0].length - 1; y++) {
                column.push(lookahead[x][y]);
            }

            this.lookahead.add(column);
        }
    }

    @Override
    public Candy generateCandy (int x) {
        if (x >= 0 && x < lookahead.size() && !lookahead.get(x).empty()) {
            return lookahead.get(x).pop().getCandy();
        }

        // Otherwise just return a random Candy
        Random random = new Random();
        int result = random.nextInt(super.designParameters.getNumberOfCandyColours());
        return new Candy(CandyColour.values()[result], CandyType.NORMAL);
    }
}