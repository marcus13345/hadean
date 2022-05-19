package xyz.valnet.hadean.pathfinding;

public class Node {
  public int x, y, g, h;
  public Node from;

  public int getCost() {
    return g + h;
  }
}
