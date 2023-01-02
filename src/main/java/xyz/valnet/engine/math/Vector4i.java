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

}
