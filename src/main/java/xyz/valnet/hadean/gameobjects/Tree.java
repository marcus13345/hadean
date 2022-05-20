package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.util.Assets;

public class Tree extends GameObject implements ITileThing {
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
}
