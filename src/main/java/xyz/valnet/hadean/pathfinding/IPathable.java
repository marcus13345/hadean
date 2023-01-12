package xyz.valnet.hadean.pathfinding;

public interface IPathable {
  public boolean isWalkable(int x, int y);
  public boolean isOutOfBounds(int x, int y);
}
