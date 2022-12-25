package xyz.valnet.engine.math;

public class Vector4f {

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

}
