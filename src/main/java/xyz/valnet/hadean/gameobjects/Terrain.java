package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorldBoundsAdapter;
import xyz.valnet.hadean.pathfinding.IPathable;
 
public class Terrain extends GameObject implements IPathable, IWorldBoundsAdapter {

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

    // Tile randomTile = getRandomTile();
    // Vector2i coords = randomTile.getCoords();
    // Stockpile stockpile = new Stockpile(coords.x, coords.y);
    // randomTile.placeThing(stockpile);

    camera = get(Camera.class);
    camera.focus(WORLD_SIZE / 2, WORLD_SIZE / 2);
  }

  public Tile getTile(int x, int y) {
    return tiles[x][y];
  }

  // TODO implement directionality. even the pathfinder doesnt give this info...
  @Override
  public boolean isWalkable(int x, int y, int fromX, int fromY) {
    if(!isOutOfBounds(x, y)) {
      return getTile(x, y).isWalkable();
    } else return false;
  }

  @Override
  public boolean isOutOfBounds(int x, int y) {
    return x < 0 || y < 0 || x >= WORLD_SIZE || y >= WORLD_SIZE;
  }

  @Override
  public Vector4f getWorldBounds() {
    return new Vector4f(0, 0, WORLD_SIZE, WORLD_SIZE);
  }

}
