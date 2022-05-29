package xyz.valnet.hadean.interfaces;

import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.Log;

public interface IHaulable extends IJob {
  public Log take();
  public Tile getDestination();
}
