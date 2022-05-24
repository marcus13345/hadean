package xyz.valnet.hadean.gameobjects;

import xyz.valnet.hadean.Tile;

public interface IHaulable extends IJob {
  public Log take();
  public Tile getDestination();
}
