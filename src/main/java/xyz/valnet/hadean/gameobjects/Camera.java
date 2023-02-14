package xyz.valnet.hadean.gameobjects;

import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.graphics.Tile9;
import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.TileBox;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.interfaces.IWorldBoundsAdapter;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

import static xyz.valnet.engine.util.Math.lerp;

public class Camera extends GameObject implements ITransient, IMouseCaptureArea {

  private int tileWidth = 16;
  // TODO link these in some way to the real resolution. lot of work here.
  private int screenWidth = 1024, screenHeight = 576;

  private Vector2f focus = new Vector2f(0, 0);

  private float minY, maxY;

  private Box focusBounds = null;

  @Override
  public void start() {
    IWorldBoundsAdapter worldBoundsAdapter = get(IWorldBoundsAdapter.class);
    Vector4f bounds = worldBoundsAdapter.getWorldBounds();
    minY = bounds.y;
    maxY = bounds.w;
  }

  public final void setFocusBounds(Box box) {
    this.focusBounds = box;
  }

  public final Vector2f getWorldMouse() {
    return screen2world(App.mouseX, App.mouseY);
  }

  @Override
  public void update(float dTime) {
    Vector2f direction = Vector2f.zero;
    if(dragOrigin == null) {
      if(getKey(87)) {
        direction = direction.add(Vector2f.north);
      }
      if(getKey(65)) {
        direction = direction.add(Vector2f.west);
      }
      if(getKey(83)) {
        direction = direction.add(Vector2f.south);
      }
      if(getKey(68)) {
        direction = direction.add(Vector2f.east);
      }
      
      Vector2f move = direction.normalize().multiply(dTime / 5f);
      
      focus = focus.add(move);
    } else {
      Vector2f dragDifference = screen2world(App.mouseX, App.mouseY).subtract(focus);
      focus = dragOrigin.subtract(dragDifference);
    }

    if(focusBounds != null) {
      focus = focus.clamp(focusBounds);
    }
  }

  public void focus(float x, float y) {
    this.focus.x = x;
    this.focus.y = y;
  }

  public final Box world2screen(Box box) {
    return Box.fromPoints(world2screen(box.a), world2screen(box.b));
  }

  public final Vector2i world2screen(float x, float y) {
    return new Vector2i((int)(x * tileWidth + screenWidth / 2 - focus.x * tileWidth), (int)(y * tileWidth + screenHeight / 2 - focus.y * tileWidth));
  }

  public final Vector2i world2screen(Vector2f pos) {
    return world2screen(pos.x, pos.y);
  }

  public final Vector2i world2screen(Vector2i pos) {
    return world2screen(pos.x, pos.y);
  }

  public final Vector2f screen2world(float x, float y) {
    return new Vector2f((x - screenWidth / 2 + focus.x * tileWidth) / tileWidth, (y - screenHeight / 2 + focus.y * tileWidth) / tileWidth);
  }

  public final Vector2f screen2world(Vector2f pos) {
    return screen2world(pos.x, pos.y);
  }

  // !! this takes an AABB and returns and AABB
  public final Vector4f world2screen(Vector4f input) {
    return new Vector4f(
      input.x * tileWidth + screenWidth / 2 - focus.x * tileWidth,
      input.y * tileWidth + screenHeight / 2 - focus.y * tileWidth,
      input.z * tileWidth + screenWidth / 2 - focus.x * tileWidth,
      input.w * tileWidth + screenHeight / 2 - focus.y * tileWidth
    );
  }

  @Deprecated
  public final void draw(Sprite sprite, float x, float y) {
    Vector2i screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (screenPos.x), (screenPos.y), tileWidth, tileWidth);
  }

  @Deprecated
  public final void draw(Sprite sprite, float x, float y, float w, float h) {
    Vector2i screenPos = world2screen(x, y);
    Drawing.drawSprite(sprite, (screenPos.x), (screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }

  public final void draw(float layer, Sprite sprite, float x, float y) {
    draw(layer, sprite, x, y, 1, 1);
  }

  public final void draw(float layer, Sprite sprite, Vector2f pos) {
    draw(layer, sprite, pos.x, pos.y, 1, 1);
  }

  public final void draw(float layer, Sprite sprite, Vector4i pos) {
    draw(layer, sprite, pos.x, pos.y, pos.z, pos.w);
  }

  public final void draw(float layer, Sprite sprite, float x, float y, float w, float h) {
    Vector2i screenPos = world2screen(x, y);
    Drawing.setLayer(layer + (((y + h) - minY) / (maxY - minY)));
    Drawing.drawSprite(sprite, (int)(screenPos.x), (int)(screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }

  @Deprecated
  public final void draw(float layer, Tile9 sprite, Box box) {
    draw(layer, sprite, box.x, box.y, box.w, box.h);
  }

  public final void draw(float layer, Tile9 sprite, TileBox box) {
    draw(layer, sprite, box.x, box.y, box.w, box.h);
  }

  public final void draw(float layer, Tile9 sprite, float x, float y, float w, float h) {
    Vector2i screenPos = world2screen(x, y);
    Drawing.setLayer(layer + (((y + h) - minY) / (maxY - minY)));
    sprite.draw((int)(screenPos.x), (int)(screenPos.y), (int)(tileWidth * w), (int)(tileWidth * h));
  }

  public final void drawProgressBar(float progress, Box worldBox) {
    int h = 6;
    Box box = world2screen(worldBox);
    Drawing.setLayer(Layers.GENERAL_UI);
    Assets.flat.pushColor(Color.black);
    Assets.uiFrame.draw((int)(box.x - h), (int)(box.y + box.h / 2 - h / 2), (int)(box.w + h * 2), h);
    Assets.flat.swapColor(Color.yellow);
    Assets.fillColor.draw((int)(box.x + 1 - h), (int)(box.y + 1 + box.h / 2 - h / 2), (int)Math.round(lerp(0, box.w - 3 + h * 2, progress)) + 1, h - 2);
    Assets.flat.popColor();
  }

  @Override
  public void mouseEnter() {}

  @Override
  public void mouseLeave() {}

  private Vector2f dragOrigin = null;

  @Override
  public void mouseDown(int button) {
    if(button == 2) {
      dragOrigin = screen2world(App.mouseX, App.mouseY);
    }
  }

  @Override
  public void mouseUp(int button) {
    if(button == 2) {
      dragOrigin = null;
    }
  }

  @Override
  public List<Box> getGuiBoxes() {
    return List.of(Box.none);
  }

  @Override
  public float getLayer() {
    return 0;
  }

  @Override
  public void scrollDown() {
    tileWidth /= 2;
    tileWidth = Math.max(tileWidth, 8);
  }

  @Override
  public void scrollUp() {
    tileWidth *= 2;
    tileWidth = Math.min(tileWidth, 32);
  }
  
}
