package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.InvalidMoveException;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Move;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Position;

//Very basic player, simply search for first available move and make it
public class basicScorePlayer implements SimulatedPlayerBase {
    GameState level;

    public basicScorePlayer(GameState level) {
        this.level = level;

    }

    @Override
    public void solve() {
        while (level.getMovesRemaining() > 0) {
            findMove:
            for (int x = 0; x < level.width; x++) {
                for (int y = 0; y < level.height; y++) {
                    Move horizontalSwap = new Move(new Position(x, y), new Position(x + 1, y));
                    if (level.isMoveValid(horizontalSwap)) {
                        try {
                            level.makeMove(horizontalSwap);
                        } catch (InvalidMoveException e) {
                            printInvalidMoveError(e.invalidMove);
                            continue;
                        }
                        break findMove;
                    }

                    Move verticalSwap = new Move(new Position(x, y), new Position(x, y + 1));
                    if (level.isMoveValid(horizontalSwap)) {
                        try {
                            level.makeMove(verticalSwap);
                        } catch (InvalidMoveException e) {
                            printInvalidMoveError(e.invalidMove);
                            continue;
                        }
                        break findMove;
                    }
                }
            }
        }
    }

    @Override
    public void printInvalidMoveError(Move move) {
        System.err.println("WARNING! basicScorePlayer has suggested an invalidMove " + move + ".");
    }

}
