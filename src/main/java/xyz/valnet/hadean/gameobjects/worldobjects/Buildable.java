package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ITileThing;

public abstract class Buildable extends WorldObject implements IBuildable, ITileThing {

  protected Vector2i getDefaultDimensions() {
    return new Vector2i(1, 1);
  }

  @Override
  public void buildAt(int x, int y, int w, int h) {
    setPosition(x, y, w, h);
  }

  @Override
  public void buildAt(int x, int y) {
    Vector2i dim = getDefaultDimensions();
    setPosition(x, y, dim.x, dim.y);
  }

  @Override
  public void onPlaced(Tile tile) {}
}
