package uk.ac.cam.cl.intelligentgamedesigner.coregame;

/**
 * 
 * Candy Generator that creates a deterministic sequence of candies.
 *
 */
public class PseudoRandomCandyGenerator extends CandyGenerator {

    public PseudoRandomCandyGenerator() {
        super();
    }

    private int curNum = 38, prime = 361, anotherPrime = 991;

    private int nextPseudoRandom() {
        // System.out.println(curNum + " " + CandyColour.values().length);
        curNum = (curNum * prime) % anotherPrime;
        return curNum;
    }

    @Override
    public Candy generateCandy(int x) {

        if (shouldGenerateIngredient()) return new Candy(null, CandyType.INGREDIENT);

        int num = nextPseudoRandom();
        // This line just adds some bombs for testing.
        // if (num % 23 == 2) return new Candy(null, CandyType.BOMB);
        // If an ingredient wasn't dropped, then drop a normal candy
        int result = num % super.design.getNumberOfCandyColours();
        CandyType type = CandyType.NORMAL;
        return new Candy(CandyColour.values()[result], type);
    }
}
