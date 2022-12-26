package xyz.valnet.engine.math;

public class Vector2i {

	public int x, y;

	public Vector2i() {
		x = 0;
		y = 0;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

  public boolean equals(Vector2i other) {
    return x == other.x && y == other.y;
  }

  public boolean isOneOf(Vector2i[] others) {
    for(Vector2i other : others) {
      if(other.equals(this)) {
        return true;
      }
    }
    return false;
  }

  public float distanceTo(int x, int y) {
    int a = this.x - x;
    int b = this.y - y;
    return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
  }

  public Vector2f asFloat() {
    return new Vector2f(x, y);
  }

}
