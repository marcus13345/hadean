package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.TileBox;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;

public interface IItemSpawner extends IWorldObject {
  public abstract TileBox getSpawnableArea();

  public default void spawn(Item item) {
    var tiles = getSpawnableArea().getTiles();
    while(tiles.size() > 0) {
      int idx = (int) Math.floor(Math.random() * tiles.size());
      var pos = tiles.get(idx);
      getTile(pos.x, pos.y);
    }
  }
}
