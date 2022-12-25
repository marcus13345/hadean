package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Vector2i;

public interface IJob {
  public boolean hasWork();
  public Vector2i[] getWorkablePositions();

  @Deprecated // please use the workable positions.
  public Vector2i getLocation();
  public String getJobName();
}
