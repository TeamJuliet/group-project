package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devankuleindiren on 21/01/2016.
 */

public class GameState {
	// TODO: Make this cell private (public just for testing purposes).
	public Cell[][] board;
	int width;
	int height;
	int movesRemaining;
	int score;
	CandyGenerator candyGenerator;

	private List<Position> detonated = new ArrayList<Position>();
	private List<Position> popped = new ArrayList<Position>();
	private Move lastMove;

	public GameState(Design design) {
		width = 10;
		height = 10;

		board = new Cell[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				board[x][y] = new Cell(CellType.EMPTY);
			}
		}

		candyGenerator = new PseudoRandomCandyGenerator(null);

		refreshBoard();
		fillBoard();
	}

	private void refreshBoard() {
		fillBoard();
		// while(false) {
		// fillBoard();
		// }

	}
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	
	private class SingleTileAnalysis {
		// the start and end of the match on x-axis.
		public int start_x, end_x;
		// the start and end of the match on y-axis.
		public int start_y, end_y;
		// the coordinates of the central tile.
		public int x, y;

		public SingleTileAnalysis(int x, int y, int start_x, int end_x, int start_y, int end_y) {
			this.start_x = start_x;
			this.end_x = end_x;
			this.start_y = start_y;
			this.end_y = end_y;
			this.x = x;
			this.y = y;
		}

		// Returns the size of the horizontal match containing the point.
		public int getLengthX() {
			return end_x - start_x + 1;
		}

		// Returns the size of the vertical match containing the point.
		public int getLengthY() {
			return end_y - start_y + 1;
		}
	}

	// Returns the axial analysis for a single tile.
	// (i.e. the intervals on the x and y-axis that match the tile.
	private SingleTileAnalysis analyzeTile(Position pos) {
		System.out.println("Board before analysis: ");
		debugBoard();
		System.out.println("Analysis for " + pos.getX() + " " + pos.getY());
		int x = pos.getX(), y = pos.getY();
		CandyColour cellColour = board[x][y].getCandy().getColour();
		int start_x = x - 1, end_x = x + 1, start_y = y - 1, end_y = y + 1;
		while (start_x >= 0 && sameColourWithCell(board[start_x][y], cellColour))
			--start_x;
		while (end_x < width && sameColourWithCell(board[end_x][y], cellColour))
			++end_x;
		while (start_y >= 0 && sameColourWithCell(board[x][start_y], cellColour))
			--start_y;
		while (end_y < width && sameColourWithCell(board[x][end_y], cellColour))
			++end_y;
		// Normalisation.
		start_x++;
		start_y++;
		end_x--;
		end_y--;
		System.out.println("s_x: " + start_x + " e_x: " + end_x + " s_y: " + start_y + " e_y: " + end_y);
		return new SingleTileAnalysis(x, y, start_x, end_x, start_y, end_y);
	}

	// Function that returns whether the tile at position forms a match.
	private boolean tileFormsMatch(Position position) {
		SingleTileAnalysis analysis = analyzeTile(position);
		return analysis.getLengthX() > 2 || analysis.getLengthY() > 2;
	}
	
	private boolean wasSomethingPopped = false;
	
	// Function that adds the tile to detonated (the ones that are going to
	// break on the
	// next state). Note that bombs should have two levels of detonation.
	// Should also update score.
	// Consider making this i, j.
	// TODO: Need to add scoring.
	private void trigger(int x, int y) {
		Cell current = board[x][y];
		
		if (current.hasCandy() && current.getCandy().isDetonated())
			return;
		if (current.hasCandy() && current.getCandy().getCandyType().isSpecial()) {
			if (!current.getCandy().isDetonated()) {
				detonated.add(new Position(x, y));
				current.getCandy().setDetonated();
				wasSomethingPopped = true;
			}
		} else if (current.hasCandy()) {
			current.removeCandy();
			wasSomethingPopped = true;
		}
	}

	// Check if any of the two positions is in the horizontal range.
	private boolean moveInHorizontalRange(Move move, int startX, int endX) {
		return inRange(lastMove.getP1().getX(), startX, endX) || inRange(lastMove.getP2().getX(), startX, endX);
	}

	// Check if any of the two positions is in the vertical range.
	private boolean moveInVerticalRange(Move move, int startY, int endY) {
		return inRange(lastMove.getP1().getY(), startY, endY) || inRange(lastMove.getP2().getY(), startY, endY);
	}

	// Function that replaces all the matched tiles with their respective
	// Candy (either empty or special is some cases).
	private void markAndReplaceMatchingTiles() {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// Do not consider EMPTY cells.
				if (board[x][y].getCellType() == CellType.EMPTY)
					continue;
				SingleTileAnalysis analysis = analyzeTile(new Position(x, y));
				// In case there is a horizontal match.
				if (analysis.getLengthX() > 2) {
					boolean foundVertical = false;
					if (analysis.getLengthX() >= 5) foundVertical = true;
					for (int k = analysis.start_x; k <= analysis.end_x; ++k) {
						SingleTileAnalysis childAnalysis = analyzeTile(new Position(k, y));
						if (childAnalysis.getLengthY() > 2) {
							// Should this candy be replaced by a wrapped candy.
							if (!foundVertical) {
								foundVertical = true;
								// Wrap the candy at position (k, y).
								board[k][y].changeCandyType(CandyType.WRAPPED);
							} else {
								trigger(k, y);
							}
							// Iterate through the vertical column and trigger
							// them.
							for (int yy = childAnalysis.start_y; yy <= childAnalysis.end_y; ++y) {
								if (yy == y)
									continue;
								trigger(k, yy);
							}
						} else {
							// If there is no vertical match formed then just
							// trigger the cell.
							trigger(k, y);
						}
					}
					if (!foundVertical) {
						// If last move is in the range we went through, then
						// make
						// that one the stripped candy.
						if (lastMove == null || !moveInHorizontalRange(lastMove, analysis.start_x, analysis.end_x)) {
							// Make the middle candy vertically stripped.
							board[x + 1][y].changeCandyType(CandyType.VERTICALLY_STRIPPED);
						} else {
							int coordinate;
							// If one of the positions has the same
							// y-coordinate.
							if (lastMove.getP1().getY() == y)
								coordinate = lastMove.getP1().getX();
							else
								coordinate = lastMove.getP2().getX();
							board[coordinate][y].changeCandyType(CandyType.VERTICALLY_STRIPPED);
						}
					} else {
						// TODO: Fix to be aligned with the last move.
						if (analysis.getLengthX() >= 5) board[x+2][y].changeCandyType(CandyType.BOMB);
					}
				} else if (analysis.getLengthY() > 2) {
					boolean foundHorizontal = false;
					// TODO: complete this.

				}
			}
		}
	}

	// Function that will detonate a wrapped candy.
	private void detonateWrapped(Position wrapped) {
		for (int i = wrapped.getX() - 1; i <= wrapped.getY() + 1; ++i) {
			for (int j = wrapped.getY() - 1; j <= wrapped.getY() + 1; ++j) {
				if (i == wrapped.getX() && j == wrapped.getY()) {
					// table[i][j].detonate
					continue;
				}
				trigger(i, j);
			}
		}
	}

	private void detonateVerticallyStripped(Position vStripped) {
		for (int y = 0; y < height; ++y) {
			trigger(vStripped.getX(), y);
		}
	}

	private void detonateHorizontallyStripped(Position hStripped) {
		for (int x = 0; x < height; ++x) {
			trigger(x, hStripped.getY());
		}
	}

	// Function that detonates all the elements that have to explode
	private void detonateAllPending() {
		List<Position> oldDetonated = detonated;
		detonated = new ArrayList<Position>();
		for (Position d : oldDetonated) {
			Cell detonatingCell = getCell(d);
			switch (detonatingCell.getCandy().getCandyType()) {
			case WRAPPED:
				detonateWrapped(d);
				break;
			case HORIZONTALLY_STRIPPED:
				detonateHorizontallyStripped(d);
				break;
			case VERTICALLY_STRIPPED:
				detonateVerticallyStripped(d);
				break;
			default:
			}
			if (detonatingCell.getCandy().getDetonationsRemaining() > 1) {
				detonatingCell.getCandy().decreaseDetonations();
				detonated.add(d);
			} else {
				detonatingCell.removeCandy();
			}
		}
	}

	// Brings candies down.
	private void bringDownCandies() {
		for (int i = 0; i < width; ++i) {
			for (int j = 1; j < height; ++j) {
				if (board[i][j - 1].getCellType().equals(CellType.EMPTY)) {
					// board[i][j].getUpper( board[i][j-1] );
				}
			}
		}
	}

	private void fillBoard() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell cell = board[x][y];
				if (cell.getCellType() == CellType.EMPTY) {
					Candy candy = candyGenerator.getCandy();
					cell.setCandy(candy);
				}
			}
		}
	}

	Cell getCell(int x, int y) {
		return board[x][y];
	}

	// Auxiliary function to check a <= x <= b
	private boolean inRange(int x, int a, int b) {
		return x >= a && x <= b;
	}

	private boolean inBoard(Position pos) {
		return inRange(pos.getX(), 0, width - 1) && inRange(pos.getY(), 0, height - 1);
	}

	private boolean isPositionValid(Position pos) {
		return inBoard(pos) && board[pos.getX()][pos.getY()].isMoveable();
	}

	private Cell getCell(Position pos) {
		return board[pos.getX()][pos.getY()];
	}

	private boolean isMoveValid(Move move) {
		if (!isPositionValid(move.getP1()) && !isPositionValid(move.getP2()))
			return false;
		Cell cell1 = getCell(move.getP1()), cell2 = getCell(move.getP2());
		if (cell1.getCandy().getCandyType().isSpecial() && cell2.getCandy().getCandyType().isSpecial())
			return true;
		else if (cell1.getCandy().getCandyType().equals(CandyType.BOMB)
				&& cell2.getCandy().getCandyType().equals(CandyType.NORMAL))
			return true;
		else if (cell1.getCandy().getCandyType().equals(CandyType.NORMAL)
				&& cell2.getCandy().getCandyType().equals(CandyType.BOMB))
			return true;
		
		// Swap values and check if the tiles form a match.
		Candy tmp = cell1.getCandy();
		cell1.setCandy(cell2.getCandy());
		cell2.setCandy(tmp);
		boolean isValid = tileFormsMatch(move.getP1()) || tileFormsMatch(move.getP2());
		return isValid;
	}
	
	private boolean sameColourWithCell(Cell c, CandyColour colour) {
		if (c.getCandy() == null) return false;
		return c.getCandy().getColour().equals(colour);
	}
	
	private void breakAllOf(CandyColour colour) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				if (sameColourWithCell(board[i][j], colour)) 
					trigger(i, j);
			}
		}
	}

	private void replaceWithSpecialAllOf(CandyColour colourMatched, CellType typeToReplace) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				// board[i][j].makeSpecial(typeToReplace());
				if (sameColourWithCell(board[i][j], colourMatched))
					trigger(i, j);
			}
		}
	}

	private boolean hasSpecial(Position pos) {
		return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().isSpecial(); 
	}
	
	private int proceedState = 0;
	
	public void makeMove(Move move) throws InvalidMoveException {
		if (!isMoveValid(move))
			throw new InvalidMoveException();
		lastMove = move;
		makeSmallMove();
		// if (hasSpecial(move.getP1()) && hasSpecial(move.getP2())) combine them
		// if (oneIsBomb() && otherRegular()) then detonate break.
		// else if (oneIsBomb() && otherSpecial()) then replace with Special.

	}
	
	
	public boolean makeSmallMove() {
		if (proceedState == 0) {
			markAndReplaceMatchingTiles();
			lastMove = null;
			if (!wasSomethingPopped) return false;
		}
		else if (proceedState == 1) detonateAllPending();
		else if (proceedState == 2) bringDownCandies();
		proceedState = (proceedState + 1) % 3;
		return true;
	}
	
	private void proceed() {
		wasSomethingPopped = false;
		markAndReplaceMatchingTiles();
		detonateAllPending();
		bringDownCandies();
		lastMove = null;
		if (wasSomethingPopped) proceed();
	}
	
	// Returns a list of all valid moves. 
	public List<Move> getValidMoves() {
		List<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				Move move1 = new Move(new Position(i, j), new Position(i+1, j));
				if (isMoveValid(move1)) moves.add(move1);
				Move move2 = new Move(new Position(i, j), new Position(i, j+1));
				if (isMoveValid(move2)) moves.add(move2);
			}
		}
		return moves;
	}

	private String candyColorToString(CandyColour candy) {
		switch (candy) {
		case RED: return "R";
		case GREEN: return "G";
		case YELLOW: return "Y";
		case ORANGE: return "O";
		case BLUE: return "B";
		case PURPLE: return "P";
		}
		return "";
	}
	
	private String candyTypeToString(CandyType candy) {
		switch (candy) {
		case NORMAL: return "N";
		case VERTICALLY_STRIPPED: return "V";
		case HORIZONTALLY_STRIPPED: return "H";
		case WRAPPED: return "W";
		}
		return "";
	}
	
	private String cellToString(int i, int j) {
		Cell cell = getCell(i, j);
		if (cell.hasCandy()) {
			return candyTypeToString(cell.getCandy().getCandyType()) + candyColorToString(cell.getCandy().getColour());
		}
		return "EE";
	}
	
	public void debugBoard() {
		System.out.println();
		System.out.print("    ");
		for (int i = 0; i < width; ++i) System.out.print("  " + i + " ");
		System.out.println();
		for (int i = 0; i < width; ++i) {
			System.out.print(" " + i + " ");
			for (int j = 0; j < height; ++j) {
				System.out.print(cellToString(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	/*
	public List<Match> getMatches() {
		List<Match> matches = new ArrayList();
		List<Coordinates> matched = new ArrayList();

		// since we check downwards and rightwards we don't need to check last
		// two rows/columns as min length is 3;
		for (int x = 0; x < width - 2; x++) {
			for (int y = 0; y < height - 2; y++) {
				// TODO: check that .contains actually matches different
				// instances with the same values;
				if (matched.contains(new Coordinates(x, y)))
					continue;
				int height = 1;
				int width = 1;

				CandyColour colour = getCell(x, y).getCandy().getColour();

				while (getCell(x + 1 + width, y).getCandy().getColour() == colour) {
					width++;
				}
				while (getCell(x, y + 1 + height).getCandy().getColour() == colour) {
					height++;
				}

				// TODO: there is quite a lot of repetition in those if
				// statements parphaps this can be optimised
				if (height > 2 && width > 2) {
					Coordinates[] cells = new Coordinates[height + width - 1];
					for (int i = 0; i < width; i++) {
						Coordinates match = new Coordinates(x + i, y);
						cells[i] = match;
						matched.add(match);
					}
					for (int j = 1; j < height; j++) { // start from one since
														// we don't want to
														// include original
														// candy twice
						Coordinates match = new Coordinates(x, y + j);
						cells[width + j] = match;
						matched.add(match);
					}
					matches.add(new Match(cells, true));
				} else if (width > 2) {
					Coordinates[] cells = new Coordinates[height + width - 1];
					for (int i = 0; i < width; i++) {
						Coordinates match = new Coordinates(x + i, y);
						cells[i] = match;
						matched.add(match);
					}
					matches.add(new Match(cells));
				} else if (height > 2) {
					Coordinates cells[] = new Coordinates[height];
					for (int j = 0; j < height; j++) {
						Coordinates match = new Coordinates(x, y + j);
						cells[j] = match;
						matched.add(match);
					}
					matches.add(new Match(cells));
				}

			}
		}

		return matches;
	} */
}