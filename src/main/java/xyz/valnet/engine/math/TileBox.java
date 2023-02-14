package xyz.valnet.engine.math;

import java.io.Serializable;

public class TileBox implements Serializable {
  public final int x, y;
  public final int w, h;

  public final Vector2i topLeft;

  @FunctionalInterface
  public interface TileCallback {
    public void apply(int x, int y);
  }

  public TileBox(int x, int y, int w, int h) {
    if(w < 0) {
      x += w + 1;
      w *= -1;
    }
    if(h < 0) {
      y += h + 1;
      h *= -1;
    }
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    topLeft = new Vector2i(x, y);
  }

  public TileBox(Vector2i pos, Vector2i dim) {
    this(pos.x, pos.y, dim.x, dim.y);
  }

  public TileBox(int x, int y, Vector2i dim) {
    this(x, y, dim.x, dim.y);
  }

  public TileBox(Vector2i pos, int w, int h) {
    this(pos.x, pos.y, w, h);
  }

  public void forEach(TileCallback cb) {
    for(int i = 0; i < w; i ++) {
      for(int j = 0; j < h; j ++) {
        cb.apply(x + i, y + j);
      }
    }
  }

  public static TileBox fromPoints(int x, int y, int x2, int y2) {
    return new TileBox(
      x <= x2 ? x : x2,
      y <= y2 ? y : y2,
      x <= x2 ? (x2 - x + 1) : (x - x2 + 1),
      y <= y2 ? (y2 - y + 1) : (y - y2 + 1)
    );
  }

  public static TileBox fromPoints(Vector2i a, int x2, int y2) {
    return fromPoints(a.x, a.y, x2, y2);
  }

  public static TileBox fromPoints(int x, int y, Vector2i b) {
    return fromPoints(x, y, b.x, b.y);
  }

  public static TileBox fromPoints(Vector2i a, Vector2i b) {
    return fromPoints(a.x, a.y, b.x, b.y);
  }

  public Box asBox() {
    return new Box(x, y, w, h);
  }
}
