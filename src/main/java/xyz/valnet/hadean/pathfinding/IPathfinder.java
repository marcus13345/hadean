package xyz.valnet.hadean.pathfinding;

import xyz.valnet.engine.math.Vector2i;

public interface IPathfinder {
  public Path getPath(int x1, int y1, int x2, int y2);
  public Path getBestPath(Vector2i src, Vector2i[] dsts);
}
