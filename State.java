public class State {
  public State getCell(int x, int y);
  public State isLegal(int x, int y, boolean direction); //direction: true for right, false for down
  public void makeMove(int x, int y, boolean direction);
}
