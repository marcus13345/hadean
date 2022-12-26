package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Vector2i;

public interface IWorkable {
  public boolean doWork();
  public Vector2i[] getWorkablePositions();
  public String getJobName();
}
