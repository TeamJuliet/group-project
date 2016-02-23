package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class Neighbour implements Comparable<Neighbour> {
	
	Neighbour(int x, int y, Label label)
	{
		this.x = x;
		this.y = y;
		
		this.label = label;
	}
	
	public void next()
	{
		label.next();
	}
	
	public void set(Label l)
	{
		label.set(l);
	}
	
	public final Label get()
	{
		return label;
	}
	
	@Override
	public int compareTo(Neighbour arg0) {
		return label.compareTo(arg0.get());
	}
	
	public final int x, y;
	private Label label;
}
