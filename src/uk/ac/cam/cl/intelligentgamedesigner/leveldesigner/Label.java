package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class Label implements Comparable {
	private int label;
	
	public Label()
	{
		label = 0;
	}
	
	public Label(Label l)
	{
		label = l.label;
	}
	
	public void next()
	{
		label++;
	}
	
	public int value()
	{
		return label;
	}
	
	public void set(Label l)
	{
		label = l.label;
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		return ((Label)arg0).label == label;
	}

	@Override
	public int compareTo(Object arg0) {
		Label l = (Label)arg0;
		return label - l.label;
	}
}
