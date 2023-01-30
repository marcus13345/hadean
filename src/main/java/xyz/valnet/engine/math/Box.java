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

  public Box copy() {
    return new Box(x, y, w, h);
  }

  public boolean contains(float x, float y) {
    return x >= this.x && x < this.x2 && y >= this.y && y < this.y2;
  }
}
