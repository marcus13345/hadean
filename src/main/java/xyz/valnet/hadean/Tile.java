package xyz.valnet.hadean;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.util.Assets;

// TODO make these tiles REAL gameobjects...
public class Tile {
  private final int x, y;
  private final Vector4f color = new Vector4f((float) Math.random() * 0.1f, 0.4f + (float) Math.random() * 0.15f, (float) Math.random() * 0.05f, 1f);
  private final Sprite sprite = Assets.defaultTerrain[(int)Math.floor(Math.random() * Assets.defaultTerrain.length)];
  private boolean obstacle;

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
    this.obstacle = false;
    // this.obstacle = Math.random() > 0.8f;
  }

  public void render(Camera camera) {
    Assets.flat.pushColor(isWalkable() ? color : new Vector4f(0.1f, 0.1f, 0.1f, 1f));
    camera.draw(sprite, x, y);
    // Drawing.drawSprite(sprite, Terrain.left + x * Terrain.TILE_SIZE, Terrain.top + y * Terrain.TILE_SIZE);
    Assets.flat.popColor();
  }

  public void wall() {
    obstacle = true;
  }

  public boolean isWalkable() {
    return !obstacle;
  }
}
