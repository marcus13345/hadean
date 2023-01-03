package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class Boulder extends Item {

  public Boulder(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean haulOnCreate() {
    return false;
  }

  @Override
  public void render() {
    camera.draw(Layers.GROUND, Assets.bigRock, x, y);
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
  public Detail[] getDetails() {
    return new Detail[] {};
  }

  @Override
  public String getName() {
    return "Boulder";
  }
  
}
