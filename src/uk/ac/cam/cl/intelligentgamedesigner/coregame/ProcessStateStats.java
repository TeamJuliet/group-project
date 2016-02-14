package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class ProcessStateStats {
    private boolean wasShuffled            = false;
    private int     numOfTransitions       = 0;
    private int     numOfValidMoves        = 0;
    private int     numOfPassedIngredients = 0;
    private int     numOfRemovedIcing      = 0;
    private Candy   candySwapped1, candySwapped2;

    public void reset() {
        this.wasShuffled = false;
        this.numOfPassedIngredients = this.numOfRemovedIcing = this.numOfTransitions = this.numOfValidMoves = 0;
        this.candySwapped1 = this.candySwapped2 = null;
    }

    public void setValidMoves(int moves) {
        this.numOfValidMoves = moves;
    }

    public void setShuffled(boolean wasShuffled) {
        this.wasShuffled = wasShuffled;
    }

    public void incrementTransitions() {
        ++this.numOfTransitions;
    }

    public void incrementIcing() {
        ++this.numOfRemovedIcing;
    }

    public void incrementPassedIngredients() {
        ++this.numOfPassedIngredients;
    }

    public void setCandySwapped1(Candy candy) {
        this.candySwapped1 = new Candy(candy);
    }

    public void setCandySwapped2(Candy candy) {
        this.candySwapped2 = new Candy(candy);
    }

    public ProcessStateStatsViewer toViewer() {
        return new ProcessStateStatsViewer(this.wasShuffled, this.numOfTransitions, this.numOfValidMoves,
                this.numOfPassedIngredients, this.numOfRemovedIcing, candySwapped1, candySwapped2);
    }
}
