package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.detail.Detail;

public abstract class Buildable extends WorldObject implements IBuildable, ITileThing, ISelectable {

  protected Vector2i getDimensions() {
    return new Vector2i(1, 1);
  }

  @Override
  public void buildAt(int x, int y, int w, int h) {
    setPosition(x, y, w, h);
  }

  @Override
  public void buildAt(int x, int y) {
    Vector2i dim = getDimensions();
    setPosition(x, y, dim.x, dim.y);
  }

  @Override
  public void onPlaced(Tile tile) {}

  @Override
  public Action[] getActions() {
    // TODO Auto-generated method stub
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {
    
  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {};
  }
}
