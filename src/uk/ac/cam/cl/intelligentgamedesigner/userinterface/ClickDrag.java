package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.util.TimerTask;

public class ClickDrag extends TimerTask{
	
	private GameBoard board;
	
	public ClickDrag(GameBoard observer){
		board = observer;
	}

	@Override
	public void run() {
		if(board instanceof CustomBoard)((CustomBoard)board).changeTile();
		if(board instanceof HumanGameBoard)((HumanGameBoard)board).updateClickPos();
	}

}
