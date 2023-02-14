package xyz.valnet.engine.graphics;

public class LayoutStyle {
  public int width = 100, height = 32;

  public static LayoutStyle normal = LayoutStyle.sized(100, 32);
  public static LayoutStyle expand = LayoutStyle.sized(Integer.MAX_VALUE, Integer.MAX_VALUE);
  public static LayoutStyle shrink = LayoutStyle.sized(0, 0);
  public static LayoutStyle expandWidth = LayoutStyle.sized(Integer.MAX_VALUE, 32);

  public static LayoutStyle sized(int w, int h) {
    LayoutStyle btn = new LayoutStyle();
    btn.width = w;
    btn.height = h;
    return btn;
  }
}