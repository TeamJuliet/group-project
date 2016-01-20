public class State {
  public State getCell(int x, int y);
  public State isLegal(int x, int y, boolean direction);
  public void makeMove(int x, int y, boolean direction);
}
