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

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

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
		/*
		 * System.out.println("Board before analysis: "); debugBoard();
		 * System.out.println("Analysis for " + pos.getX() + " " + pos.getY());
		 */
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
		// System.out.println("s_x: " + start_x + " e_x: " + end_x + " s_y: " +
		// start_y + " e_y: " + end_y);
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
		if (!inBoard(new Position(x, y))) return;
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
		// TODO(Dimitrios): make this more concise.
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// Do not consider EMPTY cells.
				if (board[x][y].getCellType() == CellType.EMPTY)
					continue;
				CandyColour colour = board[x][y].getCandy().getColour();
				SingleTileAnalysis analysis = analyzeTile(new Position(x, y));
				// In case there is a horizontal match.
				if (analysis.getLengthX() > 2) {
					// System.out.println("Found a horizontal at (" + x + ", " +
					// y + ")");
					boolean foundVertical = false;
					// If it forms a bomb there is no need to place wrapped.
					if (analysis.getLengthX() >= 5)
						foundVertical = true;
					for (int k = analysis.start_x; k <= analysis.end_x; ++k) {
						SingleTileAnalysis childAnalysis = analyzeTile(new Position(k, y));
						if (childAnalysis.getLengthY() > 2) {
							// This candy will be replaced by a wrapped one if
							// it is the first junction.
							if (!foundVertical) {
								foundVertical = true;
								// Wrap the candy at position (k, y).
								board[k][y].setCandy(new Candy(colour, CandyType.WRAPPED));
							} else {
								// Otherwise just empty that candy.
								trigger(k, y);
							}
							// Iterate through the vertical column and trigger
							// them.
							for (int yy = childAnalysis.start_y; yy <= childAnalysis.end_y; ++yy) {
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
					if (analysis.getLengthX() == 3)
						continue;
					else if (!foundVertical) {
						// If last move is in the range we went through, then
						// make
						// that one the stripped candy.
						if (lastMove == null || !moveInHorizontalRange(lastMove, analysis.start_x, analysis.end_x)) {
							// Make the middle candy vertically stripped.
							board[x + 1][y].setCandy(new Candy(colour, CandyType.VERTICALLY_STRIPPED));
						} else {
							int coordinate;
							// If one of the positions has the same
							// y-coordinate.
							if (lastMove.getP1().getY() == y)
								coordinate = lastMove.getP1().getX();
							else
								coordinate = lastMove.getP2().getX();
							board[coordinate][y].setCandy(new Candy(colour, CandyType.VERTICALLY_STRIPPED));
						}
					} else {
						// TODO: Fix to be aligned with the last move.
						if (analysis.getLengthX() >= 5)
							board[x + 2][y].changeCandyType(CandyType.BOMB);
					}
				}
				/* Symmetric case for the vertical matches. */
				else if (analysis.getLengthY() > 2) {
					boolean foundHorizontal = false;
					// If it forms a bomb there is no need to place wrapped.
					if (analysis.getLengthY() >= 5)
						foundHorizontal = true;
					for (int k = analysis.start_y; k <= analysis.end_y; ++k) {
						SingleTileAnalysis childAnalysis = analyzeTile(new Position(x, k));
						if (childAnalysis.getLengthY() > 2) {
							// This candy will be replaced by a wrapped one if
							// it is the first junction.
							if (!foundHorizontal) {
								foundHorizontal = true;
								// Wrap the candy at position (k, y).
								board[x][k].setCandy(new Candy(colour, CandyType.WRAPPED));
							} else {
								// Otherwise just empty that candy.
								trigger(x, k);
							}
							// Iterate through the vertical column and trigger
							// them.
							for (int xx = childAnalysis.start_x; xx <= childAnalysis.end_x; ++xx) {
								if (xx == x)
									continue;
								trigger(xx, k);
							}
						} else {
							// If there is no vertical match formed then just
							// trigger the cell.
							trigger(x, k);
						}
					}
					if (analysis.getLengthY() == 3)
						continue;
					else if (!foundHorizontal) {
						// If last move is in the range we went through, then
						// make that one the stripped candy.
						if (lastMove == null || !moveInVerticalRange(lastMove, analysis.start_y, analysis.end_y)) {
							// Make the middle candy vertically stripped.
							board[x][y+1].setCandy(new Candy(colour, CandyType.VERTICALLY_STRIPPED));
						} else {
							int coordinate;
							// If one of the positions has the same
							// x-coordinate.
							if (lastMove.getP1().getX() == x)
								coordinate = lastMove.getP1().getY();
							else
								coordinate = lastMove.getP2().getY();
							board[x][coordinate].setCandy(new Candy(colour, CandyType.VERTICALLY_STRIPPED));
						}
					} else {
						// TODO: Fix to be aligned with the last move.
						if (analysis.getLengthY() >= 5)
							board[x][y + 2].changeCandyType(CandyType.BOMB);
					}
				}

			}
		}
	}

	// Function that will detonate a wrapped candy.
	private void detonateWrapped(Position wrapped) {
		for (int i = wrapped.getX() - 1; i <= wrapped.getX() + 1; ++i) {
			for (int j = wrapped.getY() - 1; j <= wrapped.getY() + 1; ++j) {
				if (i == wrapped.getX() && j == wrapped.getY()) {
					continue;
				}
				trigger(i, j);
			}
		}
	}

	// Function that performs the clearing of the vertical line for the stripped candy.
	private void detonateVerticallyStripped(Position vStripped) {
		for (int y = 0; y < height; ++y) {
			trigger(vStripped.getX(), y);
		}
	}

	// Function that performs the clearing of the horizontal line for the stripped candy.
	private void detonateHorizontallyStripped(Position hStripped) {
		for (int x = 0; x < width; ++x) {
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
				// detonated.add(d);
			} else {
				detonatingCell.removeCandy();
			}
		}
	}

	// Brings candies down.
	private void bringDownCandies() {
		for (int i = 0; i < width; ++i) {
			for (int j = height - 1; j >= 1; --j) {
				if (board[i][j].getCellType().equals(CellType.EMPTY)) {
					int y = j - 1;
					// TODO: can be optimized.
					while(y >= 0 && board[i][y].getCellType().equals(CellType.EMPTY)) y--;
					// Replacement was found.
					if (y >= 0) { 
						board[i][j].setCandy(board[i][y].getCandy());
						board[i][y].removeCandy();
						if (board[i][j].getCandy().isDetonated()) {
							System.out.println("Moved Detonating Candy.");
							detonated.add(new Position(i, j));
						}
					}
				}
			}
		}
	}

	// Function that fills the board by requesting candies from the
	// candyGenerator.
	private void fillBoard() {
		int debugCount = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell cell = board[x][y];
				if (cell.getCellType() == CellType.EMPTY) {
					cell.setCandy(candyGenerator.getCandy());
					debugCount++;
				}
			}
		}
		System.out.println("The number of filled cells is " + debugCount);
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

	private boolean isPositionValidAndMoveable(Position pos) {
		return inBoard(pos) && board[pos.getX()][pos.getY()].isMoveable();
	}

	private Cell getCell(Position pos) {
		return board[pos.getX()][pos.getY()];
	}

	private void swapCandies(Move move) {
		Cell cell1 = getCell(move.getP1()), cell2 = getCell(move.getP2());
		// Swap values and check if the tiles form a match.
		Candy tmp = cell1.getCandy();
		cell1.setCandy(cell2.getCandy());
		cell2.setCandy(tmp);
	}
	
	private boolean isMoveValid(Move move) {
		if (!isPositionValidAndMoveable(move.getP1()) || !isPositionValidAndMoveable(move.getP2()))
			return false;
		Cell cell1 = getCell(move.getP1()), cell2 = getCell(move.getP2());
		if (cell1.getCandy().getCandyType().isSpecial() && cell2.getCandy().getCandyType().isSpecial())
			return true;
		// Exchanging a Bomb with a cell that has a moveable item is a valid move.
		else if (cell1.getCandy().getCandyType().equals(CandyType.BOMB) ||
				cell2.getCandy().getCandyType().equals(CandyType.BOMB))
			return true;

		swapCandies(move);
		boolean isValid = tileFormsMatch(move.getP1()) || tileFormsMatch(move.getP2());
		// Place candies as they were initially.
		swapCandies(move);
		return isValid;
	}

	private boolean sameColourWithCell(Cell c, CandyColour colour) {
		if (c.getCandy() == null || c.getCandy().getColour() == null)
			return false;
		return c.getCandy().getColour().equals(colour);
	}

	// Function that performs the combination of a bomb and a Normal Candy.
	private void breakAllOf(CandyColour colour) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				if (sameColourWithCell(board[i][j], colour))
					trigger(i, j);
			}
		}
	}

	// Function that performs the combination of a bomb and a Special Candy.
	private void replaceWithSpecialAllOf(CandyColour colourMatched, CandyType typeToReplace) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				if (sameColourWithCell(board[i][j], colourMatched)) {
					board[i][j].setCandy(new Candy(colourMatched, typeToReplace));
					trigger(i, j);
				}
			}
		}
	}

	// Function that checks whether the position contains a special candy.
	private boolean hasSpecial(Position pos) {
		return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().isSpecial();
	}
	
	// Function that checks whether the position contains a bomb.
	private boolean hasBomb(Position pos) {
		return getCell(pos).hasCandy() && getCell(pos).getCandy().getCandyType().equals(CandyType.BOMB);
	}

	private int proceedState = 0;

	public void makeMove(Move move) throws InvalidMoveException {
		if (!isMoveValid(move))
			throw new InvalidMoveException();
		// Record the last move.
		lastMove = move;
		swapCandies(move);
		Position p1 = move.getP1(), p2 = move.getP2();
		if (hasBomb(p1) && hasSpecial(p2)) {
			replaceWithSpecialAllOf(getCell(p2).getCandy().getColour(), getCell(p2).getCandy().getCandyType());
		} else if (hasBomb(p2) && hasSpecial(p1)) {
			replaceWithSpecialAllOf(getCell(p1).getCandy().getColour(), getCell(p1).getCandy().getCandyType());
		} else {
			makeSmallMove();
		}
		// TODO: Add the remaining cases.
		// makeSmallMove();
		// if (hasSpecial(move.getP1()) && hasSpecial(move.getP2())) combine
		// them
		// if (oneIsBomb() && otherRegular()) then detonate break.
		// else if (oneIsBomb() && otherSpecial()) then replace with Special.

	}

	private int countSmallMoves = 0;

	// Performs the corresponding action on each step (will be used by the DisplayBoard extension).
	public boolean makeSmallMove() {
		countSmallMoves++;
		System.out.println("We are on " + countSmallMoves);
		if (proceedState == 0) {
			System.out.println("1: Mark and replace tiles on");
			debugBoard();
			markAndReplaceMatchingTiles();
			lastMove = null;
			if (!wasSomethingPopped)
				return false;
		} else if (proceedState == 1) {
			detonateAllPending();
			System.out.println("2: Detonating all pending.");
		} else if (proceedState == 2) {
			System.out.println("3: Bringing down some candies (and filling board).");
			bringDownCandies();
			fillBoard();
			if (detonated.isEmpty()){
				wasSomethingPopped = false;
			} else {
				proceedState = 0;
			}
		}
		proceedState = (proceedState + 1) % 3;
		return true;
	}

	private void proceed() {
		wasSomethingPopped = false;
		markAndReplaceMatchingTiles();
		detonateAllPending();
		bringDownCandies();
		lastMove = null;
		if (wasSomethingPopped)
			proceed();
	}

	// Returns a list of all valid moves.
	public List<Move> getValidMoves() {
		List<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				Move move1 = new Move(new Position(i, j), new Position(i + 1, j));
				if (isMoveValid(move1))
					moves.add(move1);
				Move move2 = new Move(new Position(i, j), new Position(i, j + 1));
				if (isMoveValid(move2))
					moves.add(move2);
			}
		}
		return moves;
	}

	private String candyColorToString(CandyColour candy) {
		if (candy == null) return "B";
		switch (candy) {
		case RED:
			return "R";
		case GREEN:
			return "G";
		case YELLOW:
			return "Y";
		case ORANGE:
			return "O";
		case BLUE:
			return "B";
		case PURPLE:
			return "P";
		}
		return "";
	}

	private String candyTypeToString(CandyType candy) {
		switch (candy) {
		case NORMAL:
			return "N";
		case VERTICALLY_STRIPPED:
			return "V";
		case HORIZONTALLY_STRIPPED:
			return "H";
		case WRAPPED:
			return "W";
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
		System.out.print("   ");
		for (int i = 0; i < width; ++i)
			System.out.print(" " + i + " ");
		System.out.println();
		for (int i = 0; i < height; ++i) {
			System.out.print(" " + i + " ");
			for (int j = 0; j < width; ++j) {
				System.out.print(cellToString(j, i) + " ");
			}
			System.out.println();
		}
	}

	/*
	 * public List<Match> getMatches() { List<Match> matches = new ArrayList();
	 * List<Coordinates> matched = new ArrayList();
	 * 
	 * // since we check downwards and rightwards we don't need to check last //
	 * two rows/columns as min length is 3; for (int x = 0; x < width - 2; x++)
	 * { for (int y = 0; y < height - 2; y++) { // TODO: check that .contains
	 * actually matches different // instances with the same values; if
	 * (matched.contains(new Coordinates(x, y))) continue; int height = 1; int
	 * width = 1;
	 * 
	 * CandyColour colour = getCell(x, y).getCandy().getColour();
	 * 
	 * while (getCell(x + 1 + width, y).getCandy().getColour() == colour) {
	 * width++; } while (getCell(x, y + 1 + height).getCandy().getColour() ==
	 * colour) { height++; }
	 * 
	 * // TODO: there is quite a lot of repetition in those if // statements
	 * parphaps this can be optimised if (height > 2 && width > 2) {
	 * Coordinates[] cells = new Coordinates[height + width - 1]; for (int i =
	 * 0; i < width; i++) { Coordinates match = new Coordinates(x + i, y);
	 * cells[i] = match; matched.add(match); } for (int j = 1; j < height; j++)
	 * { // start from one since // we don't want to // include original //
	 * candy twice Coordinates match = new Coordinates(x, y + j); cells[width +
	 * j] = match; matched.add(match); } matches.add(new Match(cells, true)); }
	 * else if (width > 2) { Coordinates[] cells = new Coordinates[height +
	 * width - 1]; for (int i = 0; i < width; i++) { Coordinates match = new
	 * Coordinates(x + i, y); cells[i] = match; matched.add(match); }
	 * matches.add(new Match(cells)); } else if (height > 2) { Coordinates
	 * cells[] = new Coordinates[height]; for (int j = 0; j < height; j++) {
	 * Coordinates match = new Coordinates(x, y + j); cells[j] = match;
	 * matched.add(match); } matches.add(new Match(cells)); }
	 * 
	 * } }
	 * 
	 * return matches; }
	 */
}