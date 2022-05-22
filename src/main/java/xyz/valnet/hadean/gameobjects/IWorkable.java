package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector2i;

public interface IWorkable {
  public boolean hasWork();
  public Vector2i[] getWorablePositions();
  public void doWork();
  public Vector2i getLocation();
}
