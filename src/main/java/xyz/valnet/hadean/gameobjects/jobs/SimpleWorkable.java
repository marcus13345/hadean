package xyz.valnet.hadean.gameobjects.jobs;

import java.io.Serializable;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.interfaces.IWorkable;

public class SimpleWorkable implements IWorkable {

  private final String name;
  private float work = 0;
  private float MAX_WORK = 0;
  private IProgressUpdateCallback callback;
  private IGetPositionsFunction positions;

  @FunctionalInterface
  public interface IGetPositionsFunction extends Serializable {
    public Vector2i[] get();
  }

  @FunctionalInterface
  public interface IProgressUpdateCallback extends Serializable {
    public void progress(float progress);
  }

  public SimpleWorkable(String name, float maxWork, IGetPositionsFunction positionsFunction, IProgressUpdateCallback callback) {
    this.name = name;
    this.MAX_WORK = maxWork;
    this.positions = positionsFunction;
    this.callback = callback;
  }

  @Override
  public boolean doWork(float dTime) {
    work += dTime;
    callback.progress(work / MAX_WORK);
    return work >= MAX_WORK;
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return positions.get();
  }

  @Override
  public final String getJobName() {
    return name;
  }
  
}
