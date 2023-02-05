package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.util.Assets;

public class MasonWorkshop extends Construction {

  @Override
  protected IItemPredicate getBuildingMaterial() {
    return Log.LOG_PREDICATE;
  }

  @Override
  protected int getBuildingMaterialCount() {
    return 1;
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public String getName() {
    return "Mason's Workshop";
  }

  @Override
  protected Sprite getDefaultSprite() {
    return Assets.testTile;
  }

  @Override
  public Vector2i getDimensions() {
    return new Vector2i(3, 3);
  }
  
}
