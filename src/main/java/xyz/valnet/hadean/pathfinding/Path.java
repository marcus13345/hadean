package xyz.valnet.hadean.pathfinding;

import java.util.Iterator;
import java.util.Stack;

public class Path implements Iterable<Node> {
  private Stack<Node> nodes;
  public final int cost;

  public Path(Stack<Node> nodes, int cost) {
    this.nodes = nodes;
    this.cost = cost;
  }

  public Node peek() {
    return nodes.peek();
  }

  public Node pop() {
    return nodes.pop();
  }

  public boolean isComplete() {
    return nodes.isEmpty();
  }

  @Override
  public Iterator<Node> iterator() {
    return nodes.iterator();
  }
}
