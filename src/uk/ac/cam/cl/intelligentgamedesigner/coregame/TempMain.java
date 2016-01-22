package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public class TempMain {

    public static void main (String args[]) {
    	GameState g = new GameState(null);
    	
    	for (int y = 0; y < 10; y++) {
    		for (int x = 0; x < 10; x++) {
    			System.out.print(g.getCell(x, y).getCandy().getColour() + " ");
    		}
    		System.out.println();
    	}
    }
}
