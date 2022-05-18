package xyz.valnet.engine.graphics;

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

    public void draw(int x, int y, int w, int h) {
        Drawing.drawSprite(topLeft,        x,                            y,                            topLeft.width,                               topLeft.height);
        Drawing.drawSprite(top,            x + topLeft.width,            y,                            w - topLeft.width - topRight.width,          top.height);
        Drawing.drawSprite(topRight,       x + w - topRight.width,       y,                            topLeft.width,                               topLeft.height);
        Drawing.drawSprite(left,           x,                            y + topLeft.height,           left.width,                                  h - top.height - bottom.height);
        Drawing.drawSprite(center,         x + left.width,               y + top.height,               w - left.width - right.width,                h - top.height - bottom.height);
        Drawing.drawSprite(right,          x + w - right.width,          y + topRight.height,          right.width,                                 h - top.height - bottom.height);
        Drawing.drawSprite(bottomLeft,     x,                            y + h - bottomLeft.height,    bottomLeft.width,                            bottomLeft.height);
        Drawing.drawSprite(bottom,         x + bottomLeft.width,         y + h - bottom.height,        w - bottomLeft.width - bottomRight.width,    bottom.height);
        Drawing.drawSprite(bottomRight,    x + w - bottomRight.width,    y + h - bottomRight.height,   bottomLeft.width,                            bottomLeft.height);
    }

}
