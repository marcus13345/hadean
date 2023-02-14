package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.TileBox;
import xyz.valnet.hadean.gameobjects.terrain.Tile;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.detail.Detail;

public abstract class Buildable extends WorldObject implements IBuildable, ITileThing, ISelectable {

  @Override
  public void buildAt(TileBox box) {
    setPosition(box.asBox());
  }

  @Override
  public void onPlaced(Tile tile) {}

  @Override
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {
    
  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {};
  }

  @Override
  public String getBuildTabName() {
    return getName();
  }
}
