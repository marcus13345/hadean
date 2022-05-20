package xyz.valnet.hadean.pathfinding;

public interface IPathable {
  public boolean isWalkable(int x, int y, int fromX, int fromY);
  public boolean isInBounds(int x, int y);
}
