package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.FastNoiseLite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorldBoundsAdapter;
import xyz.valnet.hadean.pathfinding.IPathable;
 
public class Terrain extends GameObject implements IPathable, IWorldBoundsAdapter {

  public static final int WORLD_SIZE = 30;
  public static final int TILE_SIZE = 8;
  // public static int left, top;

  public float getNoise(int seed, float x, float y) {
    FastNoiseLite noise = new FastNoiseLite(seed);
    float base =      ((noise.GetNoise(x * 10, y * 10) + 1) / 2)   * 1.0f;
    float highnoise = ((noise.GetNoise(x * 100, y * 100) + 1) / 2) * 1.0f;
    return (base + highnoise) / 2.0f;
  }

  private Tile[][] tiles = new Tile[WORLD_SIZE][WORLD_SIZE];

  private Camera camera;

  @Override
  protected void create() {
    for (int i = 0; i < WORLD_SIZE; i++) {
      for (int j = 0; j < WORLD_SIZE; j++) {
        tiles[i][j] = new Tile(i, j);
        add(tiles[i][j]);
      }
    }
  }

  @Override
  protected void connect() {
    camera = get(Camera.class);
  }

  @Override
  protected void start() {
    camera.focus(WORLD_SIZE / 2, WORLD_SIZE / 2);
  }

  public Tile getTile(int x, int y) {
    if(x < 0 || y < 0) return null;
    if(x >= WORLD_SIZE || y >= WORLD_SIZE) return null;
    return tiles[x][y];
  }

  public Tile getTile(Vector2i pos) {
    return getTile(pos.x, pos.y);
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
