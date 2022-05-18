package xyz.valnet.engine.util;

public class Math {
  public static float lerp(float a, float b, float n) {
    if(n >= 1) return b;
    if(n <= 0) return a;
    return a + (b - a) * n;
  }
}
