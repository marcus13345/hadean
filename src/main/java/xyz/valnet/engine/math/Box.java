package xyz.valnet.engine.math;

import java.io.Serializable;

public class Box implements Serializable {

  public final float x, y, w, h, x2, y2;
  public final Vector2f a, b;
  public final Vector2f pos, dim;

  public static final Box none = new Box(0, 0, 0, 0);
  
  public Box(float x, float y, float w, float h) {
    if(w < 0) {
      this.w = Math.abs(w);
      this.x = x + w;
    } else {
      this.x = x;
      this.w = w;
    }
    if(h < 0) {
      this.h = Math.abs(h);
      this.y = y + h;
    } else {
      this.y = y;
      this.h = h;
    }
    this.x2 = this.x + this.w;
    this.y2 = this.y + this.h;
    this.a = new Vector2f(this.x, this.y);
    this.b = new Vector2f(this.x2, this.y2);
    this.pos = this.a;
    this.dim = new Vector2f(this.w, this.h);
  }
  
  public Box(Vector2f pos, float w, float h) {
    this(pos.x, pos.y, w, h);
  }
  
  public Box(float x, float y, Vector2f dim) {
    this(x, y, dim.x, dim.y);
  }
  
  public Box(Vector2f pos, Vector2f dim) {
    this(pos.x, pos.y, dim.x, dim.y);
  }
  
  public Box(Vector2i pos, float w, float h) {
    this(pos.x, pos.y, w, h);
  }
  
  public Box(float x, float y, Vector2i dim) {
    this(x, y, dim.x, dim.y);
  }
  
  public Box(Vector2i pos, Vector2i dim) {
    this(pos.x, pos.y, dim.x, dim.y);
  }

  public static Box fromPoints(Vector2i a, Vector2i b) {
    return new Box(a.x, a.y, b.x - a.x, b.y - a.y);
  }

  public static Box fromPoints(Vector2i a, float x2, float y2) {
    return new Box(a.x, a.y, x2 - a.x, y2 - a.y);
  }

  public static Box fromPoints(float x, float y, Vector2i b) {
    return new Box(x, y, b.x - x, b.y - y);
  }

  public static Box fromPoints(Vector2f a, Vector2f b) {
    return new Box(a.x, a.y, b.x - a.x, b.y - a.y);
  }

  public static Box fromPoints(Vector2f a, float x2, float y2) {
    return new Box(a.x, a.y, x2 - a.x, y2 - a.y);
  }

  public static Box fromPoints(float x, float y, Vector2f b) {
    return new Box(x, y, b.x - x, b.y - y);
  }

  public static Box fromPoints(float x, float y, float x2, float y2) {
    return new Box(x, y, x2 - x, y2 - y);
  }

  public static Box fromPoints(Vector2f a, Vector2i b) {
    return new Box(a.x, a.y, b.x - a.x, b.y - a.y);
  }

  public static Box fromPoints(Vector2i a, Vector2f b) {
    return new Box(a.x, a.y, b.x - a.x, b.y - a.y);
  }

  public Box copy() {
    return new Box(x, y, w, h);
  }

  public boolean contains(float x, float y) {
    return x >= this.x && x < this.x2 && y >= this.y && y < this.y2;
  }

  public boolean contains(Vector2f pos) {
    return contains(pos.x, pos.y);
  }

  public boolean intersects(Box other) {
    boolean aLeftOfB = x2 <= other.x;
    boolean aRightOfB = x >= other.x2;
    boolean aAboveB = y >= other.y2;
    boolean aBelowB = y2 <= other.y;

    return !( aLeftOfB || aRightOfB || aAboveB || aBelowB );
  }

  public Vector2i[] getBorders() {

    // TODO this could be bad, idk man. maybe define an intbox...
    int x = (int) Math.round(this.x);
    int y = (int) Math.round(this.y);
    int w = (int) Math.round(this.w);
    int h = (int) Math.round(this.h);

    int size = 2 * w + 2 * h;
    Vector2i[] vecs = new Vector2i[size];

    // top / bottom row
    for(int i = 0; i < h; i ++) {
      vecs[i] = new Vector2i(x + i, y - 1);
      vecs[size - i - 1] = new Vector2i(x + i, y + h);
    }

    // middle pillars
    for(int i = 0; i < h; i ++) {
      vecs[w + i * 2] = new Vector2i(x - 1, y + i);
      vecs[w + i * 2 + 1] = new Vector2i(x + h, y + i);
    }

    return vecs;
  }

  public Box outset(float f) {
    return new Box(x - f, y - f, w + 2 * f, h + 2 * f);
  }

  public Box quantize() {
    return Box.fromPoints(
      (float) Math.floor(x),
      (float) Math.floor(y),
      (float) Math.ceil(x2),
      (float) Math.ceil(y2)
    );
  }
}
