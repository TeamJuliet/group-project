package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

//defines functionality specific to the Human controlled game
public class HumanGameDisplayScreen extends GameDisplayScreen{
	public HumanGameDisplayScreen(){
		super();
		identifier = "Human";
		((HumanGameBoard)board).watchGameDisplay(this);
	}

	@Override
	protected GameBoard specificGameBoard() {
		return new HumanGameBoard(new Design());
	}
	
	@Override
	protected void stopGame(){
		((HumanGameBoard)board).setTimer(false);
	}
	
	@Override
	protected void initialiseGame(){
		super.initialiseGame();
		((HumanGameBoard)board).setTimer(true);
	}
}
