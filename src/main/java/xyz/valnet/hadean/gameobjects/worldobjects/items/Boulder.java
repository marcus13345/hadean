package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class Boulder extends Item {

  public static IItemPredicate BOULDER_PREDICATE = (item) -> (item instanceof Boulder);

  public Boulder(int x, int y) {
    setPosition(x, y);
  }

  @Override
  public boolean haulOnCreate() {
    return false;
  }

  @Override
  public void render() {
    Vector2i pos = getWorldPosition().xy();
    camera.draw(Layers.GROUND, Assets.bigRock, pos.x, pos.y);
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
