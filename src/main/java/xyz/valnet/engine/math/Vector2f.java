package xyz.valnet.engine.math;

import java.io.Serializable;

public class Vector2f implements Serializable {

	public float x, y;

  public static Vector2f zero = new Vector2f(0, 0);
  public static Vector2f north = new Vector2f(0, -1);
  public static Vector2f east = new Vector2f(1, 0);
  public static Vector2f south = new Vector2f(0, 1);
  public static Vector2f west = new Vector2f(-1, 0);

	public Vector2f() {
		x = 0.0f;
		y = 0.0f;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

  public Vector2i asInt() {
    return new Vector2i((int)x, (int)y);
  }

  public boolean equals(Vector2f v) {
    return x == v.x && y == v.y;
  }

  public Vector2f add(Vector2f v) {
    return new Vector2f(x + v.x, y + v.y);
  }

  public Vector2f subtract(Vector2f v) {
    return new Vector2f(x - v.x, y - v.y);
  }

  public Vector2f normalize() {
    float r = (float) Math.sqrt(x*x + y*y);
    if(r == 0) return Vector2f.zero;
    return new Vector2f(x / r, y / r);
  }

  public Vector2f multiply(float m) {
    return new Vector2f(x * m, y * m);
  }

  public String toString() {
    return "<" + x + ", " + y + ">";
  }

  public Vector2f clamp(Box box) {
    float x = Math.min(Math.max(this.x, box.x), box.x2);
    float y = Math.min(Math.max(this.y, box.y), box.y2);
    return new Vector2f(x, y);
  }
}
