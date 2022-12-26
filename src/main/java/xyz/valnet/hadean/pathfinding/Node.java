package xyz.valnet.hadean.pathfinding;

import xyz.valnet.engine.math.Vector2i;

public class Node {
  public int x, y, g, h;
  public Node from;

  public int getCost() {
    return g + h;
  }

  public Vector2i getPosition() {
    return new Vector2i(x, y);
  }
}
