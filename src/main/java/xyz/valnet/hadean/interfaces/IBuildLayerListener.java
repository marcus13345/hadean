package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.TileBox;

public interface IBuildLayerListener {
  
  public void update(TileBox box);
  public void build(TileBox box);

  public void cancel();
}
