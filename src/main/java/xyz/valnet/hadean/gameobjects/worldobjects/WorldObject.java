package xyz.valnet.hadean.gameobjects.worldobjects;

import java.util.HashSet;
import java.util.Set;

import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.terrain.Terrain;
import xyz.valnet.hadean.gameobjects.terrain.Tile;
import xyz.valnet.hadean.interfaces.ITileThing;

public abstract class WorldObject extends GameObject {
  
  // TODO make it just a box lawl
  private int x;
  private int y;
  private int w;
  private int h;

  protected Camera camera;
  protected Terrain terrain;

  private Set<Tile> linkedTiles;

  @Override
  protected void ready() {
    if(linkedTiles == null) linkedTiles = new HashSet<Tile>();
  }

  @Override
  protected void connect() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
  }

  @Override
  protected void start() {
    setPosition(x, y, w, h);
  }

  private void updateTileLinks(Set<Tile> tiles) {
    if(tiles == null || tiles.size() == 0) return;
    if(!(this instanceof ITileThing)) return;

    Set<Tile> removeTiles = new HashSet<Tile>();
    Set<Tile> addTiles = new HashSet<Tile>();

    for(Tile tile : tiles) {
      if(linkedTiles.contains(tile)) continue;
      addTiles.add(tile);
    }

    for(Tile tile : linkedTiles) {
      if(tiles.contains(tile)) continue;
      removeTiles.add(tile);
    }

    for(Tile tile : removeTiles) {
      linkedTiles.remove(tile);
      if(this instanceof ITileThing) {
        tile.removeThing((ITileThing) this);
      }
    }

    for(Tile tile : addTiles) {
      linkedTiles.add(tile);
      if(this instanceof ITileThing) {
        tile.placeThing((ITileThing) this);
      }
    }

    if(linkedTiles.size() == 0 && inScene()) {
      remove(this);
    }
    
    if(linkedTiles.size() != 0 && !inScene()) {
      add(this);
    }

  }

  protected void setPosition(Vector4i vector) {
    setPosition(vector.x, vector.y, vector.z, vector.w);
  }

  protected void setPosition(Vector2i vector) {
    setPosition(vector.x, vector.y);
  }

  protected void setPosition(int x, int y) {
    setPosition(x, y, 1, 1);
  }

  protected void setPosition(Box box) {
    setPosition((int) box.x, (int) box.y, (int) box.w, (int) box.h);
  }

  protected void setPosition(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    if(terrain == null) return;

    Set<Tile> tiles = new HashSet<Tile>();
    for(int i = 0; i < w; i ++) {
      for(int j = 0; j < h; j ++) {
        tiles.add(terrain.getTile(x + i, y + j));
      }
    }

    updateTileLinks(tiles);
  }

  public Tile getTile() {
    return terrain.getTile(x, y);
  }

  public Set<Tile> getTiles() {
    return linkedTiles;
  }

  public Vector4i getWorldPosition() {
    return new Vector4i(x, y, w, h);
  }

  public abstract String getName();
  
  public Box getWorldBox() {
    return new Box(x, y, w, h);
  }

}
