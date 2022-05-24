package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector2i;

public interface IJob {
  public boolean hasWork();
  public Vector2i[] getWorablePositions();
  public Vector2i getLocation();
}
