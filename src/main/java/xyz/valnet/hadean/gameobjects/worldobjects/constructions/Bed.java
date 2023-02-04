package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.util.Assets;

public class Bed extends Construction {

  @Override
  protected Vector2i getDimensions() {
    return new Vector2i(1, 2);
  }

  @Override
  public String getName() {
    return "Bed";
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
    
  }

  @Override
  protected IItemPredicate getBuildingMaterial() {
    return Log.LOG_PREDICATE;
  }

  @Override
  protected int getBuildingMaterialCount() {
    return 1;
  }

  @Override
  protected Sprite getDefaultSprite() {
    return Assets.bed;
  }
}
