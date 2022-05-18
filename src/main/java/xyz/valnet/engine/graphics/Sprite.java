package xyz.valnet.engine.graphics;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;

public class Sprite {
    public final Vector4f sourceBoxUV;
    private final Vector4i sourceBoxPixels;
    public final Texture atlas;
    public final int width;
    public final int height;

    public Sprite(Texture tex, Vector4i box) {
        sourceBoxPixels = box;
        sourceBoxUV = new Vector4f(
            sourceBoxPixels.x / (float) tex.width,
            sourceBoxPixels.y / (float) tex.height,
            sourceBoxPixels.z / (float) tex.width,
            sourceBoxPixels.w / (float) tex.height
        );
        atlas = tex;
        width = sourceBoxPixels.z;
        height = sourceBoxPixels.w;
    }


}
