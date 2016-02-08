package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

//defines the functionality specific to the Simulated Player viewer
public class ComputerGameDisplayScreen extends GameDisplayScreen{
	public ComputerGameDisplayScreen(){
		super();
		identifier = "Simulated Player";
	}

	@Override
	protected GameBoard specificGameBoard() {
		return new GameBoard(new Design());
	}
}
