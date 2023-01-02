package xyz.valnet.hadean.pathfinding;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Stack;

public class Path implements Iterable<Node>, Serializable {
  private Stack<Node> nodes;
  public final int cost;
  public final Node dst;

  public Path(Stack<Node> nodes, Node dst) {
    this.nodes = nodes;
    this.cost = dst.getCost();
    this.dst = dst;
  }

  public int getLength() {
    return nodes.size();
  }

  public Node peek() {
    return nodes.peek();
  }

  public Node pop() {
    return nodes.pop();
  }

  public Node getDestination() {
    return nodes.firstElement();
  }

  public boolean isComplete() {
    return nodes.isEmpty();
  }

  @Override
  public Iterator<Node> iterator() {
    return nodes.iterator();
  }
}
