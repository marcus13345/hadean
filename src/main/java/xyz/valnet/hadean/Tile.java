package xyz.valnet.hadean;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.util.Assets;

public class Tile {
  private final int x, y;
  private final Vector4f color = new Vector4f((float) Math.random() * 0.1f, 0.4f + (float) Math.random() * 0.15f, (float) Math.random() * 0.05f, 1f);
  private final Sprite sprite = Assets.defaultTerrain[(int)Math.floor(Math.random() * Assets.defaultTerrain.length)];
  private final boolean obstacle;

  public Tile(int x, int y) {
    float distanceFromOrigin = 0.9f - (float)Math.sqrt(x * x + y * y) / 42;
    this.x = x;
    this.y = y;
    this.obstacle = Math.random() > 0.8f;
  }

  public void render() {
    Assets.flat.pushColor(isWalkable() ? color : new Vector4f(0.1f, 0.1f, 0.1f, 1f));
    Drawing.drawSprite(sprite, Terrain.left + x * Terrain.TILE_SIZE, Terrain.top + y * Terrain.TILE_SIZE);
    Assets.flat.popColor();
  }

  public boolean isWalkable() {
    return !obstacle;
  }
}
