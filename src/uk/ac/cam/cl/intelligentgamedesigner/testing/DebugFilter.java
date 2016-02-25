package uk.ac.cam.cl.intelligentgamedesigner.testing;

public class DebugFilter {

    // Comment out any of these filters according to what debug output you want to see when running the code
    private static final DebugFilterKey[] debugFilterKeys = {
            //DebugFilterKey.GAME_IMPLEMENTATION,
            //DebugFilterKey.SIMULATED_PLAYERS,
            DebugFilterKey.LEVEL_DESIGN,
            //DebugFilterKey.USER_INTERFACE
            };

    public static void print (String string, DebugFilterKey debugFilterKey) {
        for (DebugFilterKey k : debugFilterKeys) {
            if (k == debugFilterKey) System.out.print(string);
        }
    }

    public static void println (String string, DebugFilterKey debugFilterKey) {
        for (DebugFilterKey k : debugFilterKeys) {
            if (k == debugFilterKey) System.out.println(string);
        }
    }
}
