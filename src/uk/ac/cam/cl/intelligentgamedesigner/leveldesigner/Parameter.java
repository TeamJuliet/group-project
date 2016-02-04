package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class Parameter {
	private Random random;
	private int value;
	private int max;
	private int start;
	
	public Parameter(Random r, int start, int max) {
		random = r;
		this.start = start;
		this.max = max;
		// The value will be between start and max inclusive.
		value = random.nextInt(max - start + 1) + start;
	}
	
	public void generateNewValue() {
		// The value will be between start and max inclusive.
		// Not doing max - start + 1, because we generate a random number one less than the range,
		// then add 1 if it's greater than or equal to the old value.
		// This guarantees a different value to the current one.
		int newValue = random.nextInt(max - start) + start;
		if (newValue >= value) {
			newValue++;
		}
		value = newValue;
	}
	
	public int getValue() {
		return value;
	}
	
}
