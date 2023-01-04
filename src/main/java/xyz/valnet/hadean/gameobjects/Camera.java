package xyz.valnet.hadean.gameobjects;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorldBoundsAdapter;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Camera extends GameObject {

  private int tileWidth = 16;
  // TODO link these in some way to the real resolution. lot of work here.
  private int screenWidth = 1024, screenHeight = 576;

  private Vector2f focus = new Vector2f(0, 0);

  private float minY, maxY;

  @Override
  public void start() {
    IWorldBoundsAdapter worldBoundsAdapter = get(IWorldBoundsAdapter.class);
    Vector4f bounds = worldBoundsAdapter.getWorldBounds();
    minY = bounds.y;
    maxY = bounds.w;
  }

  public void focus(float x, float y) {
    this.focus.x = x;
    this.focus.y = y;
  }

  public Vector2f world2screen(float x, float y) {
    return new Vector2f(x * tileWidth + screenWidth / 2 - focus.x * tileWidth, y * tileWidth + screenHeight / 2 - focus.y * tileWidth);
  }

  public Vector2f world2screen(Vector2f pos) {
    return world2screen(pos.x, pos.y);
  }

  public Vector2f screen2world(float x, float y) {
    return new Vector2f((x - screenWidth / 2 + focus.x * tileWidth) / tileWidth, (y - screenHeight / 2 + focus.y * tileWidth) / tileWidth);
  }

  public Vector4f world2Screen(Vector4f input) {
    return new Vector4f(
      input.x * tileWidth + screenWidth / 2 - focus.x * tileWidth,
      input.y * tileWidth + screenHeight / 2 - focus.y * tileWidth,
      input.z * tileWidth + screenWidth / 2 - focus.x * tileWidth,
      input.w * tileWidth + screenHeight / 2 - focus.y * tileWidth
    );
  }

  @Deprecated
  public void draw(Sprite sprite, float x, float y) {
    Vector2f screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), tileWidth, tileWidth);
  }

  @Deprecated
  public void draw(Sprite sprite, float x, float y, float w, float h) {
    Vector2f screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }

  public void draw(float layer, Sprite sprite, float x, float y) {
    draw(layer, sprite, x, y, 1, 1);
  }

  public void draw(float layer, Sprite sprite, Vector2f pos) {
    draw(layer, sprite, pos.x, pos.y, 1, 1);
  }

  public void draw(float layer, Sprite sprite, float x, float y, float w, float h) {
    Vector2f screenPos = world2screen(x, y);
    Drawing.setLayer(layer + (((y + h) - minY) / (maxY - minY)));
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }

  public void drawProgressBar(float progress, Vector4f worldBox) {
    int h = 6;
    Vector4i box = world2Screen(worldBox).toXYWH().asInt();
    Drawing.setLayer(Layers.GENERAL_UI);
    Assets.flat.pushColor(new Vector4f(0, 0, 0, 1));
    Assets.uiFrame.draw(box.x - h, box.y + box.w / 2 - h / 2, box.z + h * 2, h);
    Assets.flat.swapColor(new Vector4f(1, 1, 0, 1));
    Assets.fillColor.draw(box.x + 1 - h, box.y + 1 + box.w / 2 - h / 2, (int)Math.round(lerp(0, box.z - 3 + h * 2, progress)) + 1, h - 2);
    Assets.flat.popColor();
  }
  
}
