package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.UnmovableCandyGenerator;

abstract class DepthPotentialPlayer extends SimulatedPlayerBase {
    // The number of states that the Player should look ahead at each move.
    // Note: when this is -1, it generates all possible moves.
    protected final int                                numOfStatesAhead;

    // The number of states in the pool that will be chosen to be explored next.
    // Note: again, when this is -1, it discovers all possible moves.
    protected final int                                numOfStatesInPool;

    private PriorityQueue<GameStateWithCombinedMetric> pool;
    private PriorityQueue<GameStateWithCombinedMetric> results;

    // Function that evaluates the current game state based on the knowledge
    // at that particular state.
    public abstract GameStateMetric getGameStateMetric(GameState gameState);

    // Function that evaluates the current game state based on its potential
    // of making progress towards the goal. (e.g. if there is a large number
    // of cells containing jellies refreshed, etc).
    public abstract GameStatePotential getGameStatePotential(GameState gameState);

    public abstract GameStateCombinedMetric getCombinedMetric(GameStateMetric metric, GameStatePotential potential);

    /**
     * Function that filters the moves on the board that will be checked next.
     * 
     * @param gameState
     *            Game state where the moves will be fetched from.
     * @return A list of moves that the player will choose from.
     */
    protected List<Move> selectMoves(GameState gameState) {
        // Shuffle the moves so that they are not biased to the left and upper
        // most part of the board.
        List<Move> ret = gameState.getValidMoves();
        Collections.shuffle(ret);
        return ret;
    }

    private GameStateWithCombinedMetric generateCombinedMetric(GameStateWithCombinedMetric state, Move move) {
        GameState nextState;
        try {
            nextState = simulateNextMove(state.gameState, move);
        } catch (InvalidMoveException e) {
            System.err.println("Some of the moves generated are not possible.");
            System.out.println(state.gameState);
            System.out.println(state.gameState.getValidMoves());
            e.printStackTrace();
            return null;
        }
        Move moveRecorded = state.originalMove == null ? move : state.originalMove;
        return new GameStateWithCombinedMetric(nextState,
                getCombinedMetric(getGameStateMetric(nextState), getGameStatePotential(nextState)), moveRecorded);
    }

    private void nextDepth() {
        int upperLimit = numOfStatesInPool == -1 ? pool.size() : numOfStatesInPool;
        int elementsProcessed = 0;
        PriorityQueue<GameStateWithCombinedMetric> nextPool = new PriorityQueue<GameStateWithCombinedMetric>(
                numOfStatesInPool);
        while (elementsProcessed < upperLimit && !pool.isEmpty()) {
            GameStateWithCombinedMetric current = pool.poll();
            List<Move> moves = selectMoves(current.gameState);
            if (moves.isEmpty() || current.gameState.isGameOver()) {
                results.add(current);
                continue;
            }
            for (Move move : moves) {
                nextPool.add(generateCombinedMetric(current, move));
            }

        }
        pool = nextPool;
    }

    public Move calculateBestMove(GameState currentState) throws NoMovesFoundException {
        List<Move> moves = currentState.getValidMoves();
        if (moves.size() == 0)
            throw new NoMovesFoundException(currentState);
        pool = new PriorityQueue<GameStateWithCombinedMetric>();
        results = new PriorityQueue<GameStateWithCombinedMetric>();
        // Add the current game state with the
        pool.add(new GameStateWithCombinedMetric(currentState, new GameStateCombinedMetric(), null));
        int stages = 0;
        // TODO: Add handling for -1.
        while (stages < numOfStatesAhead) {
            nextDepth();
            stages++;
        }
        while (!pool.isEmpty()) {
            results.add(pool.poll());
        }
        Move moveMake = results.peek().originalMove;
        results.clear();
        return moveMake;
    }

    public DepthPotentialPlayer(int numOfStatesAhead, int numOfStatesInPool) {
        this.numOfStatesAhead = numOfStatesAhead;
        this.numOfStatesInPool = numOfStatesInPool;
    }

    public DepthPotentialPlayer() {
        this(2, 4);
    }

    protected void printInvalidMoveError(GameState level, Move move) {
        System.err.format(
                "WARNING! %s with settings (%d,%d) has suggested an invalidMove:\n" + level + "\n" + move + ".\n",
                this.getClass().getSimpleName(), this.numOfStatesAhead, this.numOfStatesInPool);
    }

    protected void printInvalidSuggestionError(GameState level, Move move) {
        System.err.format("WARNING! Invalid move suggested in %s (%d,%d) evaluation:\n" + level + "\n" + move + ".\n",
                this.getClass().getSimpleName(), this.numOfStatesAhead, this.numOfStatesInPool);
        System.err.println(level.getValidMoves());
    }

    private GameState simulateNextMove(GameState gameState, Move move) throws InvalidMoveException {
        GameState nextState = new GameState(gameState, new UnmovableCandyGenerator());
        nextState.makeFullMove(move);
        return nextState;
    }
}
