package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to represent a board in a way such that it can be initialised, mutated and crossed over with
 * other boards.
 */
public class DesignBoard {
	private LevelRepresentationParameters parameters;
	private List<DesignCell> board;
	public final int width;
	public final int height;

	public DesignBoard(int width, int height, LevelRepresentationParameters parameters) {
		this.width = width;
		this.height = height;
		this.parameters = parameters;
		this.board = new ArrayList<>(width * height);

		int length = width * height;
		for (int i = 0; i < length; i++) {
			// Initialise the cells with a random cell type, but with no jelly
			// If the ArrayLevelRepresentationJelly calls initialiseJellyLevels manually
			board.add(new DesignCell(parameters, 0));
		}
	}

	public DesignBoard(DesignBoard designBoardToCopy) {
		this.width = designBoardToCopy.width;
		this.height = designBoardToCopy.height;
		this.parameters = designBoardToCopy.parameters;
		this.board = new ArrayList<>(width * height);

		// Copy the cell type and
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board.add(new DesignCell(designBoardToCopy.get(x, y)));
			}
		}
	}

	public void initialiseJellyLevels() {
		for (DesignCell designCell : board) {
			if (designCell.getDesignCellType() != DesignCellType.UNUSABLE) {
				double probability = parameters.random.nextDouble();
				if (probability < parameters.targetJellyDensity) {
					designCell.setJellyLevel(1);
				} else {
					designCell.setJellyLevel(0);
				}
			}
		}
	}


	public void mutateCellType() {
		int x = parameters.random.nextInt(width);
        int y = parameters.random.nextInt(height);
        
        DesignCell currentCell = get(x, y);
        int currentIndex = currentCell.getDesignCellType().ordinal();
        
        // Doing the following guarantees the new index will be different.
        int newIndex = parameters.random.nextInt(DesignCellType.values().length - 1);
        if (newIndex >= currentIndex) {
        	newIndex++;
        }
        
        currentCell.setDesignCellType(DesignCellType.values()[newIndex]);
	}

	public void mutateJellyLevels() {
		int x = parameters.random.nextInt(width);
		int y = parameters.random.nextInt(height);

		DesignCell currentCell = get(x, y);
		int currentJellyLevel = currentCell.getJellyLevel();

		// Doing the following guarantees the new index will be different.
		int newJellyLevel = parameters.random.nextInt(ArrayLevelRepresentationJelly.maxJellyLevel - 1);
		if (newJellyLevel >= currentJellyLevel) {
			newJellyLevel++;
		}

		currentCell.setJellyLevel(newJellyLevel);
	}
	
	public void crossoverWith(DesignBoard b) {
		if (width != b.width || height != b.height) {
			throw new RuntimeException("Trying to crossover boards that have different dimensions.");
		}
		
		boolean isVerticalSplit = parameters.random.nextBoolean();
		
        // This gives a range slightly in from the maxHeight or maxWidth.
        // For example, if the maxWidth is 10, this gives us a range of 1 - 8 instead of 0 - 9.
		int max = isVerticalSplit ? width : height;
        int splitEnd = parameters.random.nextInt(max - 1) + 1;
        
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
        		DesignCell temp = get(x, y);
        		set(x, y, b.get(x, y));
        		b.set(x, y, temp);
        	}
        }
	}
	
	public DesignCell get(int x, int y) {
		return board.get(x + y * width);
	}
	
	public void set(int x, int y, DesignCell value) {
		board.set(x + y * width, value);
	}

}
