package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.Serializable;

/**
 * 
 * Abstract class that is used to generate new candies for the positions to be
 * filled. Note: that the function setDesignAndGameProgress has to been called
 * before being called.
 *
 */
public abstract class CandyGenerator implements Serializable {

	/**
	 * The level design used to generate the game that is being played.
	 */
	protected Design design;

	/**
	 * The game state progress for the current game.
	 */
	protected GameStateProgress gameStateProgress;

	/**
	 * The previous number of ingredients that remained.
	 */
	protected int previousNumberOfIngredientsRemaining;

	/**
	 * Remaining ingredients that have to drop.
	 */
	protected int ingredientsToDrop;

	/**
	 * The default candy generator constructor.
	 */
	public CandyGenerator() {
		this.ingredientsToDrop = 0;
	}

	/**
	 * Function that sets the design and game state progress of the game state.
	 * Note: this should be done in the constructor of GameState.
	 * 
	 * @param design
	 *            The design used for generating the level. (This could be null
	 *            indicating that none was used in creating the GameState.
	 * @param gameStateProgress
	 *            The GameStateProgress, so that ingredients can be dropped
	 *            during the course of the game. (This should not be null)
	 */
	public void setDesignAndGameProgress(Design design,
			GameStateProgress gameStateProgress) {
		this.design = design;
		this.gameStateProgress = gameStateProgress;

		// It is ok for the design to be null, since one might not have been
		// used for creating a GameState.
		if (this.design == null)
			return;

		if (this.gameStateProgress == null)
			System.err
					.println("Candy Generator Error: Game state progress is null");

		this.previousNumberOfIngredientsRemaining = gameStateProgress
				.getIngredientsRemaining();

		// Either the user has specified initial positions, or the GameState has
		// automatically placed 1
		int numberOfInitialIngredients = Math.max(GameStateAuxiliaryFunctions
				.getIngredientsNumber(design.getBoard()), 1);

		this.ingredientsToDrop = gameStateProgress.getIngredientsRemaining()
				- numberOfInitialIngredients;
	}

	/**
	 * The main function of the CandyGenerator that actually returns a candy
	 * given the x-coordinate at which the cell is located at.
	 * 
	 * @param x
	 *            The x-coordinate of the cell.
	 * @return The candy generated.
	 */
	public abstract Candy generateCandy(int x);

	/**
	 * Function that determines whether it should generate an ingredient.
	 * 
	 * @param canDropRandomly
	 *            Indicates whether the candy generator can generate randomly.
	 * @return whether it should generate an ingredient.
	 */
	protected boolean shouldGenerateIngredient(boolean canDropRandomly) {
		// This ensures a new ingredient is introduced whenever a user clears
		// one on the board.
		if (ingredientsToDrop > 0) {
			if (previousNumberOfIngredientsRemaining > gameStateProgress
					.getIngredientsRemaining()
					|| (gameStateProgress.hasGameBegun() && canDropRandomly)) {
				ingredientsToDrop--;
				previousNumberOfIngredientsRemaining--;
				return true;
			}
		}
		return false;
	}
}
