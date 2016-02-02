package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.List;

public class MatchAnalysis {
	public final List<Position> positionsMatched;
	public final List<CandyType> containsSpecials;
	public final boolean formsSpecial;
	public final CandyType formedSpecialType;
	
	public MatchAnalysis(List<Position> positions, List<CandyType> list, CandyType candyType) {
		this.positionsMatched = positions;
		this.containsSpecials = list;
		this.formedSpecialType = candyType;
		this.formsSpecial = candyType == null;
	}
	
}
