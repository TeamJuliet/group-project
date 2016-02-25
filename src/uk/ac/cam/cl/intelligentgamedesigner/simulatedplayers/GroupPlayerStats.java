package uk.ac.cam.cl.intelligentgamedesigner.simulatedplayers;

/**
 * 
 * Class that contains the statistics for Game Levels as provided by the Candy
 * Crush Wikia.
 *
 */
public enum GroupPlayerStats {
	/**
	 * There is no difficulty with this level (e.g. level 1)
	 */
	NONE,

	/**
	 * The level can be done in one attempt easily and rarely two or more. The
	 * odds for succeeding are about 90%.
	 */
	VERY_EASY,

	/**
	 * This can be done within 2 attempts and the odds of winning a level are
	 * about 75%.
	 */
	EASY,

	/**
	 * This can be completed within 3 or 4 attempts. The odds for winning the
	 * level are about 60%.
	 */
	SOMEWHAT_EASY,

	/**
	 * This level can be done within 10 attempts and the probability of
	 * succeeding is about 40%.
	 */
	MEDIUM,

	/**
	 * This can be done within 15 attempts. The probability of succeeding is
	 * about 25%.
	 */
	SOMEWHAT_HARD,

	/**
	 * This can be done with more than 25 attempts and the odds of winning are
	 * below 15%.
	 */
	HARD,

	/**
	 * This can be done with more than 40 attempts and the odds of winning are
	 * below 10%.
	 */
	VERY_HARD,

	/**
	 * This can take up to 100 attempts and the odds of winning are below 3%.
	 */
	INSANELY_HARD;

	/**
	 * Function that returns the percentage from which the winning rates are
	 * below.
	 * 
	 * @return the percentage of winning games for this level.
	 */
	public double getWinningPercentage() {
		switch (this) {
		case EASY:
			return 0.75;
		case HARD:
			return 0.15;
		case INSANELY_HARD:
			return 0.3;
		case MEDIUM:
			return 0.40;
		case NONE:
			return 0.0;
		case SOMEWHAT_EASY:
			return 0.6;
		case SOMEWHAT_HARD:
			return 0.25;
		case VERY_EASY:
			return 0.9;
		case VERY_HARD:
			return 0.1;
		default:
			return 0.0;
		}
	}

	/**
	 * Function that returns an estimate for the average number of attempts
	 * needed to pass the level.
	 * 
	 * @return The estimated average number of attempts required.
	 */
	public int getAverageAttemptsRequired() {
		switch (this) {
		case EASY:
			return 2;
		case HARD:
			return 25;
		case INSANELY_HARD:
			return 200;
		case MEDIUM:
			return 10;
		case NONE:
			return 1;
		case SOMEWHAT_EASY:
			return 3;
		case SOMEWHAT_HARD:
			return 15;
		case VERY_EASY:
			return 1;
		case VERY_HARD:
			return 40;
		default:
			return 0;
		}
	}
}
