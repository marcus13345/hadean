package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Box;

public interface IBuildLayerListener {
  
  public void update(Box box);
  public void build(Box box);

  public void cancel();
}
