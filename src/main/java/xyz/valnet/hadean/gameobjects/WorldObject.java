package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.scenegraph.GameObject;

public class WorldObject extends GameObject {
  
  protected float x;
  protected float y;

  protected Camera camera;
  protected Terrain terrain;

  @Override
  public void start() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
  }

}
