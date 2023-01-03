package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.Tile;

public abstract class WorldObject extends GameObject {
  
  protected float x;
  protected float y;
  protected float w;
  protected float h;

  protected Camera camera;
  protected Terrain terrain;

  @Override
  protected void connect() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
  }

  public Tile getTile() {
    return terrain.getTile((int)x, (int)y);
  }

  public Vector2f getWorldPosition() {
    return new Vector2f(x, y);
  }

  public abstract String getName();
  public abstract Vector4f getWorldBox();

}
