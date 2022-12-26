package xyz.valnet.engine.math;

public class Vector2f {

	public float x, y;

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

}
