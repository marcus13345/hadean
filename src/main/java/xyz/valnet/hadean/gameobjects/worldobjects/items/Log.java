package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

// TODO haul on placed in non stockpile. we could have a situation
// where the job gets cancelled, because a pawn has the item
// while it wants to be hauled. thats fine, but what if the pawn then drops
// the item? it should still be hauled. ergo, we should autohaul
// when placed in a non stockpile, not just on create.
public class Log extends Item {

  public static IItemPredicate LOG_PREDICATE = (item) -> (item instanceof Log);

  public Log(int x, int y) {
    setPosition(x, y);
  }

  @Override
  public void render() {
    Vector2i pos = getWorldPosition().xy();
    camera.draw(Layers.GROUND, Assets.log, pos.x, pos.y);
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
    return "Log";
  }
  
}
