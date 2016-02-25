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
	public final List<Position> positionsMatched;
	public final List<CandyType> containsSpecials;
	public final boolean formsSpecial;
	public final CandyType formedSpecialType;
	public final int jelliesRemoved;
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
