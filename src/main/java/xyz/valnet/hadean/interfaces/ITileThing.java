package xyz.valnet.hadean.interfaces;

import xyz.valnet.hadean.gameobjects.Tile;

public interface ITileThing {
  public boolean isWalkable();
  public boolean shouldRemove();
  public void onRemove();
  public void onPlaced(Tile tile);
}
