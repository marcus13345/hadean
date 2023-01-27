package xyz.valnet.engine.graphics;

public class Color {
  public final float r, g, b, a;

  public static Color black =   new Color(0, 0, 0);
  public static Color white =   new Color(1, 1, 1);

  public static Color red =     new Color(1, 0, 0);
  public static Color green =   new Color(0, 1, 0);
  public static Color blue =    new Color(0, 0, 1);

  public static Color yellow =  new Color(1, 1, 0);
  public static Color cyan =    new Color(0, 1, 1);
  public static Color magenta = new Color(1, 0, 1);

  public static Color orange =  new Color(1, 0.5f, 0);
  public static Color lime =    new Color(0.5f, 1, 0);
  public static Color aqua =    new Color(0, 1, 0.5f);
  public static Color indigo =  new Color(0, 0.5f, 1);
  public static Color purple =  new Color(0.5f, 0, 1);
  public static Color hotpink = new Color(1, 0, 0.5f);

  public Color(float r, float g, float b) {
    this(r, g, b, 1.0f);
  }

  public Color(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  public Color withAlpha(float a) {
    return new Color(r, g, b, a);
  }

  public Color brighter() {
    return new Color(
      (float) Math.sqrt(r),
      (float) Math.sqrt(g),
      (float) Math.sqrt(b),
      a
    );
  }

  public Color darker() {
    return new Color(
      (float) Math.pow(r, 2),
      (float) Math.pow(g, 2),
      (float) Math.pow(b, 2),
      a
    );
  }

  public static Color grey(float f) {
    return new Color(f, f, f);
  }
}
