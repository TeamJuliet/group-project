package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class Parameter {
	private Random random;
	private int value;
	private int max;
	private int min;
	
	public Parameter(Random r, int min, int max) {
		this(r, min, max, 0);
		// The value will be between min and max inclusive.
		value = random.nextInt(max - min + 1) + min;
	}
	
	private Parameter(Random r, int min, int max, int initialValue) {
		this.min = min;
		this.max = max;
		random = r;
		value = initialValue;
	}
	
	public Parameter copy() {
		return new Parameter(random, min, max, value);
	}
	
	public void generateNewValue() {
		// The value will be between min and max inclusive.
		// Not doing max - min + 1, because we generate a random number one less than the range,
		// then add 1 if it's greater than or equal to the old value.
		// This guarantees a different value to the current one.
		int newValue = random.nextInt(max - min) + min;
		if (newValue >= value) {
			newValue++;
		}
		value = newValue;
	}
	
	public int getValue() {
		return value;
	}
	
}
