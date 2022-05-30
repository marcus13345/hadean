package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Path;

public interface IWorker {
  public Vector2f getLocation();
  public IPathfinder getPathfinder();
  public void setPath(Path path);
  public String getName();
}
