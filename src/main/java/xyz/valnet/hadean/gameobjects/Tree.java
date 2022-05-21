package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Tree extends GameObject implements ITileThing, ISelectable {
  private Camera camera;

  private int x, y;

  public Tree(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void start() {
    camera = get(Camera.class);
  }

  @Override
  public void render() {
    camera.draw(Assets.tree, x - 1, y - 2, 3, 3);
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x - 1, y - 2, x + 2, y + 1);
  }

  public static final Action ACTION_CHOP = new Action("Chop");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_CHOP
    };
  }

  @Override
  public void runAction(Action action) {
    // TODO Auto-generated method stub
    
  }
}
