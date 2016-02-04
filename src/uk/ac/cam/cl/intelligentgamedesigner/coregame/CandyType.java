package uk.ac.cam.cl.intelligentgamedesigner.coregame;

public enum CandyType {
	NORMAL,
	VERTICALLY_STRIPPED,
	HORIZONTALLY_STRIPPED,
	WRAPPED,
	BOMB, // Does not have a Candy Color.
	INGREDIENT //Does not have a Candy Color
	; 
	
	public boolean isSpecial() {
		return this != NORMAL;
	}
	
	// Returns the number a certain type of candy detonates.
	public int getDetonations() {
		switch (this) {
		case VERTICALLY_STRIPPED:
			return 1;
		case HORIZONTALLY_STRIPPED:
			return 1;
		case WRAPPED:
			return 2;
		default:
			return 0;
		
		}
	}
}
