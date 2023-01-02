package xyz.valnet.hadean.util;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
  private final T t;
  private final U u;
  public Pair(T t, U u) {
    this.t = t;
    this.u = u;
  }
  public T first() {
    return t;
  }
  public U second() {
    return u;
  }
}
