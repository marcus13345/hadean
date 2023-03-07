package xyz.valnet.hadean.interfaces;

import java.io.Serializable;

import xyz.valnet.engine.math.Vector2i;

public interface IWorkable extends Serializable {
  public boolean doWork(float dTime);
  public Vector2i[] getWorkablePositions();
  // we should honestly switch to only have names of jobs not steps...
  @Deprecated
  public String getJobName();
}
