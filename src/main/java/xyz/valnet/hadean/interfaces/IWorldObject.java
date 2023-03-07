package xyz.valnet.hadean.interfaces;

import xyz.valnet.hadean.gameobjects.terrain.Tile;

public interface IWorldObject {
  public Tile getTile(int x, int y);
}
