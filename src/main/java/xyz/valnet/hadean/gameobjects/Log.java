package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.Layers;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Log extends GameObject implements ITileThing, ISelectable {

  private Camera camera;

  private int x, y;

  public Log(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void start() {
    camera = get(Camera.class);
  }

  @Override
  public void render() {
    Drawing.setLayer(Layers.GROUND);
    camera.draw(Assets.log, x, y);
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

  @Override
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {}

  @Override
  public String details() {
    return "A fat log";
  }
  
}
