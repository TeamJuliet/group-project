package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.List;

/**
 * 
 * Class that contains information about how a match performed. This means that it contains information for:
 *      - A list of positions that where matched.
 *      - A list with the special candies that were detonated.
 *      - A boolean of whether a special candy was formed.
 *      - The special candy that was formed (if one was formed).
 *      - The number of jellies removed.
 *      - The number of blockers removed.
 *
 */
public class MatchAnalysis {
    /**
     * A list of the positions that were matched.
     */
	public final List<Position> positionsMatched;
	
	/**
	 * A list with the types of special candies that were matched.
	 */
	public final List<CandyType> containsSpecials;
	
	/**
	 * Whether this match will generate a special candy.
	 */
	public final boolean formsSpecial;
	
	/**
	 * The special candy that will be formed, if one is formed.
	 */
	public final CandyType formedSpecialType;
	
	/**
	 * The number of jellies that are removed by this match.
	 */
	public final int jelliesRemoved;
	
	/**
	 * The number of blockers that are removed by this match.
	 */
	public final int blockersRemoved;
	
	public MatchAnalysis(List<Position> positions, List<CandyType> list, CandyType candyType, int jelliesRemoved, int blockersRemoved) {
		this.positionsMatched = positions;
		this.containsSpecials = list;
		this.formedSpecialType = candyType;
		this.formsSpecial = candyType != null;
		this.jelliesRemoved = jelliesRemoved;
		this.blockersRemoved = blockersRemoved;
	}
	
}
