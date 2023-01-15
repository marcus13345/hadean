package xyz.valnet.engine.math;

import java.io.Serializable;

public class Vector4i implements Serializable {

	public int x, y, z, w;

	public Vector4i() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

  public Vector2i xy() {
    return new Vector2i(x, y);
  }

  // WORKS WITH WIDTH HEIGHT NOT AABB
  public Vector2i[] getInternals() {
    int size = z * w;
    Vector2i[] vecs = new Vector2i[size];
    for(int i = 0; i < z; i ++) {
      for(int j = 0; j < w; j ++) {
        vecs[i * z + j] = new Vector2i(x + i, y + j);
      }
    }
    return vecs;
  }

  // WORKS WITH WIDTH HEIGHT NOT AABB
  public Vector2i[] getBorders() {

    int size = 2 * z + 2 * w;
    Vector2i[] vecs = new Vector2i[size];

    // top / bottom row
    for(int i = 0; i < z; i ++) {
      vecs[i] = new Vector2i(x + i, y - 1);
      vecs[size - i - 1] = new Vector2i(x + i, y + w);
    }

    // middle pillars
    for(int i = 0; i < w; i ++) {
      vecs[z + i * 2] = new Vector2i(x - 1, y + i);
      vecs[z + i * 2 + 1] = new Vector2i(x + z, y + i);
    }

    return vecs;
  }
}
