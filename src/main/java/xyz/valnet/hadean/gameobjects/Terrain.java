package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.Tile;
import xyz.valnet.hadean.pathfinding.IPathable;
import xyz.valnet.hadean.scenes.GameScene;
 
// TODO SPLIT PATHABLES. | implements IPathable, the thing that has callbacks for interfacing with a pathfinder.
public class Terrain extends GameObject implements IPathable {

  public static final int WORLD_SIZE = 24;
  public static final int TILE_SIZE = 8;

  // public static int left, top;

  private Tile[][] tiles = new Tile[WORLD_SIZE][WORLD_SIZE];

  private Camera camera;

  public void start() {
    for (int i = 0; i < WORLD_SIZE; i++) {
      for (int j = 0; j < WORLD_SIZE; j++) {
        tiles[i][j] = new Tile(i, j);
        add(tiles[i][j]);
      }
    }
    camera = get(Camera.class);
    
    camera.focus(WORLD_SIZE / 2, WORLD_SIZE / 2);
  }

  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  @Override
  public void render() {
    // left = 400 - (WORLD_SIZE * TILE_SIZE / 2);
    // top = 225 - (WORLD_SIZE * TILE_SIZE / 2);

    // Drawing.setLayer(0f);
    // for (int i = 0; i < WORLD_SIZE; i++) {
    //   for (int j = 0; j < WORLD_SIZE; j++) {
    //     tiles[i][j].render(camera);
    //   }
    // }
  }

  @Override
  public boolean isWalkable(int x, int y, int fromX, int fromY) {
    return getTile(x, y).isWalkable();
  }

  @Override
  public boolean isInBounds(int x, int y) {
    return x < 0 || y < 0 || x >= WORLD_SIZE || y >= WORLD_SIZE;
  }

}
