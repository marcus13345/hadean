package xyz.valnet.engine.graphics;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;

public class Tile9 {
    
    private final Sprite topLeft;
    private final Sprite top;
    private final Sprite topRight;
    private final Sprite left;
    private final Sprite center;
    private final Sprite right;
    private final Sprite bottomLeft;
    private final Sprite bottom;
    private final Sprite bottomRight;

    public Tile9(
        Sprite topLeft,
        Sprite top,
        Sprite topRight,
        Sprite left,
        Sprite center,
        Sprite right,
        Sprite bottomLeft,
        Sprite bottom,
        Sprite bottomRight
    ) {
        this.topLeft = topLeft;
        this.top = top;
        this.topRight = topRight;
        this.left = left;
        this.center = center;
        this.right = right;
        this.bottomLeft = bottomLeft;
        this.bottom = bottom;
        this.bottomRight = bottomRight;
    }

    public void draw(Vector4f box) {
      draw(box.asInt());
    }

    public void draw(Vector4i box) {
      draw(box.x, box.y, box.z, box.w);
    }

    public void draw(int x, int y, int w, int h) {
      int a = w < 0 ? x + w : x; // top left x
      int b = h < 0 ? y + h : y; // top left y
      int c = w < 0 ? -w : w;    // abs width
      int d = h < 0 ? -h : h;    // abs height

      Drawing.drawSprite(topLeft,        a,                            b,                            topLeft.width,                               topLeft.height);
      Drawing.drawSprite(top,            a + topLeft.width,            b,                            c - topLeft.width - topRight.width,          top.height);
      Drawing.drawSprite(topRight,       a + c - topRight.width,       b,                            topLeft.width,                               topLeft.height);
      Drawing.drawSprite(left,           a,                            b + topLeft.height,           left.width,                                  d - top.height - bottom.height);
      Drawing.drawSprite(center,         a + left.width,               b + top.height,               c - left.width - right.width,                d - top.height - bottom.height);
      Drawing.drawSprite(right,          a + c - right.width,          b + topRight.height,          right.width,                                 d - top.height - bottom.height);
      Drawing.drawSprite(bottomLeft,     a,                            b + d - bottomLeft.height,    bottomLeft.width,                            bottomLeft.height);
      Drawing.drawSprite(bottom,         a + bottomLeft.width,         b + d - bottom.height,        c - bottomLeft.width - bottomRight.width,    bottom.height);
      Drawing.drawSprite(bottomRight,    a + c - bottomRight.width,    b + d - bottomRight.height,   bottomLeft.width,                            bottomLeft.height);
    }

}
