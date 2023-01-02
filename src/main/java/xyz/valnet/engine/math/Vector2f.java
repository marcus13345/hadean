package xyz.valnet.engine.math;

import java.io.Serializable;

public class Vector2f implements Serializable {

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

  public Vector2f add(Vector2f v) {
    return new Vector2f(x + v.x, y + v.y);
  }

}
