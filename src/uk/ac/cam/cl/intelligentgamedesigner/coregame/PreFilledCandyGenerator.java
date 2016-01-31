package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class PreFilledCandyGenerator extends CandyGenerator {

    private ArrayList<Stack<Cell>> lookahead;

    public PreFilledCandyGenerator (Cell[][] lookahead) {
        super(new DesignParameters());

        this.lookahead = new ArrayList<>(10);

        // Populate the stacks with the candies specified in the lookahead array
        for (int x = 0; x < 10; x++) {
            Stack<Cell> column = new Stack<>();

            for (int y = 9; y >= 0; y--) {
                column.push(lookahead[x][y]);
            }

            this.lookahead.add(column);
        }
    }

    @Override
    public Candy generateCandy (int x) {
        if (x >= 0 && x < 10 && !lookahead.get(x).empty()) {
            return lookahead.get(x).pop().getCandy();
        }

        // Otherwise just return a random Candy
        Random random = new Random();
        int result = random.nextInt(CandyColour.values().length);
        return new Candy(CandyColour.values()[result], CandyType.NORMAL);
    }
}
