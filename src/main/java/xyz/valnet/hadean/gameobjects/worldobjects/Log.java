package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Log extends Item {
  

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
  public String details() {
    return "A fat log";
  }

  @Override
  public String getName() {
    return "Log";
  }
  
}
