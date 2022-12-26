package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Log extends WorldObject implements ITileThing, ISelectable {

  @Override
  public void start() {
    super.start();
  }

  public Log(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    camera.draw(Layers.GROUND, Assets.log, x, y);
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
  public void onRemove() {}

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

  private static final Action ACTION_HAUL = new Action("Haul");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_HAUL
    };
  }

  @Override
  public void runAction(Action action) {
    
  }

  @Override
  public String details() {
    return "A fat log";
  }

  @Override
  public String getName() {
    return "Log";
  }
  
}
