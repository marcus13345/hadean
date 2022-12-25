package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Stockpile extends WorldObject implements ITileThing, ISelectable {

  private WorldObject thing;

  public boolean isFree() {
    return thing == null;
  }

  @Override
  public void render() {
    camera.draw(Layers.GROUND, Assets.stockpile, x, y);
  }

  @Override
  public boolean isWalkable() {
    return true;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
    
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x+1, y+1);
  }

  @Override
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {
  }

  @Override
  public String details() {
    return "";
  }
  
}
