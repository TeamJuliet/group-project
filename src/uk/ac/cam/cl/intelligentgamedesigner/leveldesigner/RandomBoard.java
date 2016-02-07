package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomBoard<T> {
	private Random random;
	private List<T> board;
	private List<T> possibleValues;
	public final int width;
	public final int height;
	
	private RandomBoard(int width, int height, Random random, List<T> possibleValues, List<T> board) {
		this.width = width;
		this.height = height;
		this.random = random;
		this.possibleValues = possibleValues;
		this.board = board;
	}
	
	public RandomBoard(int width, int height, Random random, T[] possibleValues) {
		this(width, height, random, Arrays.asList(possibleValues), new ArrayList<>());
		
		int length = width * height;
		for (int i = 0; i < length; i++) {
			int r = random.nextInt(this.possibleValues.size());
			board.add(this.possibleValues.get(r));
		}
	}

	public RandomBoard(RandomBoard<T> b) {
		this(b.width, b.height, b.random, b.possibleValues, new ArrayList<>(b.board));
	}
	
	public void mutate() {
		int x = random.nextInt(width);
        int y = random.nextInt(height);
        
        T currentValue = get(x, y);
        int currentIndex = possibleValues.indexOf(currentValue);
        
        // Doing the following guarantees the new index will be different.
        int newIndex = random.nextInt(possibleValues.size() - 1);
        if (newIndex >= currentIndex) {
        	newIndex++;
        }
        
        set(x, y, possibleValues.get(newIndex));
	}
	
	public void crossoverWith(RandomBoard<T> b) {
		if (width != b.width || height != b.height) {
			throw new RuntimeException("Trying to crossover boards that have different dimensions.");
		}
		
		boolean isVerticalSplit = random.nextBoolean();
		
        // This gives a range slightly in from the maxHeight or maxWidth.
        // For example, if the maxWidth is 10, this gives us a range of 1 - 8 instead of 0 - 9.
		int max = isVerticalSplit ? width : height;
        int splitEnd = random.nextInt(max - 1) + 1;
        
        int length = isVerticalSplit ? height : width;
        for (int i = 0; i < splitEnd; i++) {
        	for (int j = 0; j < length; j++) {
        		int x = i;
        		int y = j;
        		if (!isVerticalSplit) {
        			x = j;
        			y = i;
        		}
        		
        		// Swap the values.
        		T temp = get(x, y);
        		set(x, y, b.get(x, y));
        		b.set(x, y, temp);
        	}
        }
	}
	
	public T get(int x, int y) {
		return board.get(x + y * width);
	}
	
	public void set(int x, int y, T value) {
		board.set(x + y * width, value);
	}

}
