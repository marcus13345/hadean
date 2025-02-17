package xyz.valnet.engine.math;

import java.io.Serializable;

public class Vector4f implements Serializable {

	public float x, y, z, w;

	public Vector4f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		w = 0.0f;
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

  public static Vector4f one  = new Vector4f(1f, 1f, 1f, 1f);
  public static Vector4f black = new Vector4f(0f, 0f, 0f, 1f);
  public static Vector4f zero = new Vector4f(0f, 0f, 0f, 0f);

  public static Vector4f opacity(float w) {
    return new Vector4f(1, 1, 1, w);
  }

  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
  }

  public boolean contains(float x, float y) {
    return x >= this.x && x < this.x + this.z && y >= this.y && y < this.y + this.w;
  }

  public Vector4f toAABB() {
    return new Vector4f(
      x,
      y,
      x + z,
      y + w
    );
  }

  public Vector4f toXYWH() {
    return new Vector4f(
      x,
      y,
      z - x,
      w - y
    );
  }

  public Vector4i asInt() {
    return new Vector4i((int)x, (int)y, (int)z, (int)w);
  }

  public Vector4f pad(float all) {
    return new Vector4f(x + all, y + all, z - all * 2, w - all * 2);
  }

  public Vector4f pad(float top, float bottom, float left, float right) {
    return new Vector4f(x + left, y + top, z - left - right, w - top - bottom);
  }

  public Vector4f copy() {
    return new Vector4f(x, y, z, w);
  }
}
