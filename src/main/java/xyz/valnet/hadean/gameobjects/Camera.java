package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.scenegraph.GameObject;

public class Camera extends GameObject {

  private int tileWidth = 16;
  // TODO link these in some way to the real resolution.
  private int screenWidth = 1024, screenHeight = 576;

  private Vector2f focus = new Vector2f(0, 0);

  public void focus(float x, float y) {
    this.focus.x = x;
    this.focus.y = y;
  }

  public Vector2f world2screen(float x, float y) {
    return new Vector2f(x * tileWidth + screenWidth / 2 - focus.x * tileWidth, y * tileWidth + screenHeight / 2 - focus.y * tileWidth);
  }

  public void draw(Sprite sprite, float x, float y) {
    Vector2f screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), tileWidth, tileWidth);
  }

  public void draw(Sprite sprite, float x, float y, float w, float h) {
    Vector2f screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }
  
}
